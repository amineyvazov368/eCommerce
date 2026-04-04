// admin-products.js
const apiBase = "http://localhost:8080/api/admin/products";



// ------------------------
// ✅ API FETCH (token refresh support)
// ------------------------
async function apiFetch(url, options = {}) {
    let token = localStorage.getItem("adminToken");

    options.headers = {
        ...options.headers,
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    };

    let res = await fetch(url, options);

    // 🔥 ONLY 401 → refresh
    if (res.status === 401) {
        const refreshRes = await fetch("http://localhost:8080/api/users/refresh", {
            method: "POST",
            credentials: "include"
        });

        if (refreshRes.ok) {
            const refreshData = await refreshRes.json();
            localStorage.setItem("adminToken", refreshData.accessToken);

            // retry original request
            options.headers["Authorization"] = "Bearer " + refreshData.accessToken;
            res = await fetch(url, options);
        } else {
            alert("Session expired. Please login again.");
            window.location.href = "index.html";
            throw new Error("Session expired");
        }
    }

    // ❌ Əgər 403-dürsə → role problemidir
    if (res.status === 403) {
        throw new Error("Forbidden - You are not authorized");
    }

    // ❌ digər errorlar
    if (!res.ok) {
        throw new Error("API error: " + res.status);
    }

    // ✅ SAFE JSON parse
    const text = await res.text();
    return text ? JSON.parse(text) : null;
}
// ================================
// PRODUCT JS
// ================================
const productsTableBody = document.querySelector("#productsTable tbody");
const addProductBtn = document.getElementById("addProductBtn");
const productModal = document.getElementById("productModal");
const productModalTitle = document.getElementById("modalTitle");
const productClose = productModal.querySelector(".close");
const productForm = document.getElementById("productForm");

let editMode = false; // add or edit

// Open Add Product Modal
addProductBtn.addEventListener("click", () => {
    editMode = false;
    productForm.reset();
    productModalTitle.innerText = "Add Product";
    populateCategoryDropdown();
    productModal.style.display = "flex";
});

// Close Product Modal
productClose.addEventListener("click", () => productModal.style.display = "none");
window.addEventListener("click", e => {
    if (e.target === productModal) productModal.style.display = "none";
});


async function populateCategoryDropdown() {
    try {
        const categories = await apiFetch("http://localhost:8080/api/admin/categories");

        const select = document.getElementById("productCategoryId");
        select.innerHTML = `<option value="">Select Category</option>`; // təmizlə və default

        categories.forEach(cat => {
            const option = document.createElement("option");
            option.value = cat.id;      // backend üçün ID
            option.textContent = cat.name; // istifadəçi üçün ad
            select.appendChild(option);
        });

    } catch (err) {
        console.error("Failed to load categories for dropdown:", err);
    }
}

