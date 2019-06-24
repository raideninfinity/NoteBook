<?php
require_once 'connection.php';

class Model
{
  const TABLE_NAME = '';
  protected $data;
  protected $form = ["id" => "int"];
  
  private function __construct()
  {
  }
 
  public function set_data($source)
  {    
  }
 
  private static function generate_array($result)
  {
    $array = array();
    foreach ($result as $entry)
    {
      $class = get_called_class();
      $var = new $class();
      $var->set_data($entry);
      array_push($array, $var);
    }
    return $array;    
  }
  
  public static function all()
  {
    $result = $GLOBALS['connection']->query("SELECT * FROM ".static::TABLE_NAME);
    return static::generate_array($result);
  }
  
  public static function where($cond = '*', $count = 0, $offset = 0)
  {
    $query = "SELECT * FROM ".static::TABLE_NAME." WHERE ".$cond;
    if ($count > 0)
    {
      $query.=" LIMIT ".(string)$count." OFFSET ".(string)$offset;
    }
    $result = $GLOBALS['connection']->query($query);
    return static::generate_array($result);
  }
  
  public static function count_where($cond = '')
  {
    $query = "SELECT COUNT(ID) FROM ".static::TABLE_NAME;
    if ($cond != '')
    {
      $query .= " WHERE ".$cond;
    }
    $result = $GLOBALS['connection']->query($query);
    return intval($result[0]['COUNT(ID)']);
  }
  
  public static function find($cond = '*')
  {
    $query = "SELECT * FROM ".static::TABLE_NAME." WHERE ".$cond;
    $result = $GLOBALS['connection']->query($query);
    if (count($result) > 0)
    {
      $class = get_called_class();
      $var = new $class();
      $var->set_data($result[0]);
      return $var;
    }
    else
    {
      return null;
    }  
  }
  
  public static function get($id)
  {
    $query = "SELECT * FROM ".static::TABLE_NAME." WHERE id = ".$id;
    $result = $GLOBALS['connection']->query($query);
    if (count($result) > 0)
    {
      $class = get_called_class();
      $var = new $class();
      $var->set_data($result[0]);
      return $var;
    }
    else
    {
      return null;
    }  
  }
  
  public function set_id($id)
  {
    $this->data['id'] = $id;
  }
  
  public function get_id()
  {
    return $this->data['id'];
  }
  
  private function insert()
  {
    $query = "INSERT INTO ".static::TABLE_NAME." (";
    foreach($this->form as $key => $value)
    {
      if ($key == 'id'){ continue; }
      $query .= $key.',';
    }
    $query = substr($query, 0, -1);
    $query .= ") VALUES (";
    foreach($this->form as $key => $value)
    {
      if ($key == 'id'){ continue; }
      if ($value == 'string')
      {
        $query .= "'".$this->data[$key]."',";
      }
      else
      {
        $query .= $this->data[$key].',';
      }
    }
    $query = substr($query, 0, -1);    
    $query .= ")";   
    //echo $query;
    if ($GLOBALS['connection']->execute($query) == true)
    {
      $this->set_id($GLOBALS['connection']->get_insert_id());
    }
  }
  
  private function update()
  {
    $query = "UPDATE ".static::TABLE_NAME." SET ";
    foreach($this->form as $key => $value)
    {
      if ($key == 'id'){ continue; }
      $query .= $key.' = ';
      if ($value == 'string')
      {
        $query .= "'".$this->data[$key]."' ,";  
      }
      else
      {
        $query .= $this->data[$key].' ,';
      }
    }
    $query = substr($query, 0, -1);   
    $query .= ' WHERE id = '.(string)$this->get_id();
    $GLOBALS['connection']->execute($query); 
  }
  
  public function save()
  {
    if ($this->get_id() == 0)
    {
      $this->insert();
    }
    else
    {
      $this->update();
    }
  }
  
  public function erase()
  {
    $GLOBALS['connection']->execute("DELETE FROM ".static::TABLE_NAME." WHERE ID = ".$this->data["id"]);
    $this->set_id(0);
  }

  public function display()
  {
    echo json_encode($this->data);
    echo('<br>');
  }
  
}

?>







