<?php
session_start();
$_SESSION['cas'] = $_GET['cas'];

echo 'CAS server set.'."\n";

if(isset($_GET['cafile'])) {
	$_SESSION['cafile'] = $_GET['cafile'];
	echo 'CA file set.'."\n";
} else {
	unset($_SESSION['cafile']);
	echo 'CA file unset.'."\n";
}

if(isset($_GET['method'])) {
	$_SESSION['method'] = $_GET['method'];
	echo 'Method set.'."\n";
} else {
	unset($_SESSION['method']);
	echo 'Method unset.'."\n";
}

if(isset($_GET['url'])) {
	$_SESSION['url'] = $_GET['url'];
	echo 'URL set.'."\n";
} else {
	unset($_SESSION['url']);
	echo 'URL unset.'."\n";
}
