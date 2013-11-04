<?php
require_once 'dbinfo.php';

//save off user request info
$reqUser=$_POST['emailAddress'];
$reqPass=$_POST['userPassword'];

$dbConnection = mysql_connect($dbHost, $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery = "SELECT * FROM users WHERE emailAddress='$reqUser' AND userPassword='$reqPass'";

$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

if(mysql_num_rows($result) > 0)
 echo (1);
else
 echo (0);

mysql_close();
?>