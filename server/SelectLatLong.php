<?php
	$con=mysqli_connect("localhost","root","qwerty","db1");
	$statement=mysqli_prepare($con,"SELECT firma_adi,latitude,longtitude FROM firma");
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement,$firma_adi,$lat,$lon);
	$firma=array();
	while(mysqli_stmt_fetch($statement)){
		$firma[firma_adi]=$firma_adi;
		$firma[lat]=$lat;
		$firma[lon]=$lon;
	}
	echo json_encode($firma);
	mysqli_stmt_close($statement);
	mysqli_close($con);
?>