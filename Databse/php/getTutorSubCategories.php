<?php
require_once 'dbinfo.php';

$email=$_POST['email'];

//$email="jonathan.eason@mavs.uta.edu";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery ="SELECT subCatToTutor.subCategory FROM tutors
INNER JOIN subCatToTutor ON tutors.tutorId=subCatToTutor.tutorId 
INNER JOIN users ON tutors.userId=users.userId
WHERE  users.emailAddress='$email'";

$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

while($row=mysql_fetch_assoc($result))
{
  $output[]=$row;
}
print(json_encode($output));

mysql_close();
?>