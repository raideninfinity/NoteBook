<?php

require_once "model.php";

class DeletedNote extends Model
{
  const TABLE_NAME = 'deleted_notes';
  protected $form = ["id" => "int", "user_id" => "int", "hash_id" => "string"];
  
  public function __construct()
  {
    $this->data = ["id" => 0, "user_id" => 0, "hash_id" => ""];
  }

  public function set_user_id($user_id)
  {
    $this->data['user_id'] = $user_id;
  }

  public function get_user_id()
  {
    return $this->data['user_id'];
  }
 
  public function set_hash_id($hash_id)
  {
    $this->data['hash_id'] = $hash_id;
  }

  public function get_hash_id()
  {
    return $this->data['hash_id'];
  }
  
  public function set_data($source)
  {
    $this->data['id'] = intval($source['id']);
    $this->data['user_id'] = intval($source['user_id']);
    $this->data['hash_id'] = $source['hash_id'];
  }
  
}

?>