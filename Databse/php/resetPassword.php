<?php
require_once 'dbinfo.php';

$email=$_POST['email'];
$password=$_POST['password'];

//$email="test@test.com";
//$password="12345678";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

// Create the query and insert
// into our database.
$query = "UPDATE users SET userPassword='$password' WHERE emailAddress='$email'";

$results = mysql_query($query, $dbConnection);

echo($results);
mysql_close();
?>