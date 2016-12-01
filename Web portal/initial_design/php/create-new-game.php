<?php
include "connection.php";

$time = $_POST["time"]; 
$alias = $_POST["alias"];

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

echo $connection->insert_id;

mysqli_close($connection);
?>
