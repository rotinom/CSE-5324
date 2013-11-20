<?php
require_once 'dbinfo.php';

$email=$_POST['email'];
$zip=$_POST['zip'];
$lat=$_POST['lat'];
$lon=$_POST['lon'];
$rate=$_POST['rate'];
$profile=$_POST['profile'];
$schedule=$_POST['schedule'];

//$email="test@test.com";
//$zip="76244";
//$lat="32.9458766";
//$lon="-97.276076";
//$rate="60";
//$profile="Best tutor in the world";
//$schedule="M-F 8AM-5PM";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery = "SELECT userId FROM users WHERE emailAddress='$email'";

$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

$row = mysql_fetch_row($result);
$id = $row[0];

if(!is_null($id))
{
  // Create the query and insert
  // into our database.
  $query = "INSERT INTO tutors (userId, zipcode, lat, lon, rate, profile, schedule) VALUES ('$id','$zip','$lat','$lon','$rate','$profile','$schedule')";
  $results = mysql_query($query, $dbConnection);
  echo($results);
}
else
{
  echo(0);
}

mysql_close();
?>