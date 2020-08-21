<?php

require_once 'db_functions.php';

$response = array("error" => FALSE);

if (isset ($_POST['user_email']) && isset ($_POST['user_password'])) {
    
	$user_email=$_POST['user_email'];
	$user_password=$_POST['user_password'];
	$user_profile_type=$_POST['user_profile_type'];

	$user_details_exists =login_details($user_email,$user_password);
    if(count( $user_details_exists )>0)
	{
		$response["error"] = FALSE;
		$response['auto_number']=$user_details_exists['auto_number'];
 		$response['user_name']=$user_details_exists['u_name'];
 		$response['user_email']=$user_details_exists['u_email'];
 		$response['user_profile_type']=$user_details_exists['u_profile_type'];
    	$response["msg"] = "User details exists..!!";
    	echo json_encode($response);
	}
	else
	{
	    $response["error"] = TRUE;
	    $response["msg"] = "User Not Exists..!!";
		echo json_encode($response);
	}
	

}

?>