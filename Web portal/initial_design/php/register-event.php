<?php

include "connection.php";

//$time = $_POST["time"]; 
//$alias = $_POST["alias"]

$time  = '12:00'; 
$value = 5;
$type  = 'FUEL';

$sessionId  = 9;

$sql = "INSERT INTO ValueTime ( Type,  Value, Time,  IdSession) 
					   VALUES ( '$type', $value,'$time', $sessionId)";

echo $sql;

$result = mysqli_query($connection, $sql);


mysqli_close($connection);

?>
