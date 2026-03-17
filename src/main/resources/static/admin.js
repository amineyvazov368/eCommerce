// admin-products.js
const apiBase = "http://localhost:8080/api/admin/products";

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
    productModal.style.display = "flex";
});

// Close Product Modal
productClose.addEventListener("click", () => productModal.style.display = "none");
window.addEventListener("click", e => {
    if (e.target === productModal) productModal.style.display = "none";
});

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

        const res = await fetch(apiBase, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) {
            console.error("Error fetching products:", res.status, await res.text());
            return;
        }

        const products = await res.json();
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
                    <button class="editBtn">Edit</button>
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
                productModal.style.display = "flex";
            };

            // Delete button
            tr.querySelector(".deleteBtn").onclick = async () => {
                if (!confirm("Are you sure to delete this product?")) return;

                try {
                    const deleteRes = await fetch(`${apiBase}/${p.id}`, {
                        method: "DELETE",
                        headers: {
                            "Authorization": `Bearer ${token}`
                        }
                    });

                    if (!deleteRes.ok) {
                        console.error("Failed to delete product:", deleteRes.status, await deleteRes.text());
                        alert("Failed to delete product!");
                        return;
                    }

                    fetchProducts();
                } catch (err) {
                    console.error("Delete error:", err);
                    alert("Delete error!");
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

        const res = await fetch(url, {
            method,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(productData)
        });

        if (!res.ok) {
            const errText = await res.text();
            console.error("Save product failed:", res.status, errText);
            alert("Failed to save product!");
            return;
        }

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

        const res = await fetch(`http://localhost:8080/api/admin/categories`, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) {
            console.error("Error fetching categories:", res.status, await res.text());
            return;
        }

        const categories = await res.json();
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
                    <button class="editBtn">Edit</button>
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
                    const deleteRes = await fetch(`http://localhost:8080/api/admin/categories/${cat.id}`, {
                        method: "DELETE",
                        headers: {
                            "Authorization": `Bearer ${token}`
                        }
                    });

                    if (!deleteRes.ok) {
                        console.error("Failed to delete category:", deleteRes.status, await deleteRes.text());
                        alert("Failed to delete category!");
                        return;
                    }

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

    const id = document.getElementById("categoryId").value;
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
            await fetch(`http://localhost:8080/api/admin/categories/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(categoryData)
            });
        } else {
            await fetch(`http://localhost:8080/api/admin/categories`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(categoryData)
            });
        }

        categoryModal.style.display = "none";
        fetchCategories();

    } catch (err) {
        console.error("Save category error:", err);
        alert("Error saving category!");
    }
});

// Navbar click switches
document.getElementById("navCategories").addEventListener("click", () => {
    document.getElementById("productsSection").style.display = "none";
    document.getElementById("categoriesSection").style.display = "block";
    fetchCategories();
});

document.getElementById("navProducts").addEventListener("click", () => {
    document.getElementById("productsSection").style.display = "block";
    document.getElementById("categoriesSection").style.display = "none";
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