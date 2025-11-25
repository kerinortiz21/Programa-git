// ========================================
// FUNCIONES DE INTERFAZ DE USUARIO
// ========================================

import { createEmployee, updateEmployee, deleteEmployee, getEmployeeById, searchEmployees } from './employees.js';
import { validateEmployeeData, showSuccess, showError, showWarning, showLoading, hideLoading, setupRealtimeValidation, validateEmail, validatePhone, validateIdentification } from './validations.js';

// ========== ELEMENTOS DEL DOM ==========
const employeesTableBody = document.getElementById('employeesTableBody');
const noEmployees = document.getElementById('noEmployees');
const searchInput = document.getElementById('searchInput');
const newEmployeeBtn = document.getElementById('newEmployeeBtn');
const employeeModal = document.getElementById('employeeModal');
const modalTitle = document.getElementById('modalTitle');
const closeModalBtn = document.getElementById('closeModalBtn');
const cancelModalBtn = document.getElementById('cancelModalBtn');
const saveEmployeeBtn = document.getElementById('saveEmployeeBtn');

// Estadísticas
const totalEmployeesEl = document.getElementById('totalEmployees');
const activeEmployeesEl = document.getElementById('activeEmployees');
const inactiveEmployeesEl = document.getElementById('inactiveEmployees');
const departmentsEl = document.getElementById('departments');

// Variable para almacenar el ID del empleado en edición
let editingEmployeeId = null;

