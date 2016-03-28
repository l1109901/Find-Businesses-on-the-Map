<?php
	$con=mysqli_connect("localhost","root","qwerty","db1");

    $tcno=$_POST["tc_no"];
	$firma_adi=$_POST["firma_adi"];
	$lat=$_POST["latitude"];
	$lon=$_POST["longtitude"];

    $statement=mysqli_prepare($con,"INSERT INTO firma (tc_no,firma_adi,latitude,longtitude) VALUES (?,?,?,?)");
	mysqli_stmt_bind_param($statement,"isdd",$tcno,$firma_adi,$lat,$lon);
	mysqli_stmt_execute($statement);

	mysqli_stmt_close($statement);

	mysqli_close($con);
?>