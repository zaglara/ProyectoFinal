<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['post'])) {
  $post = $_POST['post'];
  $comments = $db->getComments($post);

  if($comments){
    $response["error"] = FALSE;
    $response["success_msg"] = "Funciona";
    $response["comments"] = $comments;
    echo json_encode($response);
  }
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No hay comentarios";
    echo json_encode($response);
  }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No existe el post";
    echo json_encode($response);
}



?>
