
<?php

session_start();
if (isset($_SESSION['usuario'])) {

    header("location: bienvenido.php");
}

?>





<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login KyA</title>
    <link rel="stylesheet" href="webkya/css/estilos.css">
    <link rel="icon" href="webkya/images/Hechos con.png" type="image/png" />
</head>
<body>

    <main>

        <div class="contenedor_todo">

            <div class="caja_trasera">

                <div class="caja_trasera-login">

                    <h3> ¿Ya tienes una cuenta? </h3>
                    <p> Inicia sesión para ingresar en la página</p>
                    <button id="btn_iniciar-sesion"> Iniciar Sesión </button>
                </div>

                <div class="caja_trasera-register">

                    <h3> ¿No tienes una cuenta? </h3>
                    <p> Inicia sesión para ingresar en la página</p>
                    <button id="btn_registrarse"> Regístrate </button>
                </div>
            </div> 

            <!-- Este codigo es para la seccion de login y registro-->
                <div class="contenedor_login-register">
                    <!-- Este formulario es para el login-->
                    <form action="php/login_usuario.php" class="formulario_login" method="POST">

                        <h2> Iniciar Sesión </h2>
                        <input type="text" placeholder="Correo Electrónico" name = "correo">
                        <input type="password" placeholder="Contraseña" name = "password">
                        <button> Entrar </button>
                    </form>
                    <!-- Este formulario es para registarse-->
                    <form action="php/registro_usuario.php" method="POST" class="formulario_register">

                        <h2> Registrarse</h2>
                        <input type="text" placeholder="Nombre Completo" name = "nombre">
                        <input type="text" placeholder="Correo electrónico" name = "correo">
                        <input type="password" placeholder="Contraseña" name = "password">
                        <button> Guardar </button>

                    </form>
                </div>
            </div>
    </main>
<script src="webkya/js/script.js"></script>

</body>
</html>