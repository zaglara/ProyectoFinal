<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['description']) && isset($_POST['age']) && isset($_POST['details']) && isset($_POST['status']) && isset($_POST['category_id']) && isset($_POST['user_id'])){
    $description = $_POST['description'];
    $age = $_POST['age'];
    $details = $_POST['details'];
    $status = $_POST['status'];
    $animal = $_POST['category_id'];
    $user = $_POST['user_id'];

    $post = $db->storePet($description, $age, $details, $status, $animal, $user);
    if ($post){
        $response["error"] = FALSE;
        $response["success_msg"] = "Publicacion guardada correctamente";
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
    $response["error_msg"] = "Es necesario llenar todos los campos";
    echo json_encode($response);
}

?>
