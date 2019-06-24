<?php

require_once "model.php";

class Note extends Model
{
  const TABLE_NAME = 'notes';
  protected $form = ["id" => "int", "user_id" => "int", "content" => "string", "create_date" => "string", "edit_date" => "string", "hash_id" => "string"];
  
  public function __construct()
  {
    $this->data = ["id" => 0, "user_id" => 0, "content" => "", "create_date" => "", "edit_date" => "", "hash_id" => ""];
  }

  public function set_user_id($user_id)
  {
    $this->data['user_id'] = $user_id;
  }

  public function get_user_id()
  {
    return $this->data['user_id'];
  }
 
  public function set_content($content)
  {
    $this->data['content'] = $content;
  }

  public function get_content()
  {
    return $this->data['content'];
  }
  
  public function set_create_date($create_date)
  {
    $this->data['create_date'] = $create_date;
  }

  public function get_create_date()
  {
    return $this->data['create_date'];
  }

  public function set_edit_date($edit_date)
  {
    $this->data['edit_date'] = $edit_date;
  }

  public function get_edit_date()
  {
    return $this->data['edit_date'];
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
    $this->data['content'] = $source['content'];
    $this->data['create_date'] = $source['create_date'];
    $this->data['edit_date'] = $source['edit_date'];
    $this->data['hash_id'] = $source['hash_id'];
  }
  
}

?>