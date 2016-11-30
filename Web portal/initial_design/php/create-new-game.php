<?php
include "connection.php";

$time = $_POST["time"]; 
$alias = $_POST["alias"];

$idmachine=1;


$sql = "SELECT Id from Alias WHERE name='$alias'";
$result = mysqli_query($connection, $sql);


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


 <!-- $stmt->bind_param('i',$id);

   /* execute query */
   $stmt->execute();

   /* Store the result (to get properties) */
   $stmt->store_result();

   /* Get the number of rows */
   $num_of_rows = $stmt->num_rows;

   /* Bind the result to variables */
   $stmt->bind_result($id, $first_name, $last_name, $username);

   while ($stmt->fetch()) {
        echo 'ID: '.$id.'<br>';
        echo 'First Name: '.$first_name.'<br>';
        echo 'Last Name: '.$last_name.'<br>';
        echo 'Username: '.$username.'<br><br>';
   }

   /* free results */
   $stmt->free_result();

   /* close statement */
   $stmt->close(); -->