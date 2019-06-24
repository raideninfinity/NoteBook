<?php

require_once "model.php";

class User extends Model
{
  const TABLE_NAME = 'users';
  protected $form = ["id" => "int", "email" => "string", "password" => "string", "api_link" => "string"];
  
  public function __construct($email = '', $password = '', $api_link = '')
  {
    $this->data = ["id" => 0, "email" => "", "password" => "", "api_link" => ""];
    $this->set_email($email);
    $this->set_password($password);
    $this->set_api_link($api_link);
  }

  public function set_email($email)
  {
    $this->data['email'] = $email;
  }

  public function get_email()
  {
    return $this->data['email'];
  }
  
  public function set_password($password)
  {
    $this->data['password'] = $password;
  }

  public function get_password()
  {
    return $this->data['password'];
  }
 
  public function set_api_link($api_link)
  {
    $this->data['api_link'] = $api_link;
  }

  public function get_api_link()
  {
    return $this->data['api_link'];
  }
  
  public function set_data($source)
  {
    $this->data['id'] = intval($source['id']);
    $this->data['email'] = $source['email'];
    $this->data['password'] = $source['password'];
    $this->data['api_link'] = $source['api_link'];
  }
  
}

?>