<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['name']) && isset($_POST['lastname']) && isset($_POST['nickname']) && isset($_POST['email']) && isset($_POST['telephone']) && isset($_POST['address']) && isset($_POST['password']) && isset($_POST['image'])){
    $name = $_POST['name'];
    $lastname = $_POST['lastname'];
    $nickname = $_POST['nickname'];
    $email = $_POST['email'];
    $telephone = $_POST['telephone'];
    $password = $_POST['password'];
    $address = $_POST['address'];
    $image = $_POST['image'];
    $binaryimage = $image;
    list($type, $binaryimage) = explode(';', $binaryimage);
    list(, $binaryimage) = explode(',', $binaryimage);
    $image = base64_decode($binaryimage);

    if ($db->isUserExisted($email)){
        $response["error"] = TRUE;
        $response["error_msg"] = "Ya existe una cuenta con este email";
        echo json_encode($response);
    }
    else{
        $user = $db->storeUser($name, $lastname, $nickname, $email, $telephone, $password, $address, $image);
        if ($user){
            $response["error"] = FALSE;
            $response["success_msg"] = "Usuario registrado correctamente";
            echo json_encode($response);
        }
        else {
            $response["error"] = TRUE;
            $response["error_msg"] = "Error desconocido";
            echo json_encode($response);
        }
    }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Es necesario registrar nombre, email, contraseña, direccion y foto";
    echo json_encode($response);
}

?>
