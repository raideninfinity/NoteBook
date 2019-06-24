<?php

  require_once 'database.php';

  $api_link = $_POST["api_link"];
  $response["result"] = "";
  $response["hash_list"] = [];
  
  $user = User::find("api_link = '$api_link'");
  if (!isset($user))
  {
    $response["result"] = "NOT_EXIST";
  }
  else
  {
    $id = $user->get_id();
    $list = Note::where("user_id = $id");
    foreach($list as $item)
    {
      $hash = array("hash_id" => $item->get_hash_id(), "edit_date" => $item->get_edit_date());
      array_push($response["hash_list"], $hash);
    }
    $response["result"] = "SUCCESS";
  }
    
  echo (json_encode($response));

?>