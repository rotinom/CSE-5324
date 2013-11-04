<?php
require_once 'dbinfo.php';

//save off user request info
$subcat=$_POST['subcat'];
$radius=$_POST['radius'];
$lat=$_POST['lat'];
$lon=$_POST['lon'];

//unit test parameters
//$subcat="23";
//$radius="25";
//$lat="32.8";
//$lon="-97.1";
//echo "<h1>subcat=" . $subcat . "</h1>";
//echo "<h1>radius=" . $radius . "</h1>";
//echo "<h1>lat=" . $lat . "</h1>";
//echo "<h1>lon=" . $lon . "</h1>";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery ="SELECT users.firstName, tutors.rate, tutors.rating, tutors.picture, tutors.premium, (3959*acos(cos(radians('".$lat."'))*cos(radians(tutors.lat))*cos(radians(tutors.lon)-radians('".$lon."'))+sin(radians('".$lat."'))*sin(radians(tutors.lat)))) AS distance 
FROM tutors 
INNER JOIN subCatToTutor ON tutors.tutorId=subCatToTutor.tutorId 
INNER JOIN users ON tutors.userId=users.userId
WHERE  subCatToTutor.subCategory='".$subcat."' 
HAVING distance<'".$radius."'
ORDER BY distance LIMIT 0 , 20";

//echo "<h1>Query=" . $sqlQuery . "</h1>";

$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

while($row=mysql_fetch_assoc($result))
  $output[]=$row;
print(json_encode($output));

mysql_close();
?>