<?php

require_once 'db_functions.php';

$response = array("error" => FALSE);

if (isset ($_POST['user_email'])) {
    
	$user_email=$_POST['user_email'];
	$verify =verify_user($user_email);
    if($verify==1)
	{
		$response["error"] = FALSE;
    	$response["msg"] = "Verified Successfully.!";
    	echo json_encode($response);
	}
	else if($verify==2)
	{
	    $response["error"] = TRUE;
    	$response["msg"] = "Already Verified. Please Signin";
    	echo json_encode($response);
	}
	else
	{
	    $response["error"] = TRUE;
	    $response["msg"] = "Error Occured.!";
		echo json_encode($response);
	}
	

}

?>