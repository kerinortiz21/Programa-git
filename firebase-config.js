
// CONFIGURACIÓN DE FIREBASE


import { initializeApp } from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js';
import { getAuth } from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-auth.js';
import { getFirestore } from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js';

// configuración de Firebase
const firebaseConfig = {
    apiKey: "AIzaSyAgMzH-w1yZNCObgvEARFjZny5sGqv9ZnE",
    authDomain: "sistema-gestion-talento-humano.firebaseapp.com",
    projectId: "sistema-gestion-talento-humano",
    storageBucket: "sistema-gestion-talento-humano.firebasestorage.app",
    messagingSenderId: "166995203222",
    appId: "1:166995203222:web:afbb508e9de093c198deb7"
};

// Inicializar Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

// Exportar para usar en otros archivos
export { auth, db };