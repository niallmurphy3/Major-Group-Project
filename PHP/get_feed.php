<?php
include "databaseConfig.php";

$email = $_POST['email'];

$user = mysqli_fetch_assoc(
    mysqli_query($conn, "SELECT courseID FROM students WHERE studentEmail='$email'")
);

$course = $user['courseID'];

$result = mysqli_query(
    $conn,
    "SELECT * FROM posts WHERE courseCode='$course' ORDER BY postID DESC"
);

while ($row = mysqli_fetch_assoc($result)) {
    echo $row['studentEmail'] . "\n";
    echo $row['content'] . "\n";
    echo "-------------------\n";
}
?>