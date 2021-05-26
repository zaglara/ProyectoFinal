<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['user'])) {
  $user = $_POST['user'];
  $posts = $db->getSavedPosts($user);

  if($posts){
    $response["error"] = FALSE;
    $response["success_msg"] = "Funciona";
    $response["posts"] = $posts;
    echo json_encode($response);
  }
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No tiene posts guardados";
    echo json_encode($response);
  }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No existe el usuario";
    echo json_encode($response);
}

?>
