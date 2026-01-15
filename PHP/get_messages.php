<?php
include "databaseConfig.php";

$email = $_POST['email'];

$sql = "SELECT studentEmail, content
        FROM message
        WHERE recipientEmail = '$email'
        ORDER BY messageID DESC";

$result = mysqli_query($conn, $sql);

$messages = [];

while ($row = mysqli_fetch_assoc($result)) {
    $messages[] = $row;
}

echo json_encode($messages);
