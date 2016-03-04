<?php
	$con=mysqli_connect("localhost","root","qwerty","db1");

	
	$kullanici_adi=$_POST["kullanici_adi"];
	$sifre=$_POST["sifre"];
	
	$statement=mysqli_prepare($con,"SELECT * FROM user WHERE kullanici_adi=? AND sifre=?");
	
	mysqli_stmt_bind_param($statement,"ss",$kullanici_adi,$sifre);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement,$user_id,$ad,$soyad,$dogum_yil,$email,$tel,$kim,$kullanici_adi,$sifre);
	
	$user=array();
	
	while(mysqli_stmt_fetch($statement)){
		$user[ad]=$ad;
		$user[soyad]=$soyad;
		$user[dogum_yil]=$dogum_yil;
		$user[email]=$email;
		$user[tel]=$email;
		$user[kim]=$kim;
	}
	
	echo json_encode($user);
	mysqli_stmt_close($statement);
	
	mysqli_close($con);
?>