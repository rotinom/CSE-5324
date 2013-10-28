<?php
require_once 'dbinfo.php';

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery ="SELECT name FROM mainCategories";
$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

while($row=mysql_fetch_assoc($result))
  $output[]=$row;
print(json_encode($output));

mysql_close();
?>