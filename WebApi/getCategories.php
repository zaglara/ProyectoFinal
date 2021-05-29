<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

$res = $db->getCategories();

if($res){
  $response["error"] = FALSE;
  $response["success_msg"] = "Jala";
  $response["especies"] = $res;
  echo json_encode($response);
}
else {
  $response["error"] = TRUE;
  $response["error_msg"] = "No se pueden obtener las categorias";
  echo json_encode($response);
}


?>
