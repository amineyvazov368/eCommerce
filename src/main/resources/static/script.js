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
    initNavbar();
    fetchCategories();
    fetchProducts();
    fetchCart();
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

        if (res.ok) {
            const data = await res.json();
            const user = data.userDto; // real user object

            currentUser = {
                id: user.id,
                userName: user.userName,
                firstName: user.firstName,
                role: user.role,
                token: data.accessToken // JWT varsa
            };

            localStorage.setItem("currentUser", JSON.stringify(currentUser));
            // JWT token-i localStorage-də saxla
            localStorage.setItem("adminToken", data.accessToken);

            // Navbar update
            document.getElementById("userNameDisplay").style.display = "block";
            document.getElementById("userNameText").innerText = currentUser.userName || currentUser.firstName;
            document.getElementById("userActions").style.display = "none";

            // Logout düyməsini bağla
            document.getElementById("logoutBtn").onclick = logoutUser;

            document.getElementById("loginModal").style.display = "none";

            // Admin redirect
            if (currentUser.role === "ADMIN") {
                // admin token artıq localStorage-də var
                window.location.href = "admin.html";
            } else {
                fetchCart();
            }

        } else {
            const errText = await res.text();
            console.error("Login failed:", res.status, errText);
            alert("Login failed!");
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
        const headers = {};
        if (currentUser.token) headers["Authorization"] = `Bearer ${currentUser.token}`;

        const res = await fetch(`${apiBase}/api/carts/me`, {headers});
        if (res.ok) {
            const data = await res.json();
            cartItems = data.cartItems || [];

            document.getElementById("cartCount").innerText = cartItems.length;

            let total = cartItems.reduce((sum, item) => sum + parseFloat(item.price) * item.quantity, 0);
            document.getElementById("cartTotalPrice").innerText = total.toFixed(2) + '₼';

            renderCartDropdown(); // dropdown update
        } else {
            console.error("Fetch cart failed", await res.text());
        }
    } catch (err) {
        console.error("Fetch cart error", err);
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
    currentUser = null;           // user məlumatlarını sil
    cartItems = [];               // cart sıfırla
    document.getElementById("userNameDisplay").style.display = "none";
    document.getElementById("userActions").style.display = "block";
    document.getElementById("cartCount").innerText = 0; // cart count sıfırla
    document.getElementById("cartTotalPrice")?.remove(); // total price sil

    // opsional: home page refresh
    fetchProducts();
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
