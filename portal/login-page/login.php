<?php
require_once('uphpCAS.php');
session_start();

try {
	$cas = new uphpCAS($_SESSION['cas']);
	if(isset($_SESSION['cafile'])) {
		$cas->setCaFile($_SESSION['cafile']);
	}
	$user = $cas->authenticate();
	
	header('Location: index.php');
} catch(Exception $e) {
	echo 'Jasig authentication failed: '.$e->getMessage();
	die();
}
