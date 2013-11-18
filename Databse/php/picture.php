<?php
require_once 'dbinfo.php';

$email=$_GET['email'];
//$email="jonathan.eason@mavs.uta.edu";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$query = mysql_query("SELECT picture FROM users WHERE emailAddress='$email'");
$row = mysql_fetch_array($query);
$content = $row['picture'];

header('Content-type: image/jpg');
echo $content;
//echo $email;

?>