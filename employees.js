// ========================================
// FUNCIONES CRUD DE EMPLEADOS
// ========================================

import { db } from './firebase-config.js';
import { collection, addDoc, getDocs, doc, updateDoc, deleteDoc, onSnapshot, query } from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js';
import { displayEmployees, updateStatistics } from './ui.js';

// Variable global para almacenar empleados
export let allEmployees = [];

// CARGAR EMPLEADOS (Tiempo Real)
export function loadEmployees() {
    const q = query(collection(db, 'employees'));
    
    // Listener en tiempo real
    onSnapshot(q, (snapshot) => {
        allEmployees = [];
        snapshot.forEach((doc) => {
            allEmployees.push({ id: doc.id, ...doc.data() });
        });
        
        console.log('Empleados cargados:', allEmployees.length);
        displayEmployees(allEmployees);
        updateStatistics(allEmployees);
    });
}


// ========== CREAR EMPLEADO ==========
export async function createEmployee(employeeData) {
    try {
        const docRef = await addDoc(collection(db, 'employees'), employeeData);
        console.log('Empleado creado con ID:', docRef.id);
        return { success: true, id: docRef.id };
    } catch (error) {
        console.error('Error al crear empleado:', error);
        return { success: false, error: error.message };
    }
}

// ========== ACTUALIZAR / EDITAR EMPLEADO ==========
export async function updateEmployee(employeeId, employeeData) {
    try {
        const employeeRef = doc(db, 'employees', employeeId);
        await updateDoc(employeeRef, employeeData);
        console.log('Empleado actualizado:', employeeId);
        return { success: true };
    } catch (error) {
        console.error('Error al actualizar empleado:', error);
        return { success: false, error: error.message };
    }
}

// ========== ELIMINAR EMPLEADO ==========
export async function deleteEmployee(employeeId) {
    try {
        await deleteDoc(doc(db, 'employees', employeeId));
        console.log('Empleado eliminado:', employeeId);
        return { success: true };
    } catch (error) {
        console.error('Error al eliminar empleado:', error);
        return { success: false, error: error.message };
    }
}

// ========== OBTENER EMPLEADO POR ID ==========
export function getEmployeeById(employeeId) {
    return allEmployees.find(emp => emp.id === employeeId);
}

// ========== BUSCAR EMPLEADOS ==========
export function searchEmployees(searchTerm) {
    const term = searchTerm.toLowerCase().trim();
    
    if (!term) {
        return allEmployees;
    }
    
    return allEmployees.filter(emp => 
        (emp.nombreCompleto || '').toLowerCase().includes(term) ||
        (emp.identificacion || '').includes(term) ||
        (emp.cargo || '').toLowerCase().includes(term) ||
        (emp.correo || '').toLowerCase().includes(term)
    );
}