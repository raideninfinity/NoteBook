<?php

  function genRandomString($len = 72) 
  {
      $chara = '0123456789abcdefghijklmnopqrstuvwxyz';
      $str = '';    
      for ($p = 0; $p < $len; $p++) 
      {
          $str .= $chara[random_int(0, 16)];
      }
      return $str;
  }

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
    if (isset($user))
    {
      $response["result"] = "USER_EXIST";
    }
    else
    {
      $api_link = "".md5($email.$password.genRandomString());
      $user = new User($email, $password, $api_link);
      $user->save();
      $response["api_link"] = $user->get_api_link();
      $response["result"] = "REG_SUCCESS";
    }
  }
  echo (json_encode($response));

?>