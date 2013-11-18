<?php
echo "trying to mail";
//$name=$_POST['name'];
$name="Jonathan";
//$from=$_POST['from'];
$from="sean.crane@mavs.uta.edu";
//$to=$_POST['to'];
$to="jonathan.eason@mavs.uta.edu";
//$body=$_POST['body'];
$body="MIke check one two!!";
echo "trying to mail0";
require("class.phpmailer.php");
echo "trying to mail00";
$mail = new PHPMailer();
//$mail->IsSMTP();
echo "trying to mail1";
$mail->SMTPAuth = true;
$mail->Host = "mail.uta.edu";
$mail->Port = 26;
echo "trying to mail2";
$mail->Username = "jwe0053";
$mail->Password = "1337Pazwerd1!!";
$mail->SetFrom($from, 'Web App');
echo "trying to mail3";
$mail->Subject = "New MyTutor Message";
$mail->MsgHTML($body);
$mail->AddAddress($to, $name);
echo "trying to mail4";
if($mail->Send()) {
  echo "Message sent!";
} else {
  echo "Mailer Error: " . $mail->ErrorInfo;
}

?>