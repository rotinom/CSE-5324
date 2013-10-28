<?php
require_once 'dbinfo.php';

//save off user request info
//$radius=$_POST['radius'];
//$lat=$_POST['lat'];
//$lon=$_POST['lon'];

$radius="12";
$lat="32.821200";
$lon="-97.1036395";

echo "<h1>radius=" . $radius . "</h1>";
echo "<h1>lat=" . $lat . "</h1>";
echo "<h1>lon=" . $lon . "</h1>";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery ="SELECT *, ( 3959 * acos( cos( radians('".$lat."') ) * cos( radians( lat ) ) * cos( radians( lon ) - radians('".$lon."') ) + sin( radians('".$lat."') ) * sin( radians( lat ) ) ) ) AS distance FROM users HAVING distance < '".$radius."' ORDER BY distance LIMIT 0 , 20";

echo "<h1>Query=" . $sqlQuery . "</h1>";

$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

while($row=mysql_fetch_assoc($result))
  $output[]=$row;
print(json_encode($output));

mysql_close();
?>