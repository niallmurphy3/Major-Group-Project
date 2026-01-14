<?php
include "databaseConfig.php";

if(isset($_POST['email']) && isset($_POST['password'])){
    $email = $_POST['email'];
    $password = $_POST['password'];


    $sqlStudent = "SELECT studentID, studentName FROM students WHERE studentEmail='$email' AND password='$password'";
    $resultStudent = mysqli_query($conn, $sqlStudent);

    if(mysqli_num_rows($resultStudent) > 0){
        $row = mysqli_fetch_assoc($resultStudent);
        echo json_encode([
            "status" => "success",
            "id" => $row['studentID'],
            "name" => $row['studentName']
        ]);
        exit;
    }
    
   
    echo json_encode([
        "status" => "error",
        "message" => "Invalid email or password"
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Email and password required"
    ]);
}
?>
