// Konfiqurasiya
const API_BASE = "http://localhost:8080/api";
let currentUser = { id: 1 }; // Test üçün default user

// State Management
let state = {
    products: [],
    categories: [],
    cart: { cartItems: [], totalPrice: 0 }
};

// --- API SERVICES ---
const api = {
    async fetchProducts() {
        const res = await fetch(`${API_BASE}/products`);
        return await res.json();
    },
    async fetchCategories() {
        const res = await fetch(`${API_BASE}/categories`);
        return await res.json();
    },
    async getCart() {
        const res = await fetch(`${API_BASE}/cart-items/${currentUser.id}`);
        return await res.json();
    },
    async addToCart(productId, quantity = 1) {
        const res = await fetch(`${API_BASE}/cart-items/add?userId=${currentUser.id}&productId=${productId}&quantity=${quantity}`, {
            method: 'POST'
        });
        return await res.json();
    },
    async updateQty(itemId, type) {
        const endpoint = type === 'inc' ? 'increase' : 'decrease';
        await fetch(`${API_BASE}/cart-items/${endpoint}/${itemId}`, { method: 'PUT' });
    }
};

// --- UI RENDERERS ---
const UI = {
    renderProducts(items) {
        const grid = document.getElementById('productGrid');
        grid.innerHTML = items.map(p => `
            <div class="product-card">
                <img src="${p.image}" alt="${p.name}" class="product-img">
                <h3>${p.name}</h3>
                <p class="price">${p.price} AZN</p>
                <button class="add-btn" onclick="handleAddToCart(${p.id})">
                    <i class="fas fa-cart-plus"></i> Səbətə at
                </button>
            </div>
        `).join('');
    },

    renderCategories(cats) {
        const container = document.getElementById('categoryList');
        const btns = cats.map(c => `
            <button class="cat-btn" onclick="filterByCategory('${c.name}')">${c.name}</button>
        `).join('');
        container.innerHTML += btns;
    },

    updateCartUI() {
        const list = document.getElementById('cartItemsList');
        const count = document.getElementById('cartCount');
        const total = document.getElementById('totalPrice');

        count.innerText = state.cart.length;
        let sum = 0;

        list.innerHTML = state.cart.map(item => {
            sum += item.price * item.quantity;
            return `
                <div class="cart-item">
                    <div>
                        <h4>${item.product.name}</h4>
                        <small>${item.price} AZN</small>
                    </div>
                    <div class="qty-controls">
                        <button onclick="handleQty(${item.id}, 'dec')">-</button>
                        <span>${item.quantity}</span>
                        <button onclick="handleQty(${item.id}, 'inc')">+</button>
                    </div>
                </div>
            `;
        }).join('');

        total.innerText = sum.toFixed(2);
    }
};

// --- EVENT HANDLERS (Global scope üçün window-a bağlanır) ---
window.handleAddToCart = async (id) => {
    try {
        await api.addToCart(id);
        await refreshCart();
        alert("Məhsul əlavə edildi!");
    } catch (err) {
        console.error("Xəta:", err);
    }
};

window.filterByCategory = async (categoryName) => {
    document.getElementById('loading').classList.remove('hidden');
    const url = categoryName === 'all'
        ? `${API_BASE}/products`
        : `${API_BASE}/products/search/by-category?category=${categoryName}`;

    const res = await fetch(url);
    const data = await res.json();
    UI.renderProducts(data);
    document.getElementById('loading').classList.add('hidden');
};

window.handleSearch = async () => {
    const query = document.getElementById('searchInput').value;
    const res = await fetch(`${API_BASE}/products/search/by-name?name=${query}`);
    const data = await res.json();
    UI.renderProducts(data);
};

async function refreshCart() {
    state.cart = await api.getCart();
    UI.updateCartUI();
}

