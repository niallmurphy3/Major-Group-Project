<?php
include "databaseConfig.php";

if(isset($_POST['email']) && isset($_POST['password'])){
    
    $email = $_POST['email'];
    $password = $_POST['password'];

    $sql = "SELECT studentID, studentName, password FROM students WHERE studentEmail='$email'";
    $result = mysqli_query($conn, $sql);

    if(mysqli_num_rows($result) > 0){

        $row = mysqli_fetch_assoc($result);

        if(password_verify($password, $row['password'])){

            echo json_encode([
                "status" => "success",
                "id" => $row['studentID'],
                "name" => $row['studentName']
            ]);
            exit;

        } else {
            echo json_encode([
                "status" => "error",
                "message" => "Invalid email or password"
            ]);
        }

    } else {
        echo json_encode([
            "status" => "error",
            "message" => "Invalid email or password"
        ]);
    }

} else {
    echo json_encode([
        "status" => "error",
        "message" => "Email and password required"
    ]);
}
?>