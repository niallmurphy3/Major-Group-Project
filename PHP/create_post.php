<?php
header('Content-Type: application/json');
error_reporting(0);
ini_set('display_errors', 0);

include "databaseConfig.php";

$email = $_POST['email'] ?? '';
$content = $_POST['content'] ?? '';

$resultUser = mysqli_query($conn,
    "SELECT courseID FROM students WHERE studentEmail='$email'"
);

if (!$resultUser || mysqli_num_rows($resultUser) == 0) {
    echo json_encode(["status"=>"error"]);
    exit;
}

$user = mysqli_fetch_assoc($resultUser);
$course = $user['courseID'];

mysqli_query($conn,
    "INSERT INTO posts (studentEmail, courseCode, content)
     VALUES ('$email', '$course', '$content')"
);

echo json_encode(["status"=>"success"]);
?>