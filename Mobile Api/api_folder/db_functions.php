<?php
$con=mysqli_connect("localhost","foresight_db","foresight_db","foresight_db");

 function user_details($user_name,$user_email,$user_password,$user_profile_type,$user_verification_number)
    {
    global $con;
    $insert_details=mysqli_query($con,"INSERT INTO userdetails_tb(u_name,u_email,u_password,u_profile_type,u_verification_number,u_status) VALUES('".$user_name."','".$user_email."','".md5($user_password)."','".$user_profile_type."','".$user_verification_number."','0')");
    if($insert_details>0)
    {
        $to = $user_email; // this is your Email address
        $from = 'noreply@foresight.com'; // this is the sender's Email address
        $subject = "Verification Code";
        $message ="Dear ". $user_name .','. "\n\n" . "Verification code to activate Foresight mobile is ". "\n\n".$user_verification_number . "\n\n"." Team Foresight";
    
        $headers = "From:" . $from;
        mail($to,$subject,$message,$headers);
        return 1;
    }
    else
    {
        return 0;
    }
    }
    
    function if_email_exists($email)
    {
        global $con;
        $select_email=mysqli_query($con,"select * from userdetails_tb where u_email='".$email."'");
        $count=mysqli_num_rows($select_email);
        if($count>0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    
    function login_details($user_email,$user_password)
    {
       global $con;
       $empty=array();
       $select_email=mysqli_query($con,"select * from userdetails_tb where u_email='".$user_email."' and u_password='".md5($user_password)."' and u_status='1'");
       $count=mysqli_num_rows($select_email);
       if($count>0)
       {
            $row = mysqli_fetch_assoc($select_email);
            return $row;
       }
       else
       {
          return $empty; 
       }
       
    }
    
    function verify_user($email)
    {
       global $con;
       $select_user=mysqli_query($con,"Select * from userdetails_tb where u_email='".$email."' and u_status='1'");
       $count_row=mysqli_num_rows($select_user);
       
       if($count_row==1)
       {
           return 2;
       }
       else
       {
           $upate_user=mysqli_query($con,"update userdetails_tb set u_status='1' where u_email='".$email."'");
           if($upate_user>0)
           {
               return 1;
           }
           else
           {
               return 0;
           }
       }
    }
?>