// Fetch all products
async function fetchProducts() {
    productsTableBody.innerHTML = "";
    try {
        const token = localStorage.getItem("adminToken");
        if (!token) {
            alert("Admin token not found. Please login first.");
            window.location.href = "index.html";
            return;
        }

        const products = await apiFetch(apiBase);
        if (!Array.isArray(products)) {
            console.error("Products is not an array:", products);
            return;
        }

        products.forEach(p => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.description}</td>
                <td>${p.stock}</td>
                <td>${p.price}₼</td>
                <td><img src="${p.image}" width="50" height="50"/></td>
                <td>
                    <button class="editBtn">Update</button>
                    <button class="deleteBtn">Delete</button>
                </td>
            `;

            // Edit button
            tr.querySelector(".editBtn").onclick = () => {
                editMode = true;
                productModalTitle.innerText = "Edit Product";
                document.getElementById("productId").value = p.id;
                document.getElementById("productName").value = p.name;
                document.getElementById("productDescription").value = p.description;
                document.getElementById("productStock").value = p.stock;
                document.getElementById("productPrice").value = p.price;
                document.getElementById("productImage").value = p.image;
                populateCategoryDropdown().then(() => {
                    document.getElementById("productCategoryId").value = p.category.id;
                });
                productModal.style.display = "flex";
            };

            // Delete button
            tr.querySelector(".deleteBtn").onclick = async () => {
                if (!confirm("Are you sure to delete this product?")) return;

                try {
                    await apiFetch(`${apiBase}/${p.id}`, { method: "DELETE" }); // <- apiFetch istifadə olunur
                    fetchProducts(); // refresh table
                } catch (err) {
                    console.error("Delete error:", err);
                    alert("Failed to delete product!");
                }
            };

            productsTableBody.appendChild(tr);
        });

    } catch (err) {
        console.error("Fetch products error:", err);
        alert("Fetch products error!");
    }
}

// Form submit for add/edit
productForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("productId").value;
    const name = document.getElementById("productName").value.trim();
    const description = document.getElementById("productDescription").value.trim();
    const stock = parseInt(document.getElementById("productStock").value);
    const price = parseFloat(document.getElementById("productPrice").value);
    const image = document.getElementById("productImage").value.trim();
    const categoryId = parseInt(document.getElementById("productCategoryId").value);

    const productData = {name, description, stock, price, image, CategoryId: categoryId};

    try {
        const token = localStorage.getItem("adminToken");
        if (!token) {
            alert("Admin token not found. Please login first.");
            window.location.href = "index.html";
            return;
        }

        let url = apiBase;
        let method = "POST";
        if (editMode) {
            url = `${apiBase}/${id}`;
            method = "PUT";
        }

        await apiFetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(productData)
        });

        productModal.style.display = "none";
        editMode = false;
        fetchProducts();

    } catch (err) {
        console.error("Save product error:", err);
        alert("Error saving product!");
    }
});

// ================================
// CATEGORY JS
// ================================
const categoriesTableBody = document.querySelector("#categoriesTable tbody");
const addCategoryBtn = document.getElementById("addCategoryBtn");
const categoryModal = document.getElementById("categoryModal");
const categoryForm = document.getElementById("categoryForm");
const categoryModalTitle = document.getElementById("categoryModalTitle");
const categoryClose = categoryModal.querySelector(".close");

let editCategoryMode = false;

// Open Add Category Modal
addCategoryBtn.addEventListener("click", () => {
    editCategoryMode = false;
    categoryForm.reset();
    categoryModalTitle.innerText = "Add Category";
    categoryModal.style.display = "flex";
});

// Close Category Modal
categoryClose.addEventListener("click", () => categoryModal.style.display = "none");
window.addEventListener("click", e => {
    if (e.target === categoryModal) categoryModal.style.display = "none";
});

// Fetch Categories
async function fetchCategories() {
    categoriesTableBody.innerHTML = "";
    try {
        const token = localStorage.getItem("adminToken");
        if (!token) {
            alert("Admin token not found. Please login first.");
            window.location.href = "index.html";
            return;
        }

        const categories = await apiFetch(`http://localhost:8080/api/admin/categories`);
        if (!Array.isArray(categories)) {
            console.error("Categories is not an array:", categories);
            return;
        }

        categories.forEach(cat => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${cat.id}</td>
                <td>${cat.name}</td>
                <td>
                    <button class="editBtn">Update</button>
                    <button class="deleteBtn">Delete</button>
                </td>
            `;

            // Edit Category
            tr.querySelector(".editBtn").onclick = () => {
                editCategoryMode = true;
                categoryModalTitle.innerText = "Edit Category";
                document.getElementById("categoryId").value = cat.id;
                document.getElementById("categoryName").value = cat.name;
                categoryModal.style.display = "flex";
            };

            // Delete Category
            tr.querySelector(".deleteBtn").onclick = async () => {
                if (!confirm("Are you sure to delete this category?")) return;

                try {
                    await apiFetch(`http://localhost:8080/api/admin/categories/${cat.id}`, { method: "DELETE" });

                    fetchCategories();
                } catch (err) {
                    console.error("Delete error:", err);
                    alert("Error deleting category!");
                }
            };

            categoriesTableBody.appendChild(tr);
        });

    } catch (err) {
        console.error("Fetch categories error:", err);
        alert("Error fetching categories!");
    }
}

// Category Form Submit
categoryForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("categoryId").value.trim();
    const name = document.getElementById("categoryName").value;
    const categoryData = {name};

    try {
        const token = localStorage.getItem("adminToken");
        if (!token) {
            alert("Admin token not found. Please login first.");
            window.location.href = "index.html";
            return;
        }

        if (id) {
            // Edit category
            await apiFetch(`http://localhost:8080/api/admin/categories/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(categoryData)
            });
        } else {
            // Add category
            await apiFetch(`http://localhost:8080/api/admin/categories`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(categoryData)
            });
        }

        categoryModal.style.display = "none";
        document.getElementById("categoryId").value = "";
        document.getElementById("categoryName").value = "";
        fetchCategories();

    } catch (err) {
        console.error("Save category error:", err);
        alert("Error saving category!");
    }
});

