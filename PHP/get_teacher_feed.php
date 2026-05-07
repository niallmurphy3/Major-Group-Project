<?php
header('Content-Type: application/json');
include "databaseConfig.php";

$course = $_POST['course'] ?? '';

$result = mysqli_query(
    $conn,
    "SELECT postID, studentEmail, content
     FROM posts
     WHERE courseCode='$course'
     ORDER BY postID DESC"
);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode($data);
?>