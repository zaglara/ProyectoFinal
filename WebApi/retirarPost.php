<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['post'])) {
  $post = $_POST['post'];
  $update = $db->retirarPost($post);

  if($update){
    $response["error"] = FALSE;
    $response["success_msg"] = "El post se ha retirado";
    echo json_encode($response);
  }
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No se puede retirar el post";
    echo json_encode($response);
  }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No existe el post";
    echo json_encode($response);
}
