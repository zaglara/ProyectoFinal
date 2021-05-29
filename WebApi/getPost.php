<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['id'])) {
	$id = $_POST['id'];
	$post = $db->getPost($id);

	if($post){
	  $response["error"] = FALSE;
	  $response["success_msg"] = "Datos del post";
	  $response["post"] = $post;
	  echo json_encode($response);
	}
	else {
	  $response["error"] = TRUE;
	  $response["error_msg"] = "No se pudo obtener los datos del post";
	  echo json_encode($response);
	}
}
else {
	$response["error"] = TRUE;
	$response["error_msg"] = "No hay forma de detectar el post";
	echo json_encode($response);
}

?>
