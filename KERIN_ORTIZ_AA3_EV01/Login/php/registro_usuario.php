<?php

include 'conexion.php'; /* llave que permite entrar a la base de datos */

$nombre = $_POST['nombre'];
$correo = $_POST['correo'];
$password = $_POST['password'];

/* Verifica que no haya campos vacios */
if (empty($nombre) || empty($correo) || empty($password)) {
    echo '
    <script>
        alert("Por favor, ingrese todos los datos.");
        window.location = "../index.php";
    </script>
    ';
    exit(); // Detiene la ejecución si algún campo está vacío
}

/* Verificacion para el correo no se repita*/ 

$verificar_correo = mysqli_query($conexion, "SELECT*FROM usuarios WHERE correo='$correo' ");
if(mysqli_num_rows($verificar_correo)>0) {
    echo '
    <script>

    alert("Este correo ya se encuentra en uso. Intenta con otro diferente");
    window.location = "../index.php";
    </script>
    ';
    exit();

}


/* Estas líneas son para almacenar los datos en la base de datos */
$query = "INSERT INTO usuarios(nombre, correo, password) 
          VALUES('$nombre', '$correo', '$password')";

$ejecutar = mysqli_query($conexion, $query); /* anteriormente se creo la variable de conexion para la base de datos y ahora se incluye aqui */

/* Código para que aparezca el mensaje de registrado */
if ($ejecutar) {
    
    echo '
    <script>
        alert("Usuario registrado exitosamente");
        window.location = "../index.php";
    </script>
    ';
} else {
    echo '
    <script>
        alert("Usuario no registrado. Inténtelo de nuevo");
        window.location = "../index.php";
    </script>
    ';
}

mysqli_close($conexion);

?>
