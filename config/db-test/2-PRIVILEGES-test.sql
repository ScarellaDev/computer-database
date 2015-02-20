  #-----------------------------------
  #USER RIGHTS MANAGEMENT
  #-----------------------------------
  CREATE USER 'admincdbtest'@'localhost' IDENTIFIED BY 'qwerty1234';

  GRANT ALL PRIVILEGES ON `computer-database-test-db`.* TO 'admincdbtest'@'localhost' WITH GRANT OPTION;


  FLUSH PRIVILEGES;
