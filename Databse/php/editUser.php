<?php
require_once 'dbinfo.php';

$email=$_POST['email'];
$password=$_POST['password'];
$first=$_POST['first'];
$last=$_POST['last'];
$zip=$_POST['zip'];
$lat=$_POST['lat'];
$lon=$_POST['lon'];

//$email="test@test.com";
//$password="1234";
//$first="first";
//$last="last";
//$zip="76244";
//$lat="32.9458766";
//$lon="-97.276076";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

// Create the query and insert
// into our database.
$query = "UPDATE users SET emailAddress='$email', userPassword='$password', firstName='$first', lastName='$last', zipcode='$zip', lat='$lat', lon='$lon' WHERE emailAddress='$email'";

$results = mysql_query($query, $dbConnection);

echo($results);
mysql_close();
?>