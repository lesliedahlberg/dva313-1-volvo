<?php

include "connection.php";

//$time = $_POST["time"]; 
//$alias = $_POST["alias"]

$time = "12:00"; 
//$alias = $_POST["alias"]

$aliasID = 1;

$sql = "INSERT INTO session ( TimePlayed, IdAlias) VALUES ( $time, $aliasID)";
$result = mysqli_query($connection, $sql);

echo $conn->insert_id;

mysqli_close($connection);

?>
