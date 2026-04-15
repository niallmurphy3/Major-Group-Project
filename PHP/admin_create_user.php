<?php
include "databaseConfig.php";

$name = $_POST['name'];
$email = $_POST['email'];
$password = password_hash($_POST['password'], PASSWORD_DEFAULT);
$role = $_POST['role'];
$course = $_POST['course'];

if($role == "student"){
    $sql = "INSERT INTO students (studentName, studentEmail, password, courseID)
            VALUES ('$name', '$email', '$password', '$course')";
}
elseif($role == "teacher"){
    $sql = "INSERT INTO teachers (teacherName, teacherEmail, password)
            VALUES ('$name', '$email', '$password')";
}
elseif($role == "admin"){
    $sql = "INSERT INTO admins (adminName, adminEmail, password)
            VALUES ('$name', '$email', '$password')";
}

if(mysqli_query($conn, $sql)){
    echo "User created";
}else{
    echo "Error";
}
?>