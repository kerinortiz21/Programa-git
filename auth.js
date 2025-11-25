// ========================================
// FUNCIONES DE AUTENTICACIÓN
// ========================================

import { auth } from './firebase-config.js';
import { signInWithEmailAndPassword, signOut, onAuthStateChanged } from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-auth.js';
import { loadEmployees } from './employees.js';
import { validateEmail, showError, showSuccess, showLoading, hideLoading } from './validations.js';

// ELEMENTOS DEL DOM 
const loginScreen = document.getElementById('loginScreen');
const dashboardScreen = document.getElementById('dashboardScreen');
const loginBtn = document.getElementById('loginBtn');
const logoutBtn = document.getElementById('logoutBtn');
const loginEmail = document.getElementById('loginEmail');
const loginPassword = document.getElementById('loginPassword');
const loginError = document.getElementById('loginError');

// FUNCIÓN DE LOGIN 
export async function login() {
    const email = loginEmail.value.trim();
    const password = loginPassword.value;

    // Validaciones
    if (!email || !password) {
        showLoginError('Por favor completa todos los campos');
        return;
    }

    if (!validateEmail(email)) {
        showLoginError('Por favor ingresa un correo electrónico válido');
        return;
    }

    if (password.length < 6) {
        showLoginError('La contraseña debe tener al menos 6 caracteres');
        return;
    }

    // Mostrar loading
    showLoading('Iniciando sesión...');
    loginBtn.disabled = true;

    try {
        await signInWithEmailAndPassword(auth, email, password);
        hideLoginError();
        loginEmail.value = '';
        loginPassword.value = '';
        showSuccess('Sesión iniciada exitosamente');
    } catch (error) {
        console.error('Error en login:', error);
        
        // Mensajes de error específicos
        let errorMessage = 'Error al iniciar sesión';
        
        switch (error.code) {
            case 'auth/invalid-email':
                errorMessage = 'El correo electrónico no es válido';
                break;
            case 'auth/user-disabled':
                errorMessage = 'Esta cuenta ha sido deshabilitada';
                break;
            case 'auth/user-not-found':
                errorMessage = 'No existe una cuenta con este correo';
                break;
            case 'auth/wrong-password':
                errorMessage = 'Contraseña incorrecta';
                break;
            case 'auth/invalid-credential':
                errorMessage = 'Usuario o contraseña incorrectos';
                break;
            case 'auth/too-many-requests':
                errorMessage = 'Demasiados intentos fallidos. Intenta más tarde';
                break;
            case 'auth/network-request-failed':
                errorMessage = 'Error de conexión. Verifica tu internet';
                break;
            default:
                errorMessage = 'Usuario o contraseña incorrectos';
        }
        
        showLoginError(errorMessage);
        showError(errorMessage);
    } finally {
        hideLoading();
        loginBtn.disabled = false;
    }
}

// ========== FUNCIÓN DE LOGOUT ==========
export function logout() {
    if (!confirm('¿Estás seguro de cerrar sesión?')) {
        return;
    }

    showLoading('Cerrando sesión...');
    
    signOut(auth)
        .then(() => {
            console.log('Sesión cerrada exitosamente');
            showSuccess('Sesión cerrada correctamente');
            hideLoading();
        })
        .catch((error) => {
            console.error('Error al cerrar sesión:', error);
            showError('Error al cerrar sesión. Intenta nuevamente');
            hideLoading();
        });
}

// ========== MOSTRAR/OCULTAR PANTALLAS ==========
function showLogin() {
    loginScreen.classList.remove('hidden');
    dashboardScreen.classList.add('hidden');
}

function showDashboard() {
    loginScreen.classList.add('hidden');
    dashboardScreen.classList.remove('hidden');
}

// ========== MANEJO DE ERRORES ==========
function showLoginError(message) {
    loginError.textContent = message;
    loginError.classList.remove('hidden');
}

function hideLoginError() {
    loginError.classList.add('hidden');
}

// ========== OBSERVADOR DE AUTENTICACIÓN ==========
onAuthStateChanged(auth, (user) => {
    if (user) {
        console.log('Usuario autenticado:', user.email);
        showDashboard();
        loadEmployees(); // Cargar empleados cuando se autentique
    } else {
        console.log('Usuario no autenticado');
        showLogin();
    }
});

// ========== EVENT LISTENERS ==========
loginBtn.addEventListener('click', login);
logoutBtn.addEventListener('click', logout);

// Permitir login con tecla Enter
loginPassword.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        login();
    }
});

loginEmail.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        login();
    }
});