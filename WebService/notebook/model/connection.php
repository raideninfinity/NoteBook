<?php
class Connection
{
  const DB_HOST = 'localhost';
  const DB_USER = 'root';
  const DB_PASSWORD = 'test';
  const DB_NAME = 'notebook';

  private $mysqli = null;
  
  public function connect()
  {
    $this->mysqli = new mysqli(self::DB_HOST, self::DB_USER, self::DB_PASSWORD, self::DB_NAME);
    if ($this->mysqli->connect_errno)
    {
      echo $this->mysqli->connect_errno;
      echo $this->mysqli->connect_error;
      return false;
    }
    else
    {
      return true;
    }
  }

  public function disconnect()
  {
    if ($this->mysqli != null)
    {
      $this->mysqli->close();
    }
  }

  public function query($str)
  {
    if ($result = $this->mysqli->query($str))
    {
      return $result->fetch_all(MYSQLI_ASSOC);
    }
    else
    {
      echo $this->mysqli->connect_errno;
      echo $this->mysqli->error;      
      return array();
    }
  }

  public function execute($str)
  {
    if ($result = $this->mysqli->query($str))
    {
      return true;
    }
    else
    {
      echo $this->mysqli->connect_errno;
      echo $this->mysqli->error;      
      return false;
    }
  }
  
  public function get_insert_id()
  {
    return $this->mysqli->insert_id;
  }
  
  public function __construct()
  {
    $this->connect();
  }
  
  function __destruct() 
  {
    $this->disconnect();
  }
  
}

$GLOBALS['connection'] = new Connection();
?>