<?php

$host ="localhost";
$usuarios="root";
$password="";
$basededatos="login_register_db";

$conexion=new mysqli($host,$usuarios,$password,$basededatos);  /* Esto crea la conexion con la base de datos en phpmyAdmin*/

/* Esto permite saber si se conectó o no con la base de datos*/
if($conexion->connect_error)(
    die("Conexión no establecida". $conexion->connect_error)


);

//*Recepcion de informacion

header("Content-Type: application/json"); /*Permite devolver un archivo json con informacion para mostrar, recibir o consumir datos*/
$metodo=$_SERVER['REQUEST_METHOD']; 

$path= isset($_SERVER['PATH_INFO'])?$_SERVER['PATH_INFO']:'/';

$searchId= explode('/', $path);
$id = (count($searchId) > 1) ? $searchId[1] : null;

//Estos son los métodos SQL para hacer las modificaciones de los registros con PHP
switch($metodo){
    // Consultas SELECT
    case 'GET':
        consultaSelect($conexion,$id);
        break;
    // Insertar datos
    case 'POST':
        insertar($conexion);
        break;
    // Update de usuarios
    case 'PUT':
        update($conexion,$id);
        break;
    // Eliminación de usuarios
    case 'DELETE':
        borrar($conexion,$id);
        break;
    default:
    echo "Método no permitido";
    break;
 }

 // Función que permite la consulta de usuarios en la base de datos
function consultaSelect($conexion, $id){
    $sql= ($id ===null)? "SELECT * FROM usuarios":"SELECT * FROM usuarios WHERE id=$id"; // Permite la búsqueda a través del ID
    $resultado= $conexion->query($sql);

    if($resultado){
        $dato= array();
        while($fila=$resultado->fetch_assoc()){
            $dato[]=$fila;

        }
        echo json_encode($dato);
    }

    //Verificación del id, para ver si se encuentra o no en la base de datos
    if (empty($dato)) {
        echo json_encode(array('Error' => 'No se encontraron usuarios asociados al id '.$id));
    } else {
        echo json_encode($dato);
    }


}

//Función que permite la inserción de datos
function insertar($conexion){

    //En este caso, la base de datos de ejemplo cuenta con dos columnas: nombre y correo.
    $dato_1= json_decode(file_get_contents('php://input'),true);
    $nombre= $dato_1['nombre'];
    $correo=$dato_1['correo'];
    $sql = "INSERT INTO usuarios(nombre, correo) VALUES ('$nombre', '$correo')";
    $resultado= $conexion->query($sql);

//Verifica si la inserción fue realizada o no, en dado caso no, muestra un mensaje de error.
    if($resultado){
        $dato_1['id'] = $conexion->insert_id;
        echo json_encode($dato_1);
    } else{
       echo json_encode(array('error'=>'Error al insertar un usuario'));
    }

}

//Permite la eliminación de datos
function borrar($conexion, $id) {
    // Este fragmento valida que el ID no sea nulo
    if ($id === null) {
        echo json_encode(array('error' => 'ID no proporcionado'));
        return;
    }

    // Permite que solo se ingresen valores enteros
    $id = intval($id);

    // Crear la consulta SQL para eliminar el registro
    $sql = "DELETE FROM usuarios WHERE id = $id";
    $resultado = $conexion->query($sql);

    // Verificación de el delete
    if ($resultado) {
        echo json_encode(array('mensaje' => 'Usuario eliminado'));
    } else {
        echo json_encode(array('error' => 'Error al eliminar usuario: ' . $conexion->error));
    }
}

//Permite la actualización de datos
function update($conexion, $id){
    $dato_1= json_decode(file_get_contents('php://input'),true);
    $nombre= $dato_1['nombre'];
    $correo=$dato_1['correo'];

    echo "El id a editar es: ".$id. "con el dato ".$nombre;
    $sql = "UPDATE usuarios SET nombre = '$nombre', correo='$correo' WHERE id =$id";
    $resultado= $conexion->query($sql);

    if ($resultado) {
        echo json_encode(array('Completado' => 'El usuario ha sido actualziado'));
    } else {
        echo json_encode(array('Error' => 'Error al actualizar el usuario: ' . $conexion->error));
    }


}




?>
