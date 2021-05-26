<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['post']) && isset($_POST['user'])){
    $post = $_POST['post'];
    $user = $_POST['user'];

    $searchPost = $db->searchPost($post, $user);
    if ($searchPost) {
        $response["error"] = TRUE;
        $response["error_msg"] = "Este post ya está guardado";
        echo json_encode($response);
    }
    else {
        $savePost = $db->insertGuardado($post, $user);
        if ($savePost){
            $response["error"] = FALSE;
            $response["success_msg"] = "Post guardado exitosamente";
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
    $response["error_msg"] = "Error";
    echo json_encode($response);
}

?>
