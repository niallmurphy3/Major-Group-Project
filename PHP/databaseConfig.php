<?php
$servername = "localhost"; 
$username = "root"; 
$password = "Boba16329941!";
$dbname = "campuscompass"; 

$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn) {
    die(json_encode([
        "status" => "error",
        "message" => "Database connection failed"
    ]));
}
?>
