#include <mysql/my_global.h>
#include <mysql/mysql.h>
#include "dbFilterPort.h"

void dbFilterPort(int id, char *host, char *user, char *pass)
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

  mysql_query(con, "SELECT * FROM ports WHERE `id` = $%d", id);

  mysql_close(con);
}
~                                                                                                               
"dbPortInput.c" 31L, 914C                                                                     24,3          All

