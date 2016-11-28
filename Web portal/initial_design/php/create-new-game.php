<?php

include "connection.php";

//$time = $_POST["time"]; 
//$alias = $_POST["alias"]

$time = '12:00'; 
//$alias = $_POST["alias"]

$aliasID = 4;
$idmachine=1;

$sql = "INSERT INTO Session ( TimePlayed, IdAlias, IdMachine) VALUES ( '$time', $aliasID, $idmachine)";
$result = mysqli_query($connection, $sql);

echo $connection->insert_id;

mysqli_close($connection);

?>
