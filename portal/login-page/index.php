<?php
require_once('uphpCAS.php');
session_start();

$cas = new uphpCAS($_SESSION['cas']);

if($cas->isAuthenticated()) {
	$user = $cas->authenticate();
	echo 'Authenticated as '.$user->user;
} else {
	echo 'Not authenticated. <a href="login.php">Log in</a>';
}
