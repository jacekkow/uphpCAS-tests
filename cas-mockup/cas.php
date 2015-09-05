<?php
# Mockup of a CAS server

session_start();

switch($_SERVER['PATH_INFO']) {
	case '/login':
		if($_SERVER['REQUEST_METHOD'] == 'POST') {
			$ticket = NULL;
			if($_POST['user']) {
				$ticket = $_POST;
			}
			$_SESSION['ticket'] = json_encode($ticket);
		}
		
		if(isset($_SESSION['ticket'])) {
			$url = $_GET['service'];
			$ticket = $_SESSION['ticket'];
			
			if(!isset($_GET['method']) OR $_GET['method'] != 'POST') {
				if(strpos($url, '?') === FALSE) {
					$url .= '?';
				} else {
					$url .= '&';
				}
				$url .= 'ticket='.$ticket;
				
				header('Location: '.$url);
				die();
			} else {
				include('cas_login_post.php');
				die();
			}
		} elseif($_SERVER['REQUEST_METHOD'] == 'GET') {
			include('cas_login_form.php');
			die();
		}
		break;
	
	case '/logout':
		unset($_SESSION['ticket']);
		if(isset($_GET['service'])) {
			header('Location: '.$_GET['service']);
		}
		die('Logged out');
		break;
	
	case '/serviceValidate':
		if(!isset($_GET['ticket'])) {
			readfile('cas_failure_noticket.xml');
			die();
		}
		if(!isset($_GET['service'])) {
			readfile('cas_failure_noservice.xml');
			die();
		}
		
		$ticket = json_decode($_GET['ticket'], TRUE);
		if(!is_array($ticket)) {
			readfile('cas_failure_malformatted.xml');
			die();
		}
		if(!isset($ticket['user'])) {
			readfile('cas_failue_nouser.xml');
			die();
		}
		if(!isset($ticket['service']) || $_GET['service'] != $ticket['service']) {
			readfile('cas_failure_wrongservice.xml');
			die();
		}
		
		$dom = new DOMDocument('1.0', 'utf-8');
		$root = $dom->createElementNS('http://www.yale.edu/tp/cas', 'cas:serviceResponse');
		$response = $dom->createElement('cas:authenticationSuccess');
		$user = $dom->createElement('cas:user', $ticket['user']);
		// TODO: attributes
		$response->appendChild($user);
		$root->appendChild($response);
		$dom->appendChild($root);
		echo $dom->saveXML();
		die();
		
		break;
}

header('HTTP/1.1 404 Not Found');
die('Invalid method');
