<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['user']) && isset($_POST['status'])) {
  $user = $_POST['user'];
  $status = $_POST['status'];
  $posts = $db->getMyPosts($user, $status);

  if($posts){
    $response["error"] = FALSE;
    $response["success_msg"] = "Funciona";
    $response["posts"] = $posts;
    echo json_encode($response);
  }
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No tiene posts disponibles";
    echo json_encode($response);
  }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No existe la categoría";
    echo json_encode($response);
}

?>
