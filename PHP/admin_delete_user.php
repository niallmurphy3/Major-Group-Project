<?php
include "databaseConfig.php";

$email = $_POST['email'];

mysqli_query($conn, "DELETE FROM students WHERE studentEmail='$email'");
mysqli_query($conn, "DELETE FROM teachers WHERE teacherEmail='$email'");
mysqli_query($conn, "DELETE FROM admins WHERE adminEmail='$email'");

echo "User deleted if existed";
?>