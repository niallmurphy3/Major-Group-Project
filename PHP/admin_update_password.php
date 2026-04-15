<?php
include "databaseConfig.php";

$email = $_POST['email'];
$password = password_hash($_POST['password'], PASSWORD_DEFAULT);

$updated = false;


$sql = "UPDATE students SET password='$password' WHERE studentEmail='$email'";
if (mysqli_query($conn, $sql) && mysqli_affected_rows($conn) > 0) {
    $updated = true;
}


$sql = "UPDATE teachers SET password='$password' WHERE teacherEmail='$email'";
if (mysqli_query($conn, $sql) && mysqli_affected_rows($conn) > 0) {
    $updated = true;
}


$sql = "UPDATE admins SET password='$password' WHERE adminEmail='$email'";
if (mysqli_query($conn, $sql) && mysqli_affected_rows($conn) > 0) {
    $updated = true;
}

if ($updated) {
    echo "Password updated successfully";
} else {
    echo "User not found";
}
?>