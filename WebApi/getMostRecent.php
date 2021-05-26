<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

$posts = $db->getMostRecent();

if($posts){
  $response["error"] = FALSE;
  $response["success_msg"] = "Funciona";
  $response["posts"] = $posts;
  echo json_encode($response);
}
else {
  $response["error"] = TRUE;
  $response["error_msg"] = "No se pueden obtener los mas recientes";
  echo json_encode($response);
}


?>
