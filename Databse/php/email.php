<?php
require_once('class.phpmailer.php');
$fromName=$_POST['fromName'];
//$fromName="Sean Crane";
$from=$_POST['fromAddress'];
//$from="sean.crane@mavs.uta.edu";
$toName=$_POST['toName'];
//$toName="Jonathan Eason";
$to=$_POST['toAddress'];
//$to="jonathan.eason@mavs.uta.edu";
$body=$_POST['body'];
//$body="MIke check one two!!";

$mail = new PHPMailer();
$mail->AddAddress($to, $name);
$mail->Subject = "Email from MyTutor";
$mail->From = $from;
$mail->FromName = $fromName;
$mail->Body = $body;

if($mail->Send()) {
  echo "Message sent!";
} else {
  echo "Mailer Error: " . $mail->ErrorInfo;
}

?>
