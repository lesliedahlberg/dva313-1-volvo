
<?php

include "connection.php";


$data='%';

if(!empty($_POST['getData']) && $_POST['getData']!="All"){
$valu=(string) $_POST['getData'];
$data='Excavator';
}




$queryy="SELECT s.id,s.score,a.name,s.timePlayed,s.duration, m.name as machine, (select sum(vtt.value) from ValueTime as vtt where vtt.type='Fuel' and vtt.IdSession=s.id) as totalFuel,
(select sum(vtt.value) from ValueTime as vtt where vtt.type='DISTANCE' and vtt.IdSession=s.id) as totalDistance,
(select sum(vtt.value) from ValueTime as vtt where vtt.type='LOAD' and vtt.IdSession=s.id) as totalLoad,
(select ROUND(avg(vtt.value),0) from ValueTime as vtt where vtt.type='Fuel' and vtt.IdSession=s.id) as averageFuel,
(select ROUND(avg(vtt.value),0) from ValueTime as vtt where vtt.type='DISTANCE' and vtt.IdSession=s.id) as averageDistance,
(select ROUND(avg(vtt.value),0) from ValueTime as vtt where vtt.type='LOAD' and vtt.IdSession=s.id) as averageLoad,
(select ROUND(avg(vtt.value),0) from ValueTime as vtt where vtt.type='Speed' and vtt.IdSession=s.id) as averageSpeed, 
(select ROUND(avg(vtt.value),0) from ValueTime as vtt where vtt.type='RPM' and vtt.IdSession=s.id) as averageRpm,    
(select ROUND(avg(vtt.value),0) from ValueTime as vtt where vtt.type='ALTITUDE' and vtt.IdSession=s.id) as averageAltitude,
GROUP_CONCAT(DISTINCT v.time order by v.time asc) TIME, GROUP_CONCAT(case when v.type='Fuel' then v.value end ORDER BY v.time asc SEPARATOR ',') 
FUEL,GROUP_CONCAT(case when v.type='RPM' then v.value end ORDER BY v.time asc SEPARATOR ',') RPM, 
GROUP_CONCAT(case when v.type='SPEED' then v.value end ORDER BY v.time asc SEPARATOR ',') SPEED, 
GROUP_CONCAT(case when v.type='ALTITUDE' then v.value end ORDER BY v.time asc SEPARATOR ',') ALTITUDE, 
GROUP_CONCAT(case when v.type='DISTANCE' then v.value end ORDER BY v.time asc SEPARATOR ',') DISTANCE, 
GROUP_CONCAT(case when v.type='LOAD' then v.value end ORDER BY v.time asc SEPARATOR ',') LOADED 
FROM Session as s JOIN Alias as a ON (a.id = s.IdAlias) JOIN ValueTime as v ON (v.IdSession=s.id) JOIN Machine as m on (s.IdMachine=m.Id) WHERE m.name like ? GROUP BY s.id order by s.score desc LIMIT 200";

$stmt = $connection->prepare($queryy);
$stmt->bind_param('s', $data);

$stmt->execute();

$result = $stmt->get_result();

//echo $result;

$emparray = array();

while ($row = mysqli_fetch_assoc($result)){   
$emparray[] = $row;
}

$json = json_encode ( $emparray ); //JSON_FORCE_OBJECT
echo $json;


mysqli_close($connection);
?>
