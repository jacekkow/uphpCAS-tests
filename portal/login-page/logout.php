<?php
require_once('uphpCAS.php');
session_start();

try {
	$cas = new uphpCAS($_SESSION['cas']);
	$user = $cas->logout();
} catch(Exception $e) {
	echo 'Jasig authentication failed: '.$e->getMessage();
	die();
}
