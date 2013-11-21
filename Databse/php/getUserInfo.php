<?php
require_once 'dbinfo.php';

//save off user request info
$email=$_POST['email'];

//unit test parameters
//$email="jonathan.eason@mavs.uta.edu";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery ="SELECT firstName, lastName, zipcode, lat, lon FROM users WHERE emailAddress='$email'";


$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

while($row=mysql_fetch_assoc($result))
  $output[]=$row;
print(json_encode($output));

mysql_close();
?>