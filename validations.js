// ========================================
// SISTEMA DE VALIDACIONES Y MENSAJES
// ========================================

// ========== VALIDACIONES DE CAMPOS ==========

export function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

export function validatePhone(phone) {
    // Acepta formatos: 3001234567, 300-123-4567, 300 123 4567, etc.
    const phoneRegex = /^[0-9\s\-\(\)]{7,15}$/;
    return phoneRegex.test(phone);
}

export function validateIdentification(id) {
    // Solo números y entre 6 y 15 dígitos
    const idRegex = /^[0-9]{6,15}$/;
    return idRegex.test(id);
}

export function validateRequired(value) {
    return value && value.toString().trim().length > 0;
}

export function validateMinLength(value, minLength) {
    return value && value.toString().trim().length >= minLength;
}

export function validateMaxLength(value, maxLength) {
    return value && value.toString().trim().length <= maxLength;
}

export function validateNumber(value) {
    return !isNaN(value) && value >= 0;
}

export function validateDate(dateString) {
    const date = new Date(dateString);
    return date instanceof Date && !isNaN(date);
}

export function validateDateRange(startDate, endDate) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    return start <= end;
}

export function validateAge(birthDate, minAge = 18, maxAge = 100) {
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
    }
    
    return age >= minAge && age <= maxAge;
}

// ========== VALIDACIÓN COMPLETA DE EMPLEADO ==========

export function validateEmployeeData(employeeData) {
    const errors = [];

    // DATOS PERSONALES
    if (!validateRequired(employeeData.nombreCompleto)) {
        errors.push('El nombre completo es obligatorio');
    } else if (!validateMinLength(employeeData.nombreCompleto, 3)) {
        errors.push('El nombre completo debe tener al menos 3 caracteres');
    } else if (!validateMaxLength(employeeData.nombreCompleto, 100)) {
        errors.push('El nombre completo no puede exceder 100 caracteres');
    }

    if (!validateRequired(employeeData.identificacion)) {
        errors.push('La identificación es obligatoria');
    } else if (!validateIdentification(employeeData.identificacion)) {
        errors.push('La identificación debe contener solo números (6-15 dígitos)');
    }

    if (employeeData.correo && !validateEmail(employeeData.correo)) {
        errors.push('El correo electrónico no es válido');
    }

    if (employeeData.telefono && !validatePhone(employeeData.telefono)) {
        errors.push('El teléfono no es válido (debe tener 7-15 dígitos)');
    }

    if (employeeData.fechaNacimiento) {
        if (!validateDate(employeeData.fechaNacimiento)) {
            errors.push('La fecha de nacimiento no es válida');
        } else if (!validateAge(employeeData.fechaNacimiento, 18, 80)) {
            errors.push('La edad del empleado debe estar entre 18 y 80 años');
        }
    }

    if (employeeData.fechaExpedicion && employeeData.fechaNacimiento) {
        if (!validateDateRange(employeeData.fechaNacimiento, employeeData.fechaExpedicion)) {
            errors.push('La fecha de expedición no puede ser anterior a la fecha de nacimiento');
        }
    }

    // DATOS LABORALES
    if (!validateRequired(employeeData.cargo)) {
        errors.push('El cargo es obligatorio');
    } else if (!validateMinLength(employeeData.cargo, 3)) {
        errors.push('El cargo debe tener al menos 3 caracteres');
    }

    if (employeeData.fechaIngreso) {
        if (!validateDate(employeeData.fechaIngreso)) {
            errors.push('La fecha de ingreso no es válida');
        } else {
            const today = new Date();
            const ingreso = new Date(employeeData.fechaIngreso);
            if (ingreso > today) {
                errors.push('La fecha de ingreso no puede ser futura');
            }
        }
    }

    if (employeeData.fechaRetiro) {
        if (!validateDate(employeeData.fechaRetiro)) {
            errors.push('La fecha de retiro no es válida');
        } else if (employeeData.fechaIngreso) {
            if (!validateDateRange(employeeData.fechaIngreso, employeeData.fechaRetiro)) {
                errors.push('La fecha de retiro no puede ser anterior a la fecha de ingreso');
            }
        }
    }

    // Validar que si está retirado, debe tener fecha de retiro
    if (employeeData.estado === 'Retirado' && !employeeData.fechaRetiro) {
        errors.push('Si el empleado está retirado, debe especificar la fecha de retiro');
    }

    // DATOS NUMÉRICOS
    if (employeeData.salarioBasico && !validateNumber(employeeData.salarioBasico)) {
        errors.push('El salario básico debe ser un número válido');
    }

    if (employeeData.auxilioTransporte && !validateNumber(employeeData.auxilioTransporte)) {
        errors.push('El auxilio de transporte debe ser un número válido');
    }

    if (employeeData.rodamiento && !validateNumber(employeeData.rodamiento)) {
        errors.push('El rodamiento debe ser un número válido');
    }

    return {
        isValid: errors.length === 0,
        errors: errors
    };
}

// ========== SISTEMA DE NOTIFICACIONES ==========

