<?php
require_once 'dbinfo.php';

$email=$_POST['email'];

//$email="jonathan.eason@mavs.uta.edu";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

$sqlQuery = "SELECT userId FROM users WHERE emailAddress='$email'";

$result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());

$row = mysql_fetch_row($result);
$id = $row[0];

if(!is_null($id))
{
  $sqlQuery = "SELECT tutorId FROM tutors WHERE userId='$id'";
  $result = mysql_query($sqlQuery) or die("SQL Query Failed: " . mysql_error());
  $row = mysql_fetch_row($result);
  $tutorId = $row[0];

  if(!is_null($id))
  {
    // Create the query and insert
    // into our database.
    $query = "SELECT * FROM subCatToTutor WHERE tutorId='$tutorId'";
    $results = mysql_query($query, $dbConnection);
    while($row=mysql_fetch_assoc($result))
      $output[]=$row;
    print(json_encode($output));
  }
  else
  {
    echo(0);
  }
}
else
{
  echo(0);
}

mysql_close();
?>