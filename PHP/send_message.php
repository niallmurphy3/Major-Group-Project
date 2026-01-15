<?php
include "databaseConfig.php";

$sender = $_POST['sender'];
$recipient = $_POST['recipient'];
$content = $_POST['content'];

$sql = "INSERT INTO message (content, studentEmail, recipientEmail)
        VALUES ('$content', '$sender', '$recipient')";

mysqli_query($conn, $sql);

echo json_encode(["status" => "success"]);
