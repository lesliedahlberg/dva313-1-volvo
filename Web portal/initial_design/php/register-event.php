<?php

include "connection.php";

//$time = $_POST["time"]; 
//$alias = $_POST["alias"]

$time  = "12:00"; 
$value = "5"
$type  = "FUEL"

$session  = 1;

$sql = "INSERT INTO valuetime ( Type,  Value, Time,  IdSession) 
					   VALUES ( $type, $value,$time, $IdSession)";

$result = mysqli_query($connection, $sql);


mysqli_close($connection);

?>