// ========== MOSTRAR EMPLEADOS EN TABLA ==========
export function displayEmployees(employees) {
    if (employees.length === 0) {
        employeesTableBody.innerHTML = '';
        noEmployees.classList.remove('hidden');
        return;
    }

    noEmployees.classList.add('hidden');
    
    employeesTableBody.innerHTML = employees.map(emp => `
        <tr class="hover:bg-gray-50 transition-colors">
            <td class="px-6 py-4">
                <div class="flex items-center gap-3">
                    <div class="bg-blue-100 rounded-full p-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-blue-600">
                            <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"></path>
                            <circle cx="12" cy="7" r="4"></circle>
                        </svg>
                    </div>
                    <div>
                        <p class="font-semibold text-gray-800">${emp.nombreCompleto || 'N/A'}</p>
                        <p class="text-sm text-gray-500">${emp.correo || 'N/A'}</p>
                    </div>
                </div>
            </td>
            <td class="px-6 py-4 text-gray-700">${emp.identificacion || 'N/A'}</td>
            <td class="px-6 py-4 text-gray-800 font-medium">${emp.cargo || 'N/A'}</td>
            <td class="px-6 py-4 text-gray-700">${emp.eps || 'N/A'}</td>
            <td class="px-6 py-4">
                <span class="px-3 py-1 rounded-full text-sm font-semibold ${
                    emp.estado === 'Activo' 
                        ? 'bg-green-100 text-green-700' 
                        : 'bg-orange-100 text-orange-700'
                }">
                    ${emp.estado || 'N/A'}
                </span>
            </td>
            <td class="px-6 py-4">
                <div class="flex items-center justify-center gap-2">
                    <button onclick="window.editEmployeeUI('${emp.id}')" class="bg-indigo-50 hover:bg-indigo-100 text-indigo-600 p-2 rounded-lg transition-colors" title="Editar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"></path>
                        </svg>
                    </button>
                    <button onclick="window.deleteEmployeeUI('${emp.id}')" class="bg-red-50 hover:bg-red-100 text-red-600 p-2 rounded-lg transition-colors" title="Eliminar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M3 6h18"></path>
                            <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path>
                            <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path>
                        </svg>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

// ========== ACTUALIZAR ESTADÍSTICAS ==========
export function updateStatistics(employees) {
    const total = employees.length;
    const activos = employees.filter(e => e.estado === 'Activo').length;
    const retirados = employees.filter(e => e.estado === 'Retirado').length;
    const cargos = new Set(employees.map(e => e.cargo).filter(Boolean)).size;

    totalEmployeesEl.textContent = total;
    activeEmployeesEl.textContent = activos;
    inactiveEmployeesEl.textContent = retirados;
    departmentsEl.textContent = cargos;
}

// ========== BÚSQUEDA DE EMPLEADOS ==========
function handleSearch() {
    const searchTerm = searchInput.value;
    const filtered = searchEmployees(searchTerm);
    displayEmployees(filtered);
}

// ========== ABRIR MODAL PARA NUEVO EMPLEADO ==========
function openNewEmployeeModal() {
    editingEmployeeId = null;
    modalTitle.textContent = 'Nuevo Empleado';
    clearForm();
    employeeModal.classList.remove('hidden');
}

// ========== EDITAR EMPLEADO ==========
window.editEmployeeUI = function(employeeId) {
    editingEmployeeId = employeeId;
    const employee = getEmployeeById(employeeId);
    
    if (!employee) {
        alert('Empleado no encontrado');
        return;
    }
    
    modalTitle.textContent = 'Editar Empleado';
    loadFormData(employee);
    employeeModal.classList.remove('hidden');
}

// ========== ELIMINAR EMPLEADO (APARTADO VISUAl) ==========
window.deleteEmployeeUI = async function(employeeId) {
    const employee = getEmployeeById(employeeId);
    
    if (!employee) {
        showError('Empleado no encontrado');
        return;
    }

    const confirmed = confirm(
        `¿Está seguro de eliminar a ${employee.nombreCompleto}?\n\n` +
        `Identificación: ${employee.identificacion}\n` +
        `Cargo: ${employee.cargo}\n\n` +
        `Esta acción no se puede deshacer.`
    );

    if (!confirmed) {
        return;
    }

    showLoading('Eliminando empleado...');

    const result = await deleteEmployee(employeeId);
    
    hideLoading();
    
    if (result.success) {
        showSuccess(`${employee.nombreCompleto} ha sido eliminado exitosamente`);
    } else {
        showError('Error al eliminar el empleado: ' + result.error);
    }
}

// ========== CERRAR MODAL ==========
function closeModal() {
    employeeModal.classList.add('hidden');
    clearForm();
    editingEmployeeId = null;
}

// ========== GUARDAR EMPLEADO ==========
async function saveEmployee() {
    const employeeData = getFormData();
    
    // Validación completa
    const validation = validateEmployeeData(employeeData);
    
    if (!validation.isValid) {
        const errorList = validation.errors.map((err, index) => 
            `${index + 1}. ${err}`
        ).join('\n');
        
        showError(
            `Se encontraron los siguientes errores:\n\n${errorList}`,
            8000
        );
        
        // Resaltar campos con error
        highlightInvalidFields(validation.errors);
        return;
    }

    showLoading(editingEmployeeId ? 'Actualizando empleado...' : 'Creando empleado...');

    let result;
    
    if (editingEmployeeId) {
        result = await updateEmployee(editingEmployeeId, employeeData);
        if (result.success) {
            showSuccess(`${employeeData.nombreCompleto} actualizado exitosamente`);
        }
    } else {
        result = await createEmployee(employeeData);
        if (result.success) {
            showSuccess(`${employeeData.nombreCompleto} creado exitosamente`);
        }
    }

    hideLoading();

    if (result.success) {
        closeModal();
    } else {
        showError('Error al guardar: ' + result.error, 6000);
    }
}

// ========== RESALTAR CAMPOS INVÁLIDOS ==========
function highlightInvalidFields(errors) {
    // Resetear todos los bordes
    const allInputs = document.querySelectorAll('#employeeModal input, #employeeModal select');
    allInputs.forEach(input => {
        input.classList.remove('border-red-500');
    });

    // Resaltar campos específicos basado en los errores
    const fieldMap = {
        'nombre': 'nombreCompleto',
        'identificación': 'identificacion',
        'correo': 'correo',
        'teléfono': 'telefono',
        'fecha de nacimiento': 'fechaNacimiento',
        'fecha de expedición': 'fechaExpedicion',
        'cargo': 'cargo',
        'fecha de ingreso': 'fechaIngreso',
        'fecha de retiro': 'fechaRetiro',
        'salario': 'salarioBasico',
        'auxilio': 'auxilioTransporte',
        'rodamiento': 'rodamiento'
    };

    errors.forEach(error => {
        const errorLower = error.toLowerCase();
        Object.keys(fieldMap).forEach(key => {
            if (errorLower.includes(key)) {
                const field = document.getElementById(fieldMap[key]);
                if (field) {
                    field.classList.add('border-red-500');
                    // Hacer scroll al primer campo con error
                    if (!document.querySelector('.border-red-500:not(#' + field.id + ')')) {
                        field.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }
                }
            }
        });
    });
}

// ========== OBTENER DATOS DEL FORMULARIO ==========
function getFormData() {
    return {
        nombreCompleto: document.getElementById('nombreCompleto').value.trim(),
        identificacion: document.getElementById('identificacion').value.trim(),
        lugarNacimiento: document.getElementById('lugarNacimiento').value.trim(),
        sexo: document.getElementById('sexo').value,
        fechaNacimiento: document.getElementById('fechaNacimiento').value,
        fechaExpedicion: document.getElementById('fechaExpedicion').value,
        estadoCivil: document.getElementById('estadoCivil').value,
        nivelEducacion: document.getElementById('nivelEducacion').value,
        direccion: document.getElementById('direccion').value.trim(),
        telefono: document.getElementById('telefono').value.trim(),
        correo: document.getElementById('correo').value.trim(),
        cargo: document.getElementById('cargo').value.trim(),
        fechaIngreso: document.getElementById('fechaIngreso').value,
        tipoContrato: document.getElementById('tipoContrato').value,
        salarioBasico: document.getElementById('salarioBasico').value,
        auxilioTransporte: document.getElementById('auxilioTransporte').value,
        rodamiento: document.getElementById('rodamiento').value,
        eps: document.getElementById('eps').value.trim(),
        fondoPension: document.getElementById('fondoPension').value.trim(),
        fondoCesantias: document.getElementById('fondoCesantias').value.trim(),
        cajaCompensacion: document.getElementById('cajaCompensacion').value.trim(),
        arl: document.getElementById('arl').value.trim(),
        banco: document.getElementById('banco').value.trim(),
        numeroCuenta: document.getElementById('numeroCuenta').value.trim(),
        estado: document.getElementById('estado').value,
        fechaRetiro: document.getElementById('fechaRetiro').value
    };
}

// ========== CARGAR DATOS EN EL FORMULARIO ==========
function loadFormData(employee) {
    Object.keys(employee).forEach(key => {
        const element = document.getElementById(key);
        if (element && key !== 'id') {
            element.value = employee[key] || '';
        }
    });
}

// ========== LIMPIAR FORMULARIO ==========
function clearForm() {
    const fields = [
        'nombreCompleto', 'identificacion', 'lugarNacimiento', 'sexo', 'fechaNacimiento',
        'fechaExpedicion', 'estadoCivil', 'nivelEducacion', 'direccion', 'telefono',
        'correo', 'cargo', 'fechaIngreso', 'tipoContrato', 'salarioBasico',
        'auxilioTransporte', 'rodamiento', 'eps', 'fondoPension', 'fondoCesantias',
        'cajaCompensacion', 'arl', 'banco', 'numeroCuenta', 'fechaRetiro'
    ];
    
    fields.forEach(field => {
        const element = document.getElementById(field);
        if (element) {
            element.value = '';
        }
    });
    
    document.getElementById('estado').value = 'Activo';
}

// ========== EVENT LISTENERS ==========
searchInput.addEventListener('input', handleSearch);
newEmployeeBtn.addEventListener('click', openNewEmployeeModal);
closeModalBtn.addEventListener('click', closeModal);
cancelModalBtn.addEventListener('click', closeModal);
saveEmployeeBtn.addEventListener('click', saveEmployee);

// ========== CONFIGURAR VALIDACIONES EN TIEMPO REAL ==========
document.addEventListener('DOMContentLoaded', () => {
    // Validar correo
    setupRealtimeValidation(
        'correo',
        validateEmail,
        'Ingresa un correo electrónico válido'
    );

    // Validar teléfono
    setupRealtimeValidation(
        'telefono',
        validatePhone,
        'Ingresa un teléfono válido (7-15 dígitos)'
    );

    // Validar identificación
    setupRealtimeValidation(
        'identificacion',
        validateIdentification,
        'La identificación debe contener solo números (6-15 dígitos)'
    );
});