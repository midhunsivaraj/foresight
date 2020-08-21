<?php
include("rtf-html-php.php");


  $applicationId = 'testocr09 ';
  $password = 'DBLHPvqlFoCIicZwawiuDGS0';
  //$fileName = '1.jpg';
  $fileName =$_FILES["file"]["name"];
  $serviceUrl = 'http://cloud.ocrsdk.com';
  $destFile = "uploads/".$_FILES['file']['name'];
  move_uploaded_file($_FILES['file']['tmp_name'], $destFile );
  
  
  $local_directory=dirname(__FILE__).'/';
 // $filePath = $local_directory.'/'.$fileName;
    $filePath =dirname(__FILE__).'/'.$destFile;
  if(!file_exists($filePath))
  {
    die('File '.$filePath.' not found.');
  }
  if(!is_readable($filePath) )
  {
     die('Access to file '.$filePath.' denied.');
  }

 
  //$url = $serviceUrl.'/processImage?language=english&exportFormat=rtf';
  $url=$serviceUrl.'/processImage?language=English&profile=DocumentConversion&imageSource=Auto&exportFormat=rtf';
  
 
  $curlHandle = curl_init();
  curl_setopt($curlHandle, CURLOPT_URL, $url);
  curl_setopt($curlHandle, CURLOPT_RETURNTRANSFER, 1);
  curl_setopt($curlHandle, CURLOPT_USERPWD, "$applicationId:$password");
  curl_setopt($curlHandle, CURLOPT_POST, 1);
  curl_setopt($curlHandle, CURLOPT_USERAGENT, "PHP Cloud OCR SDK Sample");
  curl_setopt($curlHandle, CURLOPT_FAILONERROR, true);
  $post_array = array();
  if((version_compare(PHP_VERSION, '5.5') >= 0)) {
    $post_array["my_file"] = new CURLFile($filePath);
  } else {
    $post_array["my_file"] = "@".$filePath;
  }
  curl_setopt($curlHandle, CURLOPT_POSTFIELDS, $post_array); 
  $response = curl_exec($curlHandle);
  if($response == FALSE) {
    $errorText = curl_error($curlHandle);
    curl_close($curlHandle);
    die($errorText);
  }
  $httpCode = curl_getinfo($curlHandle, CURLINFO_HTTP_CODE);
  curl_close($curlHandle);

 
  $xml = simplexml_load_string($response);
  if($httpCode != 200) {
    if(property_exists($xml, "message")) {
       die($xml->message);
    }
    die("unexpected response ".$response);
  }

  $arr = $xml->task[0]->attributes();
  $taskStatus = $arr["status"];
  if($taskStatus != "Queued") {
    die("Unexpected task status ".$taskStatus);
  }
  
 
  $taskid = $arr["id"];  
  
  

  $url = $serviceUrl.'/getTaskStatus';
 
  if(empty($taskid) || (strpos($taskid, "00000000-0") !== false)) {
    die("Invalid task id used when preparing getTaskStatus request");
  }
  $qry_str = "?taskid=$taskid";

  
  while(true)
  {
    sleep(5);
    $curlHandle = curl_init();
    curl_setopt($curlHandle, CURLOPT_URL, $url.$qry_str);
    curl_setopt($curlHandle, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($curlHandle, CURLOPT_USERPWD, "$applicationId:$password");
    curl_setopt($curlHandle, CURLOPT_USERAGENT, "PHP Cloud OCR SDK Sample");
    curl_setopt($curlHandle, CURLOPT_FAILONERROR, true);
    $response = curl_exec($curlHandle);
    $httpCode = curl_getinfo($curlHandle, CURLINFO_HTTP_CODE);
    curl_close($curlHandle);
  
    // parse xml
    $xml = simplexml_load_string($response);
    if($httpCode != 200) {
      if(property_exists($xml, "message")) {
        die($xml->message);
      }
      die("Unexpected response ".$response);
    }
    $arr = $xml->task[0]->attributes();
    $taskStatus = $arr["status"];
    if($taskStatus == "Queued" || $taskStatus == "InProgress") {
      // continue waiting
      continue;
    }
    if($taskStatus == "Completed") {
      // exit this loop and proceed to handling the result
      break;
    }
    if($taskStatus == "ProcessingFailed") {
      die("Task processing failed: ".$arr["error"]);
    }
    die("Unexpected task status ".$taskStatus);
  }

  

  $url = $arr["resultUrl"];   
  $curlHandle = curl_init();
  curl_setopt($curlHandle, CURLOPT_URL, $url);
  curl_setopt($curlHandle, CURLOPT_RETURNTRANSFER, 1);
  curl_setopt($curlHandle, CURLOPT_SSL_VERIFYPEER, false);
  $response = curl_exec($curlHandle);
  curl_close($curlHandle);
 
  // Let user donwload rtf result
  //header('Content-type: application/rtf');
  //header('Content-Disposition: attachment; filename="file.rtf"');

$reader = new RtfReader();
$rtf = $response; // or use a string
$reader->Parse($rtf);
$formatter = new RtfHtml();
echo $formatter->Format($reader->root);
 


?>