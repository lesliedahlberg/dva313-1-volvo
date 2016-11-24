<?php

include "connection.php";


//if(isset($_POST["getData"])){
//   getHighscores();
//}


//function getHighscores(){
$queryy="SELECT s.id,s.score,a.name,s.timePlayed,s.duration,GROUP_CONCAT(DISTINCT v.time order by v.time asc) TIME, GROUP_CONCAT(case when v.type='Fuel' then v.value end ORDER BY v.time asc SEPARATOR ',') FUEL,GROUP_CONCAT(case when v.type='RPM' then v.value end ORDER BY v.time asc SEPARATOR ',') RPM, GROUP_CONCAT(case when v.type='SPEED' then v.value end ORDER BY v.time asc SEPARATOR ',') SPEED, GROUP_CONCAT(case when v.type='ALTITUDE' then v.value end ORDER BY v.time asc SEPARATOR ',') ALTITUDE, GROUP_CONCAT(case when v.type='DISTANCE' then v.value end ORDER BY v.time asc SEPARATOR ',') DISTANCE, GROUP_CONCAT(case when v.type='LOAD' then v.value end ORDER BY v.time asc SEPARATOR ',') "LOAD" FROM Session as s JOIN Alias as a ON (a.id = s.IdAlias) JOIN ValueTime as v ON (v.IdSession=s.id) GROUP BY s.id order by s.score desc LIMIT 20";

$result = mysqli_query($connection, $queryy);

//echo $result;

$emparray = array();

while ($row = mysqli_fetch_assoc($result)){   
$emparray[] = $row;
}

$json = json_encode ( $emparray ); //JSON_FORCE_OBJECT
echo $json;

//echo $emparray;

//}


mysqli_close($connection);
?>