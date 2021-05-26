<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['comment']) && isset($_POST['post']) && isset($_POST['user'])){
    $comment = $_POST['comment'];
    $post = $_POST['post'];
    $user = $_POST['user'];

    $commentPost = $db->insertComment($comment, $post, $user);
    if ($commentPost){
        $response["error"] = FALSE;
        $response["success_msg"] = "Comentario publicado";
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
    $response["error_msg"] = "Es necesario escribir un comentario";
    echo json_encode($response);
}

?>
