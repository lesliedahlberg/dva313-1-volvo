<?php

include "connection.php";

// $time = $_POST["time"]; 
// $duration = $_POST["duration"];
// $alias = $_POST["alias"]

$time = "12:00"; 
$duration = "30"
$alias = $_POST["alias"]


$aliasID = 1;
$sql = "INSERT INTO session (Duration, TimePlayed, IdAlias) VALUES ($duration, $time, $aliasID)";
$result = mysqli_query($connection, $sql);

echo $conn->insert_id;

mysqli_close($connection);

?>