// --- INITIALIZE ---
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const [products, categories] = await Promise.all([
            api.fetchProducts(),
            api.fetchCategories()
        ]);

        UI.renderProducts(products);
        UI.renderCategories(categories);
        await refreshCart();
    } catch (error) {
        document.getElementById('errorMsg').innerText = "Məlumatları yükləmək mümkün olmadı.";
        document.getElementById('errorMsg').classList.remove('hidden');
    }
});

// --- AUTH & ADMIN SERVICES ---

// 1. Login Funksiyası
// 1. Login Funksiyası (YENİLƏNMİŞ)
document.getElementById('loginForm').onsubmit = async (e) => {
    e.preventDefault();

    // Email əvəzinə userName göndəririk
    const payload = {
        userName: document.getElementById('loginUsername').value,
        password: document.getElementById('loginPass').value
    };

    try {
        const res = await fetch(`${API_BASE}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            const data = await res.json();
            // Əgər backend token qaytarırsa saxlayırıq
            if(data.token) localStorage.setItem('token', data.token);

            alert("Giriş uğurludur!");
            hideModal('authModal');
            location.reload();
        } else {
            const errorData = await res.json();
            alert("İstifadəçi adı və ya şifrə səhvdir!");
        }
    } catch (err) {
        console.error("Login xətası:", err);
        alert("Serverlə əlaqə kəsildi.");
    }
};

// 2. Qeydiyyat Funksiyası
document.getElementById('registerForm').onsubmit = async (e) => {
    e.preventDefault();
    const payload = {
        firstName: document.getElementById('regFirstName').value,
        lastName: document.getElementById('regLastName').value,
        userName: document.getElementById('regUsername').value,
        email: document.getElementById('regEmail').value,
        password: document.getElementById('regPass').value,
        role: "USER",
        isActive: true
    };

    const res = await fetch(`${API_BASE}/users/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        alert("Qeydiyyat tamamlandı! İndi daxil ola bilərsiniz.");
        switchTab('login');
    }
};

// 3. Məhsul Əlavə Etmə (Admin)
document.getElementById('addProductForm').onsubmit = async (e) => {
    e.preventDefault();
    const payload = {
        name: document.getElementById('prodName').value,
        description: document.getElementById('prodDesc').value,
        price: parseFloat(document.getElementById('prodPrice').value),
        stock: parseInt(document.getElementById('prodStock').value),
        image: document.getElementById('prodImage').value,
        category: { id: document.getElementById('prodCategory').value } // Backend-də Category ID gözlənilir
    };

    const res = await fetch(`${API_BASE}/admin/products`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        alert("Məhsul bazaya əlavə edildi!");
        hideModal('adminModal');
        location.reload(); // Siyahını yenilə
    }
};

// Yardımçı funksiyalar
window.showModal = (id) => document.getElementById(id).classList.remove('hidden');
window.hideModal = (id) => document.getElementById(id).classList.add('hidden');

window.switchTab = (type) => {
    if (type === 'login') {
        document.getElementById('loginForm').classList.remove('hidden');
        document.getElementById('registerForm').classList.add('hidden');
        document.getElementById('loginTab').classList.add('active');
        document.getElementById('registerTab').classList.remove('active');
    } else {
        document.getElementById('loginForm').classList.add('hidden');
        document.getElementById('registerForm').classList.remove('hidden');
        document.getElementById('loginTab').classList.remove('active');
        document.getElementById('registerTab').classList.add('active');
    }
};

// Admin panel üçün kateqoriya siyahısını doldurmaq
async function loadCategoriesIntoSelect() {
    const categories = await api.fetchCategories();
    const select = document.getElementById('prodCategory');
    select.innerHTML = categories.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
}
loadCategoriesIntoSelect();

// Modal Logic
const modal = document.getElementById('cartModal');
document.getElementById('cartBtn').onclick = () => modal.classList.remove('hidden');
document.querySelector('.close-modal').onclick = () => modal.classList.add('hidden');