// Navbar click switches
document.getElementById("navUsers").addEventListener("click", () => {
    // Digər section-ları gizlət
    document.getElementById("productsSection").style.display = "none";
    document.getElementById("categoriesSection").style.display = "none";

    // Users section-u göstər
    document.getElementById("usersSection").style.display = "block";

    // Users-u fetch et
    fetchUsers();
});

// Məsələn, artıq mövcud olan Products və Categories listener-lar belə qalır:
document.getElementById("navCategories").addEventListener("click", () => {
    document.getElementById("productsSection").style.display = "none";
    document.getElementById("categoriesSection").style.display = "block";
    document.getElementById("usersSection").style.display = "none"; // əlavə et
    document.getElementById("ordersSection").style.display = "none";
    fetchCategories();
});

document.getElementById("navProducts").addEventListener("click", () => {
    document.getElementById("productsSection").style.display = "block";
    document.getElementById("categoriesSection").style.display = "none";
    document.getElementById("usersSection").style.display = "none"; // əlavə et
    document.getElementById("ordersSection").style.display = "none";
    fetchProducts();
});

// Initial fetch
document.addEventListener("DOMContentLoaded", fetchProducts);

const homeBtn = document.getElementById("homeBtn");

homeBtn.addEventListener("click", () => {
    // Əgər sənin home page index.html-dirsə:
    window.location.href = "home.html";

    // Əgər başqa path-dirsə dəyiş:
    // window.location.href = "/home.html";
});



// ================================
// USERS JS
// ================================
const usersTableBody = document.querySelector("#usersTable tbody");
const addUserBtn = document.getElementById("addUserBtn");
const userModal = document.getElementById("userModal");
const userModalTitle = document.getElementById("userModalTitle");
const userClose = userModal.querySelector(".close");
const userForm = document.getElementById("userForm");

let editUserMode = false;

// Open Add User Modal
// addUserBtn.addEventListener("click", () => {
//     editUserMode = false;
//     userForm.reset();
//     userModalTitle.innerText = "Add User";
//     userModal.style.display = "flex";
// });

// Close User Modal
userClose.addEventListener("click", () => userModal.style.display = "none");
window.addEventListener("click", e => {
    if (e.target === userModal) userModal.style.display = "none";
});

// Fetch all users
async function fetchUsers() {
    usersTableBody.innerHTML = "";
    try {
        const users = await apiFetch("http://localhost:8080/api/admin/users");

        users.forEach(u => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${u.id}</td>
                <td>${u.userName}</td>
                <td>${u.firstName}</td>
                <td>${u.lastName}</td>
                <td>${u.email}</td>
                <td>${u.role}</td>
                <td>
                    <button class="editBtn">Update</button>
                    <button class="deleteBtn">Ban</button>
                </td>
            `;

            // Edit button
            // Edit button - yalnız rol dəyişmək üçün
            tr.querySelector(".editBtn").onclick = () => {
                editUserMode = true;
                userModalTitle.innerText = "Change User Role";

                // User info göstəririk, amma read-only
                document.getElementById("userId").value = u.id;
                document.getElementById("userUsername").value = u.userName;
                document.getElementById("userFirstName").value = u.firstName;
                document.getElementById("userLastName").value = u.lastName;
                document.getElementById("userEmail").value = u.email;

                // Read-only etmək
                document.getElementById("userUsername").readOnly = true;
                document.getElementById("userFirstName").readOnly = true;
                document.getElementById("userLastName").readOnly = true;
                document.getElementById("userEmail").readOnly = true;

                // Rol dropdown-u set etmək
                const roleSelect = document.getElementById("userRole");
                roleSelect.innerHTML = `
        <option value="USER">User</option>
        <option value="ADMIN">Admin</option>
    `;
                roleSelect.value = u.role; // mövcud rol seçilir
                userModal.style.display = "flex";
            };

            // Delete button
            tr.querySelector(".deleteBtn").onclick = async () => {
                if (!confirm("Are you sure to delete this user?")) return;


                try {
                    // API call using apiFetch (JWT token already handled)
                    await apiFetch(`http://localhost:8080/api/admin/users/${u.id}/ban`, {
                        method: "PUT"
                    });

                    // Yenidən istifadəçiləri fetch et
                    fetchUsers();

                } catch (err) {
                    console.error("Delete user error:", err);
                    alert("Failed to delete user!");
                }
            };

            usersTableBody.appendChild(tr);
        });
    } catch (err) {
        console.error("Fetch users error:", err);
        alert("Error fetching users!");
    }
}

