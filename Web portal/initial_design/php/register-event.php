<?php

include "connection.php";

$time = $_POST["time"]; 
$value = $_POST["value"]; 
$type = $_POST["type"];
$sessionId = $_POST["session"];


$sql = "INSERT INTO ValueTime ( Type,  Value, Time,  IdSession) 
					   VALUES ( '$type', $value,'$time', $sessionId)";


$result = mysqli_query($connection, $sql);

echo $connection->insert_id;



mysqli_close($connection);

?>
