<?php

require_once 'db_functions.php';

$response = array("error" => FALSE);

if (isset($_POST['user_name']) && isset ($_POST['user_email']) && isset ($_POST['user_password']) && isset ($_POST['user_profile_type']) && isset($_POST['user_verification_number'])) {

	$user_name=$_POST['user_name'];
	$user_email=$_POST['user_email'];
	$user_password=$_POST['user_password'];
	$user_profile_type=$_POST['user_profile_type'];
	$user_verification_number=$_POST['user_verification_number'];
	
	$check_email=if_email_exists($user_email);
	if($check_email==0)
	{
	$user_details =user_details($user_name,$user_email,$user_password,$user_profile_type,$user_verification_number);
	if($user_details!=0)
	{
		$response["error"] = FALSE;
	    $response["msg"] = "User details added successfully.";
		echo json_encode($response);
	}
	else
	{
		$response["error"] = TRUE;
    	$response["msg"] = "Error in insert.";
    	echo json_encode($response);
	}
	}
	else
	{
	    $response["error"] = TRUE;
    	$response["msg"] = "Email already exists.Choose another one.";
    	echo json_encode($response);
	}
	
}

?>