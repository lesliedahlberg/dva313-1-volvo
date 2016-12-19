<?php
include "connection.php";

$duration = $_POST["duration"]; 
$score = $_POST["score"];
$published = $_POST["published"];
$sessionId = $_POST["session"];



$stmt = $connection->prepare("UPDATE Session set Duration=?, Published=?, Score=? WHERE Id=?");
$stmt->bind_param("siii", $duration, $published, $score, $sessionId);

$stmt->execute();


mysqli_close($connection);
?>
