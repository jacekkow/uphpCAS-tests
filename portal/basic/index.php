<?php
require_once('uphpCAS.php');
session_start();

try {
	$cas = new uphpCAS($_SESSION['cas']);
	if(isset($_SESSION['cafile'])) {
		$cas->setCaFile($_SESSION['cafile']);
	}
	if(isset($_SESSION['method'])) {
		$cas->setMethod($_SESSION['method']);
	}
	if(isset($_SESSION['url'])) {
		$cas->setServiceUrl($_SESSION['url']);
	}
	$user = $cas->authenticate();
	
	echo 'Authenticated as '.$user->user;
} catch(Exception $e) {
	echo 'Jasig authentication failed: '.$e->getMessage();
	die();
}
