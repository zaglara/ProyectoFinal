<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['email']) && isset($_POST['password'])){
    $email = $_POST['email'];
    $password = $_POST['password'];
    
    $user = $db->getUserByEmailAndPassword($email, $password);
    
    if($user){
        $response["error"] = FALSE;
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
        $response["error_msg"] = "La información es incorrecta";
        echo json_encode($response);
    }
    
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Se requiere ingresar email y contraseña";
    echo json_encode($response);
}

?>