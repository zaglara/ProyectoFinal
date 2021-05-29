<?php

class DB_Connect{
    private $conn;
    
    public function connect() {
        require_once 'config.php';
        $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
        $this->conn->set_charset("utf8");
        return $this->conn;
    }
}

?>