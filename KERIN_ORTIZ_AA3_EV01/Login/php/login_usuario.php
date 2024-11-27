<?php

session_start(); // Asegúrate de iniciar la sesión
include 'conexion.php'; // Incluye la conexión a la base de datos

// Recibir datos del formulario
$correo = isset($_POST['correo']) ? trim($_POST['correo']) : '';
$password = isset($_POST['password']) ? trim($_POST['password']) : '';

// Validar si los campos están vacíos
if (empty($correo) || empty($password)) {
    echo '
    <script>
        alert("Por favor, complete todos los campos.");
        window.location = "../index.php";
    </script>
    ';
    exit(); // Detener ejecución si los campos están vacíos
}

// Validar los datos en la base de datos
$validar_login = mysqli_query($conexion, "SELECT * FROM usuarios WHERE correo='$correo' AND password='$password'");

if (mysqli_num_rows($validar_login) > 0) {
    // Si hay un registro válido
    $_SESSION['usuario'] = $correo;
    header("location: ../bienvenido.php");
    exit();
} else {
    // Si no hay coincidencias en la base de datos
    echo '
    <script>
        alert("Los datos son inválidos. Por favor verifica los datos.");
        window.location = "../index.php";
    </script>
    ';
    exit();
}

?>