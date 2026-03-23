const apiBase = "http://localhost:8080"; // backend base URL
let currentUser = null;
let cartItems = [];

const savedUser = localStorage.getItem("currentUser");

if (savedUser) {
    currentUser = JSON.parse(savedUser);

    // UI update
    document.getElementById("userNameDisplay").style.display = "block";
    document.getElementById("userNameText").innerText =
        currentUser.userName || currentUser.firstName;

    document.getElementById("userActions").style.display = "none";

    document.getElementById("logoutBtn").onclick = logoutUser;

    // Cart yenidən yüklə
    fetchCart();
}

document.addEventListener("DOMContentLoaded", () => {
    const savedUser = localStorage.getItem("currentUser");

    if (savedUser) {
        currentUser = JSON.parse(savedUser);

        // Navbar update
        const userNameDisplay = document.getElementById("userNameDisplay");
        const userNameText = document.getElementById("userNameText");
        const userActions = document.getElementById("userActions");
        const logoutBtn = document.getElementById("logoutBtn");

        if (userNameDisplay) userNameDisplay.style.display = "block";
        if (userNameText) userNameText.innerText = currentUser.userName || currentUser.firstName;
        if (userActions) userActions.style.display = "none";
        if (logoutBtn) logoutBtn.onclick = logoutUser;

        // Admin page check (optional)
        if (currentUser.role === "ROLE_ADMIN" && window.location.pathname !== "/admin.html") {
            console.log("Admin is logged in on home page");

        }

        // Cart fetch only for normal users
        if (currentUser.role !== "ROLE_ADMIN") {
            fetchCart();
        }
    }

    // Navbar və kateqoriyalar / products load
    initNavbar();
    fetchCategories();
    fetchProducts();
});
// NAVBAR + MODALS
function initNavbar() {
    const loginBtn = document.getElementById("loginBtn");
    const registerBtn = document.getElementById("registerBtn");
    const loginModal = document.getElementById("loginModal");
    const registerModal = document.getElementById("registerModal");
    const loginClose = document.getElementById("loginClose");
    const registerClose = document.getElementById("registerClose");
    const toRegister = document.getElementById("toRegister");

    loginBtn.onclick = () => (loginModal.style.display = "flex");
    registerBtn.onclick = () => (registerModal.style.display = "flex");
    loginClose.onclick = () => (loginModal.style.display = "none");
    registerClose.onclick = () => (registerModal.style.display = "none");
    toRegister.onclick = () => {
        loginModal.style.display = "none";
        registerModal.style.display = "flex";
    };

    document.getElementById("loginSubmit").onclick = loginUser;
    document.getElementById("registerSubmit").onclick = registerUser;
}
async function apiFetch(url, options = {}) {
    let token = localStorage.getItem("adminToken");

    options.headers = {
        ...options.headers,
        "Authorization": "Bearer " + token
    };

    let res = await fetch(url, options);

    if (res.status === 403 || res.status === 401) {
        // Refresh token endpoint çağır
        const refreshRes = await fetch(`${apiBase}/api/users/refresh`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ refreshToken: localStorage.getItem("refreshToken") })
        });

        if (refreshRes.ok) {
            const refreshData = await refreshRes.json();
            localStorage.setItem("adminToken", refreshData.accessToken);

            // Retry original request
            options.headers["Authorization"] = "Bearer " + refreshData.accessToken;
            res = await fetch(url, options);
        } else {
            logoutUser();
            throw new Error("Session expired. Please login again.");
        }
    }

    return res.json();
}

// LOGIN
async function loginUser() {
    const username = document.getElementById("loginUserName").value;
    const password = document.getElementById("loginPassword").value;

    try {
        const res = await fetch(`${apiBase}/api/users/login`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({userName: username, password})
        });

        if (!res.ok) {
            const errText = await res.text();
            console.error("Login failed:", res.status, errText);
            alert("Login failed!");
            return;
        }

        const data = await res.json();
        console.log(data);

        const user = data.userDto || data.user;
        if (!user) {
            console.error("Login response doesn't contain user info", data);
            alert("Login failed! User info missing.");
            return;
        } // ✅ backend response ilə uyğun

        currentUser = {
            id: user.id,
            userName: user.userName,
            firstName: user.firstName,
            role: user.role,
            token: data.accessToken
        };

        // localStorage update
        localStorage.setItem("currentUser", JSON.stringify(currentUser));
        localStorage.setItem("adminToken", data.accessToken);
        localStorage.setItem("role", user.role); // ✅ burada user.role istifadə olundu

        // Navbar update
        document.getElementById("userNameDisplay").style.display = "block";
        document.getElementById("userNameText").innerText = currentUser.userName || currentUser.firstName;
        document.getElementById("userActions").style.display = "none";
        document.getElementById("logoutBtn").onclick = logoutUser;

        // Close login modal
        document.getElementById("loginModal").style.display = "none";

        // Admin redirect
        if (currentUser.role === "ROLE_ADMIN") {
            window.location.href = "admin.html";
        } else {
            fetchCart(); // normal user üçün basket load
        }

    } catch (err) {
        console.error(err);
        alert("Login error!");
    }
}

