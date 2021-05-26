<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['id'])) {
	$id = $_POST['id'];
	$res = $db->getImages($id);

	if($res){
	  $response["error"] = FALSE;
	  $response["postImages"] = $res;
	  echo json_encode($response);
	}
	else {
	  $response["error"] = TRUE;
	  $response["error_msg"] = "No se pueden obtener las categorias";
	  echo json_encode($response);
	}
}
else {
	$response["error"] = TRUE;
	$response["error_msg"] = "No existe el post";
	echo json_encode($response);
}

?>
