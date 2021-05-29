<?php

require_once 'db_function.php';
$db = new DB_Functions();

$response = array("error"=>FALSE);

if (isset($_POST['image']) && isset($_POST['ind']) && isset($_POST['user_id'])){
    $image = $_POST['image'];
    $index = $_POST['ind'];
    $user_id = $_POST['user_id'];
    $post_id = 0;

    $binaryimage = $image;
    list($type, $binaryimage) = explode(';', $binaryimage);
    list(, $binaryimage) = explode(',', $binaryimage);
    $image = base64_decode($binaryimage);

    $post = $db->getLastUserPost($user_id);
    
    if($post){
        $post_id = $post["id"];
        $postImg = $db->insertImgPost($image, $index, $post_id);
        if ($postImg) {
            $response["error"] = FALSE;
            $response["success_msg"] = "Post guardado";
            echo json_encode($response);
        }
        else {
            $response["error"] = TRUE;
            $response["error_msg"] = "No se pudo guardar la foto";
            echo json_encode($response);
        }
    }
    else {
        $response["error"] = TRUE;
        $response["error_msg"] = "No se encontró el post";
        echo json_encode($response);
    }
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "No hay imagenes";
    echo json_encode($response);
}

?>
