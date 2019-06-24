<?php
  require_once 'database.php';
  
  function display_arr($arr)
  {
    foreach ($arr as $var)
    {
      $var->display();
    }    
  }
  $var = User::all();
  
  display_arr($var);
  
  echo "<br>";
  
  $var = Note::all();
  
  display_arr($var); 
  
  echo "<hr>";
  
/*$var = Test::get(5);
$var->display();
$var->set_name('12345');
$var->save();
$var->display();*/

?>
<hr>
 <form action="hash_list.php" method="post">
  API Link: <input type="text" name="api_link"><br>
  <input type="submit" value="Submit">
</form> 

