<?php
require_once 'dbinfo.php';

$email=$_POST['email'];
//$email="sean.crane@mavs.uta.edu";

$dbConnection = mysql_connect("localhost", $dbUser, $dbPass) or die ('cannot connect to SQL databse');
mysql_select_db($dbName);

if (isset($_FILES['image']) && $_FILES['image']['size'] > 0) {

// Temporary file name stored on the server
$tmpName = $_FILES['image']['tmp_name'];

// Read the file
$fp = fopen($tmpName, 'r');
$data = fread($fp, filesize($tmpName));
$data = addslashes($data);
fclose($fp);


// Create the query and insert
// into our database.
$query = "UPDATE users SET picture='$data' ";
$query .= "WHERE emailAddress='$email'";
$results = mysql_query($query, $dbConnection);

// Print results
print "Thank you, your file has been uploaded.";

}
else {
print "No image selected/uploaded";
}

mysql_close();
?>