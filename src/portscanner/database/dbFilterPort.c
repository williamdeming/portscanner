#include <mysql/my_global.h>
#include <mysql/mysql.h>
#include <dbFilterPort.h>

void dbFilterPort(int id, char *host, char *user, char *pass)
{
  printf("===============Filtering port by id...===============");
  MYSQL *con = mysql_init(NULL);
  char buffer[100];

  if (con == NULL)
  {
    printf("%s\n", mysql_error(con));
    printf("==============================\n");
    exit(1);
  }

  if (mysql_real_connect(con, host, user, pass, NULL, 0, NULL, 0) == NULL)
  {
    printf("%s\n", mysql_error(con));
    mysql_close(con);
    printf("==============================\n");
    exit(1);
  }

  mysql_query(con, "CREATE DATABASE IF NOT EXISTS portscan;");

  mysql_query(con, "USE portscan");

  sprintf(buffer, "SELECT * FROM ports WHERE `id` = $%d", id);
  mysql_query(con, buffer);

  printf("===============Ports Filtered===============");
  mysql_close(con);
}
