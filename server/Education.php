<?php
	$con=mysqli_connect("localhost","root","qwerty","db1");

    $user_id=$_POST["tc_no"];
	$okul_adi=$_POST["okul_adi"];
	$bolum=$_POST["bolum"];
	$mezuniyet_yili=$_POST["mezuniyet_yili"];

	$statement=mysqli_prepare($con,"INSERT INTO education (tc_no,okul_adi,dolum,mezuniyet_yili) VALUES (?,?,?,?)");
	mysqli_stmt_bind_param($statement,"issi",$tc_no,$okul_adi,$bolum,$mezuniyet_yili);
	mysqli_stmt_execute($statement);

	mysqli_stmt_close($statement);

	mysqli_close($con);
?>