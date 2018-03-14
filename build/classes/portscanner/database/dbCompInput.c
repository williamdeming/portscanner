#include <mysql/my_global.h>
#include <mysql/mysql.h>
#include "dbCompInput.h"

void dbCompInput(int ip, char *network, char *host, char *user, char *pass)
{
  MYSQL *con = mysql_init(NULL);

  if (con == NULL)
  {
    fprintf(stderr, "%s\n", mysql_error(con));
  }

  if (mysql_real_connect(con, host, user, pass, NULL, 0, NULL, 0) == NULL)
  {
    fprintf(strderr, "%s\n", mysql_error(con));
    mysql_close(con);
    exit(1);
  }

  mysql_query(con, "CREATE DATABASE IF NOT EXIST portscan");

  mysql_query(con, "USE portscan");

  mysql_query(con, "CREATE TABLE IF NOT EXISTS computers(id INT unsigned AUTO_INCREMENT, ip INT(15), network VARCHAR(20), PRIMARY KEY (id)");

  mysql_query(con, "CREATE TABLE IF NOT EXISTS ports(id INT(20), port INT(7), status INT(1), expected_status INT(1)");

  mysql_query(con, "INSERT INTO computers(%s, %d)", ip, network);

  mysql_close(con);
}
~                                                                                                               
"dbPortInput.c" 31L, 914C                                                                     1,1           All

