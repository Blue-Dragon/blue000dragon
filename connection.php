<?php


const HOST_NAME = "localhost";
const USERNAME = "root";
const PASSWORD = "";
const DB_NAME = "sample_sync";

$conn = new mysqli(HOST_NAME, USERNAME, PASSWORD, DB_NAME);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
//else{
//    echo "connected to ".  DB_NAME.  "<br/><br/>";
//}
?>