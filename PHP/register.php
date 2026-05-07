<?php
include "databaseConfig.php";

if(isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['course'])){

    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
    $course = $_POST['course'];

    $sql = "INSERT INTO students (studentName, studentEmail, password, courseID)
        VALUES ('$name', '$email', '$hashedPassword', '$course')";

    if(mysqli_query($conn, $sql)){
        echo "Account created successfully";
    } else {
        echo "Error creating account";
    }

}else{
    echo "Missing fields";
}
?>