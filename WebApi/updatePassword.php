<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['id']) && isset($_POST['password'])){
    $id = $_POST['id'];
    $password = $_POST['password'];

    $user = $db->updatePassword($id, $password);
    if ($user){
      $response["error"] = FALSE;
      $response["success_msg"] = "Contraseña actualizada con exito";
      echo json_encode($response);
    }
    else {
      $response["error"] = TRUE;
      $response["error_msg"] = "Error desconocido";
      echo json_encode($response);
    }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Es necesario llenar los campos";
    echo json_encode($response);
}

?>
