#include <mysql/my_global.h>
#include <mysql/mysql.h>
#include <dbCompInput.h>

void dbCompInput(char *ip, char *network, char *host, char *user, char *pass)
{
  printf("===============Inputting computer to database...===============");
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

  mysql_query(con, "CREATE DATABASE IF NOT EXIST portscan");

  mysql_query(con, "USE portscan");

  mysql_query(con, "CREATE TABLE IF NOT EXISTS computers(id INT unsigned AUTO_INCREMENT, ip INT(15), network VARCHAR(20), PRIMARY KEY (id)");

  mysql_query(con, "CREATE TABLE IF NOT EXISTS ports(id INT(20), port INT(7), status INT(1), expected_status INT(1)");

  sprintf(buffer, "INSERT INTO computers(%s, %s)", ip, network);
  mysql_query(con, buffer);

  printf("===============Database updated.===============");
  mysql_close(con);
}
