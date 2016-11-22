<?php

include "connection.php";


if(isset($_GET["getData"])){
   getHighscores();
}


function getHighscores(){
$queryy="SELECT s.id,s.score,a.name,s.timePlayed,s.duration,GROUP_CONCAT(DISTINCT v.time order by v.time asc) TIME, GROUP_CONCAT(case when v.type='Fuel' then v.value end ORDER BY v.time asc SEPARATOR ',') FUEL,GROUP_CONCAT(case when v.type='RPM' then v.value end ORDER BY v.time asc SEPARATOR ',') RPM FROM Session as s
JOIN Alias as a ON (a.id = s.IdAlias) JOIN ValueTime as v ON (v.IdSession=s.id) GROUP BY s.id order by s.score desc LIMIT 20";

$result = mysqli_query($connection, $queryy);

$emparray = array();

while ($row = mysqli_fetch_assoc($result)){   
$emparray[] = $row;
}

echo json_encode($emparray);

//echo $emparray;

}


mysqli_close($connection);
?>