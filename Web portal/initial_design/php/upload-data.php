<?php
include "connection.php";

$jsonData=$_POST["data"];

$time = $jsonData["time"]; 
$alias = $jsonData["alias"];
$idmachine=1;

$stmt = $connection->prepare("SELECT Id from Alias WHERE name=?");

$stmt->bind_param("s", $alias);

$stmt->execute();

$result = $stmt->get_result();

$idAlias="";
if(mysqli_num_rows($result)!=0){
$data=$result->fetch_assoc();
$idAlias=$data["Id"];
}
else{

$stmt = $connection->prepare("INSERT INTO Alias (Name) values(?)");

$stmt->bind_param("s", $alias);

$stmt->execute();

$idAlias=$connection->insert_id;

}


$stmt = $connection->prepare("INSERT INTO Session ( TimePlayed, IdAlias, IdMachine) VALUES ( ?, ? ,? )");

$stmt->bind_param("sii", $time, $idAlias, $idmachine);

$stmt->execute();

$session_id = $connection->insert_id;


$timeList = $jsonData["timeList"];
$type = ["LOAD", "Fuel", "DISTANCE","SPEED","RPM", "ALTITUDE"];

$load=$jsonData["load"];
$fuel=$jsonData["fuel"];
$distance=$jsonData["distance"];
$speed=$jsonData["speed"];
$rpm=$jsonData["rpm"];
$altitude=$jsonData["altitude"];

$values[]=$load;
$values[]=$fuel;
$values[]=$distance;
$values[]=$speed;
$values[]=$rpm;
$values[]=$altitude;


for ($i=0; $i <count($timeList) ; $i++) { 
for ($j=0; $j <6; $j++) { 

$stmt = $connection->prepare("INSERT INTO ValueTime ( Type,  Value, Time,  IdSession) 
	   VALUES ( ?, ?, ?, ?)");
$stmt->bind_param("sisi", $type[$i], $values[$i][$j], $timeList[$j], $session_id);

$stmt->execute();
}


}



$duration = $jsonData["duration"]; 
$score = $jsonData["score"];
$published = $jsonData["published"];



$stmt = $connection->prepare("UPDATE Session set Duration=?, Published=?, Score=? WHERE Id=?");
$stmt->bind_param("siii", $duration, $published, $score, $session_id);

$stmt->execute();



mysqli_close($connection);
?>
