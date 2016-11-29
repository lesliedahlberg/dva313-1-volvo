<?php

include "connection.php";

$duration = $_POST["duration"]; 
$score = $_POST["score"];
$published = $_POST["published"];
$sessionId = $_POST["session"];


$sql = "UPDATE Session set Duration='$duration', Published=$published, Score=$score WHERE Id=$sessionId";


$result = mysqli_query($connection, $sql);


mysqli_close($connection);

?>
