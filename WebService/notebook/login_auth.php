<?php

  require_once 'database.php';

  $email = $_POST["email"];
  $password = $_POST["password"];
  $response["api_link"] = "";
  
  if (empty($email) || empty($password))
  {
    $response["result"] = "INPUT_EMPTY";
  }
  else
  {
    $user = User::find("email = '$email'");
    if (!isset($user))
    {
      $response["result"] = "NOT_FOUND";
    }
    else if ($user->get_password() != $password)
    {
      //$response["1"] = $user->get_password();
      //$response["2"] = $password;
      $response["result"] = "AUTH_FAIL";
    }
    else
    {
      $response["api_link"] = $user->get_api_link();
      $response["result"] = "AUTH_SUCCESS";
    }
  }
  echo (json_encode($response));

?>