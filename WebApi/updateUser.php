<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['lastname']) && isset($_POST['nickname']) && isset($_POST['telephone']) && isset($_POST['address'])){
    $id = $_POST['id'];
    $name = $_POST['name'];
    $lastname = $_POST['lastname'];
    $nickname = $_POST['nickname'];
    $telephone = $_POST['telephone'];
    $address = $_POST['address'];
    $user;

    if(isset($_POST['image'])){
        $image = $_POST['image'];
        $binaryimage = $image;
        list($type, $binaryimage) = explode(';', $binaryimage);
        list(, $binaryimage) = explode(',', $binaryimage);
        $image = base64_decode($binaryimage);
        $user = $db->updateUser($id, $name, $lastname, $nickname, $telephone, $address, $image);
    }
    else {
        $user = $db->updateUser2($id, $name, $lastname, $nickname, $telephone, $address);
    }

    //$user = $db->updateUser($id, $name, $lastname, $nickname, $telephone, $address, $image);
    if($user){
        $response["error"] = FALSE;
        $response["success_msg"] = "El usuario se actualizó con éxito";
        $response["user"]["id"] = $user["id"];
        $response["user"]["name"] = $user["nombre"];
        $response["user"]["lastname"] = $user["apellido"];
        $response["user"]["nickname"] = $user["nickname"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["telephone"] = $user["telefono"];
        $response["user"]["address"] = $user["direccion"];
        $response["user"]["image"] = "data:image/png;base64," . base64_encode($user["foto"]);
        echo json_encode($response);
    }
    else {
        $response["error"] = TRUE;
        $response["error_msg"] = "No se pudo actualizar el usuario";
        echo json_encode($response);
    }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No se pueden dejar campos en blanco";
    echo json_encode($response);
}

?>