// User Form Submit
userForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("userId").value;
    const role = "ROLE_" + document.getElementById("userRole").value; // yalnız rol

    const userRoleData = { role }; // yalnız rol göndərilir

    try {
        // Yeni endpoint yalnız rol dəyişmək üçündür
        await apiFetch(`http://localhost:8080/api/admin/users/${id}/role`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userRoleData)
        });

        userModal.style.display = "none";
        editUserMode = false;
        fetchUsers(); // table-i refresh et

    } catch (err) {
        console.error("Update user role error:", err);
        alert("Failed to update user role!");
    }
});

// Navbar click switches
document.getElementById("navUsers").addEventListener("click", () => {
    document.getElementById("productsSection").style.display = "none";
    document.getElementById("categoriesSection").style.display = "none";
    document.getElementById("usersSection").style.display = "block";
    document.getElementById("ordersSection").style.display = "none";
    // fetchUsers();
});

// Admin navbar link
const navOrders = document.createElement('a');
navOrders.href = "#";
navOrders.id = "navOrders";
navOrders.textContent = "Orders";
document.querySelector(".nav-left ul").appendChild(navOrders);

const ordersSection = document.getElementById("ordersSection");
navOrders.addEventListener("click", () => {
    hideAllSections();
    ordersSection.style.display = "block";
    fetchOrders();
});

// Bütün digər bölmələri gizlədən funksiya
function hideAllSections() {
    document.getElementById("productsSection").style.display = "none";
    document.getElementById("categoriesSection").style.display = "none";
    document.getElementById("usersSection").style.display = "none";
    ordersSection.style.display = "none";
}

// Backend base URL
const apiBases = "http://localhost:8080";

// Fetch orders
async function fetchOrders() {
    try {
        const currentUser = JSON.parse(localStorage.getItem("currentUser"));
        const adminToken = currentUser?.token;

        if (!adminToken) {
            alert("Admin olaraq login olun!");
            return;
        }

        const response = await fetch(`${apiBases}/api/admin/orders`, {
            headers: {
                "Authorization": "Bearer " + adminToken
            }
        });

        if (!response.ok) throw new Error(`Failed to fetch orders: ${response.status}`);

        const orders = await response.json();
        renderOrders(orders);

    } catch (err) {
        console.error(err);
        alert("Orderləri gətirmək mümkün olmadı!");
    }
}

// Render orders table
function renderOrders(orders) {
    const tbody = document.querySelector("#ordersTable tbody");
    tbody.innerHTML = "";

    if (!orders || orders.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;">Heç bir order yoxdur</td></tr>`;
        return;
    }

    orders.forEach(order => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${order.id}</td>
            <td>${order.username || "Unknown"}</td>
            <td>${order.totalPrice}₼</td>
            <td>${order.status}</td>
            <td>${order.createdAt ? new Date(order.createdAt).toLocaleString() : "Tarix yoxdur"}</td>
            <td>
                <button class="cancelOrderBtn" data-id="${order.id}">Cancel</button>
            </td>
        `;

        // Cancel button
        tr.querySelector(".cancelOrderBtn").onclick = async () => {
            const confirmCancel = confirm("Siz əminsiniz ki, bu orderi ləğv etmək istəyirsiniz?");
            if (!confirmCancel) return;

            try {
                const orderId = tr.querySelector(".cancelOrderBtn").dataset.id;
                const currentUser = JSON.parse(localStorage.getItem("currentUser"));
                const adminToken = currentUser?.token;

                if (!adminToken) {
                    alert("Admin olaraq login olun!");
                    return;
                }

                const cancelResp = await fetch(`${apiBases}/api/admin/orders/cancel/${orderId}`, {
                    method: "PUT",
                    headers: {
                        "Authorization": "Bearer " + adminToken
                    }
                });

                if (!cancelResp.ok) throw new Error("Cancel failed");
                alert("Order uğurla ləğv edildi!");
                fetchOrders();
            } catch (err) {
                console.error(err);
                alert("Order ləğv edilə bilmədi!");
            }
        };

        tbody.appendChild(tr);
    });
}