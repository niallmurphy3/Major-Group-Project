<?php
include "databaseConfig.php";

$email = $_POST['email'];
$password = $_POST['password'];

function checkUser($conn, $sql, $email, $password) {
    $result = mysqli_query($conn, $sql);

    if ($result && mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);

        if (password_verify($password, $row['password'])) {
            return [
                "status" => "success",
                "id" => $row['id'],
                "name" => $row['name'],
                "user_level" => $row['user_level']
            ];
        }
    }
    return null;
}


$sql = "SELECT studentID as id, studentName as name, password, user_level 
        FROM students WHERE studentEmail='$email'";

$response = checkUser($conn, $sql, $email, $password);
if ($response) {
    echo json_encode($response);
    exit;
}


$sql = "SELECT teacherId as id, teacherName as name, password, user_level 
        FROM teachers WHERE teacherEmail='$email'";

$response = checkUser($conn, $sql, $email, $password);
if ($response) {
    echo json_encode($response);
    exit;
}


$sql = "SELECT adminID as id, adminName as name, password, user_level 
        FROM admins WHERE adminEmail='$email'";

$response = checkUser($conn, $sql, $email, $password);
if ($response) {
    echo json_encode($response);
    exit;
}


echo json_encode([
    "status" => "error",
    "message" => "Invalid email or password"
]);
?>