export function showNotification(message, type = 'info', duration = 4000) {
    // Crear contenedor de notificaciones si no existe
    let container = document.getElementById('notificationContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'notificationContainer';
        container.className = 'fixed top-4 right-4 z-50 space-y-2';
        document.body.appendChild(container);
    }

    // Crear notificación
    const notification = document.createElement('div');
    notification.className = `notification max-w-md p-4 rounded-lg shadow-lg flex items-start gap-3 transform transition-all duration-300 ${getNotificationClasses(type)}`;
    
    notification.innerHTML = `
        ${getNotificationIcon(type)}
        <div class="flex-1">
            <p class="font-semibold">${getNotificationTitle(type)}</p>
            <p class="text-sm">${message}</p>
        </div>
        <button onclick="this.parentElement.remove()" class="text-current opacity-70 hover:opacity-100">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M18 6 6 18"></path>
                <path d="m6 6 12 12"></path>
            </svg>
        </button>
    `;

    container.appendChild(notification);

    // Animación de entrada
    setTimeout(() => {
        notification.style.opacity = '1';
        notification.style.transform = 'translateX(0)';
    }, 10);

    // Auto-eliminar
    if (duration > 0) {
        setTimeout(() => {
            notification.style.opacity = '0';
            notification.style.transform = 'translateX(100%)';
            setTimeout(() => notification.remove(), 300);
        }, duration);
    }
}

export function showSuccess(message, duration = 3000) {
    showNotification(message, 'success', duration);
}

export function showError(message, duration = 5000) {
    showNotification(message, 'error', duration);
}

export function showWarning(message, duration = 4000) {
    showNotification(message, 'warning', duration);
}

export function showInfo(message, duration = 4000) {
    showNotification(message, 'info', duration);
}

// ========== HELPERS DE NOTIFICACIONES ==========

function getNotificationClasses(type) {
    const classes = {
        success: 'bg-green-50 text-green-800 border-2 border-green-200',
        error: 'bg-red-50 text-red-800 border-2 border-red-200',
        warning: 'bg-yellow-50 text-yellow-800 border-2 border-yellow-200',
        info: 'bg-blue-50 text-blue-800 border-2 border-blue-200'
    };
    return classes[type] || classes.info;
}

function getNotificationIcon(type) {
    const icons = {
        success: `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-green-600 flex-shrink-0">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22 4 12 14.01 9 11.01"></polyline>
            </svg>
        `,
        error: `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-red-600 flex-shrink-0">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="15" x2="9" y1="9" y2="15"></line>
                <line x1="9" x2="15" y1="9" y2="15"></line>
            </svg>
        `,
        warning: `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-yellow-600 flex-shrink-0">
                <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"></path>
                <line x1="12" x2="12" y1="9" y2="13"></line>
                <line x1="12" x2="12.01" y1="17" y2="17"></line>
            </svg>
        `,
        info: `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-blue-600 flex-shrink-0">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="12" x2="12" y1="16" y2="12"></line>
                <line x1="12" x2="12.01" y1="8" y2="8"></line>
            </svg>
        `
    };
    return icons[type] || icons.info;
}

function getNotificationTitle(type) {
    const titles = {
        success: 'Éxito',
        error: 'Error',
        warning: 'Advertencia',
        info: 'Información'
    };
    return titles[type] || titles.info;
}

// ========== LOADING SPINNER ==========

export function showLoading(message = 'Cargando...') {
    let loader = document.getElementById('globalLoader');
    
    if (!loader) {
        loader = document.createElement('div');
        loader.id = 'globalLoader';
        loader.className = 'fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50';
        loader.innerHTML = `
            <div class="bg-white rounded-lg p-6 flex flex-col items-center gap-4">
                <div class="loader border-4 border-gray-200 border-t-blue-600 rounded-full w-12 h-12 animate-spin"></div>
                <p class="text-gray-700 font-semibold">${message}</p>
            </div>
        `;
        document.body.appendChild(loader);
    }
}

export function hideLoading() {
    const loader = document.getElementById('globalLoader');
    if (loader) {
        loader.remove();
    }
}

// ========== VALIDACIÓN DE CAMPOS EN TIEMPO REAL ==========

export function setupRealtimeValidation(fieldId, validationFn, errorMessage) {
    const field = document.getElementById(fieldId);
    if (!field) return;

    // Crear elemento de error si no existe
    let errorEl = field.nextElementSibling;
    if (!errorEl || !errorEl.classList.contains('field-error')) {
        errorEl = document.createElement('p');
        errorEl.className = 'field-error text-red-600 text-sm mt-1 hidden';
        field.parentNode.insertBefore(errorEl, field.nextSibling);
    }

    field.addEventListener('blur', () => {
        const value = field.value.trim();
        if (value && !validationFn(value)) {
            field.classList.add('border-red-500');
            errorEl.textContent = errorMessage;
            errorEl.classList.remove('hidden');
        } else {
            field.classList.remove('border-red-500');
            errorEl.classList.add('hidden');
        }
    });

    field.addEventListener('input', () => {
        if (field.classList.contains('border-red-500')) {
            const value = field.value.trim();
            if (!value || validationFn(value)) {
                field.classList.remove('border-red-500');
                errorEl.classList.add('hidden');
            }
        }
    });
}