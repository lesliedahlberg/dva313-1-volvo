<?php
$host="autoelektronikame.ipagemysql.com";
$username="mdhvolvo";
$password="volvocopilot11!";
$base="volvo";

$connection = new mysqli($host, $username, $password, $base);

if($connection->connect_error){
echo "Greska";
	die("Something Is Not Fine");
}
?>