// REGISTER
async function registerUser() {
    const firstName = document.getElementById("regFirstName").value;
    const lastName = document.getElementById("regLastName").value;
    const userName = document.getElementById("regUserName").value;
    const email = document.getElementById("regEmail").value;
    const password = document.getElementById("regPassword").value;

    try {
        const res = await fetch(`${apiBase}/api/users/register`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({firstName, lastName, userName, email, password}),
        });

        if (res.ok) {
            document.getElementById("registerModal").style.display = "none";
            document.getElementById("loginModal").style.display = "flex";
            alert("Registration successful! Please login.");
        } else {
            alert("Registration failed!");
        }
    } catch (err) {
        console.error(err);
        alert("Registration error!");
    }
}

// FETCH CATEGORIES
async function fetchCategories() {
    const res = await fetch(`${apiBase}/api/categories`);
    const categories = await res.json();
    const container = document.getElementById("categoriesContainer");
    container.innerHTML = '';

    // 💡 “All Categories” düyməsini əlavə et
    const allBtn = document.createElement("button");
    allBtn.innerText = "All Categories";
    allBtn.onclick = () => fetchProducts(); // heç bir category göndərmədən bütün məhsullar
    container.appendChild(allBtn);

    categories.forEach(cat => {
        const btn = document.createElement("button");
        btn.innerText = cat.name;
        btn.onclick = () => fetchProducts(cat.name);
        container.appendChild(btn);
    });
}

// FETCH PRODUCTS
async function fetchProducts(category = null) {
    try {
        let url = `${apiBase}/api/products`;
        if (category) url = `${apiBase}/api/products/search/by-category?category=${encodeURIComponent(category)}`;
        const res = await fetch(url);
        if (res.ok) {
            const products = await res.json();
            renderProducts(products);
        }
    } catch (err) {
        console.error(err);
    }
}

// RENDER PRODUCTS
function renderProducts(products) {
    const container = document.getElementById("productsContainer");
    container.innerHTML = "";
    products.forEach((p) => {
        const card = document.createElement("div");
        card.classList.add("product-card");
        card.innerHTML = `
            <img src="${p.image}" alt="${p.name}">
            <h3>${p.name}</h3>
            <p>${p.price}₼</p>
            <button onclick="addToCart(${p.id},1)">Add to cart</button>
        `;
        container.appendChild(card);
    });
}

// SEARCH
document.getElementById("searchBtn").onclick = async () => {
    const query = document.getElementById("searchInput").value;
    if (!query) {
        fetchProducts();
        return;
    }
    try {
        const res = await fetch(`${apiBase}/api/products/search/by-name?name=${encodeURIComponent(query)}`);
        if (res.ok) {
            const products = await res.json();
            renderProducts(products);
        }
    } catch (err) {
        console.error(err);
    }
};

// FETCH CART
async function fetchCart() {
    if (!currentUser || !currentUser.id) return;

    try {
        const data = await apiFetch(`${apiBase}/api/carts/me`);
        cartItems = data.cartItems || [];

        // Cart count
        document.getElementById("cartCount").innerText = cartItems.length;

        // Total price
        let total = cartItems.reduce((sum, item) => sum + parseFloat(item.price) * item.quantity, 0);
        document.getElementById("cartTotalPrice").innerText = total.toFixed(2) + '₼';

        // Dropdown render
        renderCartDropdown();

    } catch (err) {
        console.error("Fetch cart failed", err);
    }
}

// ADD TO CART
async function addToCart(productId, qty) {
    if (!currentUser || !currentUser.id) {
        alert("You must login first!");
        return;
    }

    try {
        const url = `${apiBase}/api/cart-items/add?userId=${currentUser.id}&productId=${productId}&quantity=${qty}`;
        console.log("Add to cart URL:", url);

        const res = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": currentUser.token ? `Bearer ${currentUser.token}` : undefined
            }
        });

        if (res.ok) {
            await fetchCart(); // cart yenilənir
        } else {
            const errText = await res.text();
            console.error("Cart update failed:", res.status, errText);
            alert("Cart update failed!");
        }
    } catch (err) {
        console.error(err);
        alert("Add to cart error!");
    }
}

