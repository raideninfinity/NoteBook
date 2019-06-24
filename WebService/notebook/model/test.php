<?php

require_once "model.php";

class Test extends Model
{
  const TABLE_NAME = 'test_table';
  protected $form = ["id" => "int", "name" => "string"];
  
  public function __construct($name = '')
  {
    $this->data = ["id" => 0, "name" => ""];
    $this->set_name($name);
  }

  public function set_name($name)
  {
    $this->data['name'] = $name;
  }

  public function get_name()
  {
    return $this->data['name'];
  }
 
  public function set_data($source)
  {
    $this->data['id'] = intval($source['id']);
    $this->data['name'] = $source['name'];
  }
  
}

?>