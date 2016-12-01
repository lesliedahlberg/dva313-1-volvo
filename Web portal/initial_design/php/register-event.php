<?php

include "connection.php";

$time = $_POST["time"]; 
$value = $_POST["value"]; 
$type = $_POST["type"];
$sessionId = $_POST["session"];


$stmt = $connection->prepare("INSERT INTO ValueTime ( Type,  Value, Time,  IdSession) 
					   VALUES ( ?, ?, ?, ?)");
$stmt->bind_param("sisi", $type, $value, $time, $sessionId);

$stmt->execute();


mysqli_close($connection);
?>
