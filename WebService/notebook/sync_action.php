<?php

  require_once 'database.php';

  $api_link = $_POST["api_link"];
  $request_str = $_POST["request_str"];
  $response["result"] = "";
  $response["retrieve"] = [];
  $response["delete"] = [];
  
  $user = User::find("api_link = '$api_link'");
  if (!isset($user))
  {
    $response["result"] = "NOT_EXIST";
  }
  else
  {
    $obj = json_decode($request_str, true);
    #DELETE
    $del_list = $obj["delete"];
    foreach($del_list as $hash_id)
    {
      $note = Note::find("hash_id = '$hash_id'");
      if (isset($note))
      {        
        $deleted_note = new DeletedNote();
        $deleted_note->set_user_id($note->get_user_id());
        $deleted_note->set_hash_id($note->get_hash_id());
        $deleted_note->save();
        $note->erase();
      }
    }
    #RETRIEVE
    $ret_list = $obj["retrieve"];
    foreach($ret_list as $hash_id)
    {
      $note = Note::find("hash_id = '$hash_id'");
      if (isset($note))
      {        
        $item = array("hash_id" => $note->get_hash_id(), "content" => $note->get_content(), 
        "create_date" => $note->get_create_date(), "edit_date" => $note->get_edit_date());
        array_push($response["retrieve"], $item);
      }
    }
    #UPDATE
    $upd_list = $obj["update"];
    $response["result"] = "SUCCESS";
    foreach($upd_list as $data)
    {
      $hash_id = $data["hash_id"];
      $note = Note::find("hash_id = '$hash_id'");
      $del_note = DeletedNote::find("hash_id = '$hash_id'");
      if (isset($del_note) != true)
      {
        if (isset($note))
        {        
          $note->set_content($data["content"]);
          $note->set_edit_date($data["edit_date"]);
          $note->save();
        }
        else
        {
          $note = new Note();
          $note->set_user_id($user->get_id());
          $note->set_hash_id($data["hash_id"]);
          $note->set_content($data["content"]);
          $note->set_create_date($data["create_date"]);
          $note->set_edit_date($data["edit_date"]);
          $note->save();
        }
      }
    }

    #DeletedNote
    $id = $user->get_id();
    $list = DeletedNote::where("user_id = $id");
    foreach($list as $item)
    {
      array_push($response["delete"], array("hash_id" => $item->get_hash_id()));
    }
  }
    
  echo (json_encode($response));

?>

















