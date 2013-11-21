<?php
require_once 'dbinfo.php';

//save off user request info
$email=$_POST['email'];
    
//unit test parameters
//$email="jonathan.eason@mavs.uta.edu";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery = "SELECT userId FROM users WHERE emailAddress='$email'";

$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

$row = mysql_fetch_row($result);
$id = $row[0];

if(!is_null($id))
{
    $sqlQuery ="SELECT * FROM tutors WHERE emailAddress='$email'";
    
    
    $result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());
    
    $row = mysql_fetch_row($result);
    $output = $row[0];
    print(json_encode($output));
}
else
{
  echo(0);
}

mysql_close();
?>