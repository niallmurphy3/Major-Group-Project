<?php
include "databaseConfig.php";

$postID = $_POST['postID'] ?? '';

if ($postID == '') {
    echo "missing postID";
    exit;
}

$result = mysqli_query($conn, "DELETE FROM posts WHERE postID='$postID'");

if ($result) {
    echo "success";
} else {
    echo "error";
}
?>