<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['category'])) {
  $category = $_POST['category'];
  $posts = $db->getByCategory($category);

  if($posts){
    $response["error"] = FALSE;
    $response["success_msg"] = "Funciona";
    $response["posts"] = $posts;
    echo json_encode($response);
  }
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No se pueden obtener los post";
    echo json_encode($response);
  }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No existe la categoría";
    echo json_encode($response);
}



?>
