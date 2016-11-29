<?php

include "connection.php";

//$time = $_POST["time"]; 
//$alias = $_POST["alias"]

$time = '12:00'; 
$alias = $_POST["alias"];

//$aliasID = 4;
$idmachine=1;

 $sql = $connection->prepare("SELECT id from Alias WHERE name=?");
$sql->bind_param('s', $alias);

$sql->execute();

$result = $sql->get_result();

$idAlias="";
if(mysqli_num_rows($result)!=0){
    $data=$result->fetch_assoc();
    $idAlias=$data["Id"];
}
else{
	 $sqlQuerry = "INSERT INTO Alias (Name) values('$alias')";
	 $result = mysqli_query($connection, $sqlQuerry);
	 $idAlias=$connection->insert_id;

}



$sql = "INSERT INTO Session ( TimePlayed, IdAlias, IdMachine) VALUES ( '$time', $idAlias, $idmachine)";
$result = mysqli_query($connection, $sql);


echo $connection->insert_id;

mysqli_close($connection);

?>