function logoutUser() {
    // 1️⃣ User və cart məlumatlarını sil
    currentUser = null;
    cartItems = [];

    // 2️⃣ localStorage-i təmizlə
    localStorage.removeItem("currentUser");
    localStorage.removeItem("adminToken");
    localStorage.removeItem("role");

    // 3️⃣ Navbar UI update
    document.getElementById("userNameDisplay").style.display = "none";
    document.getElementById("userActions").style.display = "block";

    // 4️⃣ Cart UI update
    const cartCountEl = document.getElementById("cartCount");
    if (cartCountEl) cartCountEl.innerText = 0;

    const cartTotalPriceEl = document.getElementById("cartTotalPrice");
    if (cartTotalPriceEl) cartTotalPriceEl.remove();

    // 5️⃣ Opsional: home page refresh / products reload
    fetchProducts();

    // 6️⃣ Redirect istəsən home page-ə
    window.location.href = "home.html";
}

function updateNavbarAfterLogin() {
    document.getElementById("userActions").style.display = "none";
    document.getElementById("userNameDisplay").style.display = "flex";
    document.getElementById("userNameText").innerText = currentUser.userName || currentUser.firstName;
    document.getElementById("logoutBtn").onclick = logoutUser;
}

// Cart icon click → toggle dropdown
document.getElementById("cartIcon").onclick = () => {
    const dropdown = document.getElementById("cartDropdown");
    dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
};

// Render cart items
function renderCartDropdown() {
    const container = document.getElementById("cartItemsContainer");
    container.innerHTML = '';

    if (!cartItems || cartItems.length === 0) {
        container.innerHTML = '<p>Cart is empty</p>';
        document.getElementById("cartDropdownTotal").innerText = '';
        return;
    }

    let total = 0;

    cartItems.forEach(item => {
        const name = item.productName || `Product #${item.productId}`;
        const div = document.createElement("div");
        div.classList.add("cart-item");

        div.innerHTML = `
            <span>${name}</span>
            <span>${item.price}₼ x ${item.quantity}</span>
            <div>
                <button class="increase">+</button>
                <button class="decrease">-</button>
                <button class="delete">x</button>
            </div>
        `;

        // Increase quantity
        div.querySelector(".increase").onclick = async () => {
            await fetch(`${apiBase}/api/cart-items/increase/${item.id}`, {method: "PUT"});
            await fetchCart();
        };

        // Decrease quantity
        div.querySelector(".decrease").onclick = async () => {
            await fetch(`${apiBase}/api/cart-items/decrease/${item.id}`, {method: "PUT"});
            await fetchCart();
        };

        // Delete item
        div.querySelector(".delete").addEventListener("click", async () => {
            try {
                const response = await fetch(`${apiBase}/api/cart-items/${item.id}`, {method: "DELETE"});
                await fetchCart();
                if (!response.ok) {
                    const text = await response.text();
                    console.error("Failed to delete item:", text);
                    alert("Item could not be deleted!");
                    return;
                }

                // Remove item from local cartItems array immediately (optional, faster UI feedback)
                cartItems = cartItems.filter(ci => ci.id !== item.id);


                // Re-render cart
                renderCartDropdown();

                // Optional: refetch cart from backend if needed
                // await fetchCart();

            } catch (error) {
                console.error("Error deleting item:", error);
                alert("Something went wrong!");
            }
        });

        container.appendChild(div);

        total += parseFloat(item.price) * item.quantity;
    });

    document.getElementById("cartDropdownTotal").innerText = `Total: ${total.toFixed(2)}₼`;
}

document.getElementById("createOrderBtn").onclick = async () => {
    if (!currentUser || !currentUser.token) {
        alert("Token tapılmadı, login olun!");
        return;
    }

    try {
        const response = await fetch(`${apiBase}/api/orders/cart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + currentUser.token
            }
        });

        if (!response.ok) throw new Error("Order creation failed");

        // Order uğurla yaradıldı → sadəcə alert veririk
        alert("Sifariş uğurla yaradıldı!");

        // İstəsən cartı da təmizləyə bilərsən burada
        updateCartUI([]); // updateCartUI = sənin cart update funksiyan
    } catch (error) {
        console.error(error);
        alert("Order yaradılmadı!");
    }
};

document.getElementById("myOrdersBtn").onclick = () => {
    // İstifadəçini order səhifəsinə yönləndiririk
    window.location.href = "order.html";
};


function updateCartUI(cartItems) {
    const container = document.getElementById("cartItemsContainer");
    container.innerHTML = '';
    let totalPrice = 0;

    cartItems.forEach(item => {
        const div = document.createElement("div");
        div.textContent = `${item.productName} x ${item.quantity} = ${item.price * item.quantity}₼`;
        container.appendChild(div);
        totalPrice += item.price * item.quantity;
    });

    document.getElementById("cartCount").textContent = cartItems.length;
    document.getElementById("cartTotalPrice").textContent = totalPrice + "₼";
}