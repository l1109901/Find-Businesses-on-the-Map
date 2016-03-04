<?php
	$con=mysqli_connect("localhost","root","qwerty","db1");

	$ad=$_POST["ad"];
	$soyad=$_POST["soyad"];
	$dogum_yil=$_POST["dogum_yil"];
	$email=$_POST["email"];
	$tel=$_POST["tel"];
	$kim=$_POST["kim"];//işveren mi? yoksa iş arayan?(1 ise iş arayan, 2 ise işveren)
	$kullanici_adi=$_POST["kullanici_adi"];
	$sifre=$_POST["sifre"];

	$statement=mysqli_prepare($con,"INSERT INTO user (ad,soyad,dogum_yil,email,tel,kim,kullanici_adi,sifre) VALUES (?,?,?,?,?,?,?,?)");
	mysqli_stmt_bind_param($statement,"ssisiiss",$ad,$soyad,$dogum_yil,$email,$tel,$kim,$kullanici_adi,$sifre);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_close($statement);
	
	mysqli_close($con);
?>