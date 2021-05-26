<?php

class DB_Functions{
    private $conn;

    function __construct() {
        require_once 'db_connect.php';
        $db = new DB_Connect();
        $this->conn = $db->connect();
    }

    function __destruct() {

    }

    public function storeUser($name, $lastname, $nickname, $email, $telephone, $password, $address, $image){
        $encrypted_password = password_hash($password, PASSWORD_BCRYPT);

        $stmt = $this->conn->prepare("INSERT INTO usuario(nombre, apellido, nickname, email, telefono, direccion, contrasena, foto) VALUES (?,?,?,?,?,?,?,?)");
        $stmt->bind_param("ssssssss", $name, $lastname, $nickname, $email, $telephone, $address, $encrypted_password, $image);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            return true;
        }
        else {
            return false;
        }
    }

    public function getUserByEmailAndPassword($email, $password){
        $stmt = $this->conn->prepare("SELECT * FROM usuario WHERE email = ?");
        $stmt->bind_param("s", $email);

        if ($stmt->execute()){
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            $encrypted_password = $user['contrasena'];
            //revisar que la contraseña sea la misma
            if (password_verify($password, $encrypted_password)){
                return $user;
            }
        }
        else{
            return NULL;
        }
    }

    public function isUserExisted($email){
        $stmt = $this->conn->prepare("SELECT email FROM usuario WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();

        if ($stmt->num_rows > 0){
            $stmt->close();
            return true;
        }
        else{
            $stmt->close();
            return false;
        }
    }

    public function updateUser($id, $name, $lastname, $nickname, $telephone, $address, $image){
        $stmt = $this->conn->prepare("UPDATE usuario SET nombre=?, apellido=?, nickname=?, telefono=?, direccion=?, foto = ? WHERE id = ?");
        $stmt->bind_param("sssssss", $name, $lastname, $nickname, $telephone, $address, $image, $id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            $stmt = $this->conn->prepare("SELECT * FROM usuario WHERE id = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        }
        else {
            return false;
        }
    }

    public function updateUser2($id, $name, $lastname, $nickname, $telephone, $address){
        $stmt = $this->conn->prepare("UPDATE usuario SET nombre=?, apellido=?, nickname=?, telefono=?, direccion=? WHERE id = ?");
        $stmt->bind_param("ssssss", $name, $lastname, $nickname, $telephone, $address, $id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            $stmt = $this->conn->prepare("SELECT * FROM usuario WHERE id = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        }
        else {
            return false;
        }
    }

    public function updatePassword($id, $password){
        $encrypted_password = password_hash($password, PASSWORD_BCRYPT);
        $stmt = $this->conn->prepare("UPDATE usuario SET contrasena = ? WHERE id = ?");
        $stmt->bind_param("ss", $encrypted_password, $id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            return true;
        }
        else {
            return false;
        }
    }

    public function storePet($description, $age, $details, $status, $animal, $user){
        $stmt = $this->conn->prepare("INSERT INTO publicacion(descripcion, edad, detalles, estado, id_especie, id_usuario) VALUES (?,?,?,?,?,?)");
        $stmt->bind_param("ssssss", $description, $age, $details, $status, $animal, $user);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            return true;
        }
        else {
            return false;
        }
    }

    public function getCategories(){
        $stmt = $this->conn->prepare("SELECT * FROM especie");

        if ($stmt->execute()){
            $categories = array();
            $result = $stmt->get_result();
            while ($row = $result->fetch_assoc()) {
                $categories[] = $row;
            }
            $stmt->close();
            return $categories;
        }
        else{
            return NULL;
        }
    }

    public function getMostRecent(){
        $stmt = $this->conn->prepare("SELECT publicacion.id, publicacion.descripcion, publicacion.edad, publicacion.fecha_hora, especie.especie, usuario.nombre, foto.foto FROM publicacion INNER JOIN especie ON publicacion.id_especie = especie.id INNER JOIN usuario ON publicacion.id_usuario = usuario.id INNER JOIN foto ON publicacion.id = foto.id_publicacion WHERE publicacion.estado = 'publicado' GROUP BY id ORDER BY fecha_hora DESC");

        if ($stmt->execute()){
            $posts = array();
            $result = $stmt->get_result();
            while ($row = $result->fetch_assoc()) {
                $row["foto"] = "data:image/png;base64," . base64_encode($row["foto"]);
                $posts[] = $row;
            }
            $stmt->close();
            return $posts;
        }
        else{
            return NULL;
        }
    }

    public function getByCategory($category){
        $stmt = $this->conn->prepare("SELECT publicacion.id, publicacion.descripcion, publicacion.edad, publicacion.fecha_hora, especie.especie, usuario.nombre, foto.foto FROM publicacion INNER JOIN especie ON publicacion.id_especie = especie.id INNER JOIN usuario ON publicacion.id_usuario = usuario.id INNER JOIN foto ON publicacion.id = foto.id_publicacion WHERE publicacion.estado = 'publicado' AND id_especie = ? GROUP BY id ORDER BY fecha_hora DESC");
        $stmt->bind_param("s", $category);

        if ($stmt->execute()){
            $posts = array();
            $result = $stmt->get_result();
            while ($row = $result->fetch_assoc()) {
                $row["foto"] = "data:image/png;base64," . base64_encode($row["foto"]);
                $posts[] = $row;
            }
            $stmt->close();
            return $posts;
        }
        else{
            return NULL;
        }
    }

    public function getLastUserPost($user_id){
        $stmt = $this->conn->prepare("SELECT * FROM publicacion WHERE id_usuario = ? ORDER BY id DESC LIMIT 1");
        $stmt->bind_param("s", $user_id);

        if ($stmt->execute()){
            $post = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $post;
        }
        else{
            return NULL;
        }
    }

    public function insertImgPost($image, $index, $post_id){
        $stmt = $this->conn->prepare("INSERT INTO foto(foto, indice, id_publicacion) VALUES (?,?,?)");
        $stmt->bind_param("sss", $image, $index, $post_id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            return true;
        }
        else {
            return false;
        }
    }

    public function getPost($id){
        $stmt = $this->conn->prepare("SELECT publicacion.id, publicacion.descripcion, publicacion.edad, publicacion.detalles, publicacion.fecha_hora, publicacion.id_usuario, especie.especie, usuario.nickname, usuario.telefono FROM publicacion INNER JOIN especie ON publicacion.id_especie = especie.id INNER JOIN usuario ON publicacion.id_usuario = usuario.id WHERE publicacion.id = ?");
        $stmt->bind_param("s", $id);

        if ($stmt->execute()){
            $post = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $post;
        }
        else{
            return NULL;
        }
    }

    public function getImages($id){
        $stmt = $this->conn->prepare("SELECT * FROM foto WHERE id_publicacion = ?");
        $stmt->bind_param("s", $id);

        if ($stmt->execute()){
            $fotos = array();
            $result = $stmt->get_result();
            while ($row = $result->fetch_assoc()) {
                $row["foto"] = "data:image/png;base64," . base64_encode($row["foto"]);
                $fotos[] = $row;
            }
            $stmt->close();
            return $fotos;
        }
        else{
            return NULL;
        }
    }

    public function getMyPosts($user, $status){
        $stmt = $this->conn->prepare("SELECT publicacion.id, publicacion.descripcion, publicacion.edad, publicacion.fecha_hora, especie.especie, usuario.nombre, foto.foto FROM publicacion INNER JOIN especie ON publicacion.id_especie = especie.id INNER JOIN usuario ON publicacion.id_usuario = usuario.id INNER JOIN foto ON publicacion.id = foto.id_publicacion WHERE publicacion.estado = ? AND id_usuario = ? GROUP BY id ORDER BY fecha_hora DESC");
        $stmt->bind_param("ss", $status, $user);

        if ($stmt->execute()){
            $posts = array();
            $result = $stmt->get_result();
            while ($row = $result->fetch_assoc()) {
                $row["foto"] = "data:image/png;base64," . base64_encode($row["foto"]);
                $posts[] = $row;
            }
            $stmt->close();
            return $posts;
        }
        else{
            return NULL;
        }
    }

    public function getComments($post){
        $stmt = $this->conn->prepare("SELECT comentario.id, comentario.comentario, usuario.nickname, usuario.foto FROM comentario INNER JOIN usuario ON comentario.id_usuario = usuario.id WHERE comentario.id_publicacion = ? ORDER BY id DESC");
        $stmt->bind_param("s", $post);

        if ($stmt->execute()){
            $comments = array();
            $result = $stmt->get_result();
            while ($row = $result->fetch_assoc()) {
                $row["foto"] = "data:image/png;base64," . base64_encode($row["foto"]);
                $comments[] = $row;
            }
            $stmt->close();
            return $comments;
        }
        else{
            return NULL;
        }
    }

    public function insertComment($comment, $post, $user){
        $stmt = $this->conn->prepare("INSERT INTO comentario(comentario, id_publicacion, id_usuario) VALUES (?,?,?)");
        $stmt->bind_param("sss", $comment, $post, $user);

        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            return true;
        }
        else {
            return false;
        }
    }

    public function searchPost($post, $user){
        $stmt = $this->conn->prepare("SELECT id FROM guardado WHERE id_publicacion = ? AND id_usuario = ?");
        $stmt->bind_param("ss", $post, $user);
        $stmt->execute();
        $stmt->store_result();

        if ($stmt->num_rows > 0){
            $stmt->close();
            return true;
        }
        else{
            $stmt->close();
            return false;
        }
    }

    public function insertGuardado($post, $user){
        $stmt = $this->conn->prepare("INSERT INTO guardado(id_publicacion, id_usuario) VALUES (?,?)");
        $stmt->bind_param("ss", $post, $user);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            return true;
        }
        else {
            return false;
        }
    }

    public function getSavedPosts($user){
        $stmt = $this->conn->prepare("SELECT publicacion.id, publicacion.descripcion, publicacion.edad, publicacion.fecha_hora, especie.especie, usuario.nombre, foto.foto FROM publicacion INNER JOIN especie ON publicacion.id_especie = especie.id INNER JOIN usuario ON publicacion.id_usuario = usuario.id INNER JOIN foto ON publicacion.id = foto.id_publicacion INNER JOIN guardado ON publicacion.id = guardado.id_publicacion WHERE publicacion.estado = 'publicado' AND guardado.id_usuario = ? GROUP BY id ORDER BY fecha_hora DESC");
        $stmt->bind_param("s", $user);

        if ($stmt->execute()){
            $posts = array();
            $result = $stmt->get_result();
            while ($row = $result->fetch_assoc()) {
                $row["foto"] = "data:image/png;base64," . base64_encode($row["foto"]);
                $posts[] = $row;
            }
            $stmt->close();
            return $posts;
        }
        else{
            return NULL;
        }
    }

    public function retirarPost($post){
        $stmt = $this->conn->prepare("UPDATE publicacion SET estado = 'adoptado' WHERE id = ?");
        $stmt->bind_param("s", $post);
        $result = $stmt->execute();
        $stmt->close();

        if ($result){
            return true;
        }
        else {
            return false;
        }
    }

}

?>
