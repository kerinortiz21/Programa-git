<?php


session_start();

/* codigo para que no se acceda a la sesion desde la ruta*/
if(!isset($_SESSION['usuario'])){
    echo'
    
    <script>
    
    alert("Por favor debes iniciar sesion");
    window.location = "index.php";
    </script>
    
    ';

    session_destroy();
    die();


} 




?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienvenido a Hecho con Amor.</title>
    <link rel="stylesheet" href="webkya/css/estilo2.css">
</head>
<body>
    <h1>
        ¡Bienvenid@ a Hecho con Amor. Disfruta de nuestro hermoso catálogo! :D
    </h1>

    <div class="btn_close-sesion">
    
    <button id="btn_cerrar-sesion" onclick="window.location.href='php/cerrar_sesion.php'">Cerrar Sesión</button>

    </div>
</body>
</html>