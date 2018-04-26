#include <mysql/my_global.h>
#include <mysql/mysql.h>
#include <string.h>


void dbCompInput(char *ip, char *network, char *host, char *user, char *pass)
{
  printf("===============Inputting computer to database...===============\n");
  MYSQL *con = mysql_init(NULL);
  char *query = "";
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
  query = "CREATE DATABASE IF NOT EXISTS portscan";
  mysql_real_query(con, query, strlen(query));

  query = "USE portscan";
  mysql_real_query(con, query, strlen(query));

  query = "CREATE TABLE IF NOT EXISTS computers(id INT unsigned AUTO_INCREMENT, ip VARCHAR(15), network VARCHAR(20), PRIMARY KEY (id))";
  mysql_real_query(con, query, strlen(query));

  query = "CREATE TABLE IF NOT EXISTS port(id INT(20), port INT(7), status INT(1), expected_status INT(1))";
  mysql_real_query(con, query, strlen(query));

  sprintf(buffer, "INSERT INTO computers VALUES ('0', '%s', '%s')", ip, network);
  query = buffer;
  mysql_real_query(con, query, strlen(query));

  printf("===============Database updated.===============\n");
  mysql_close(con);
}

int main(int argc, char *argv[])
{
  if (argc != 3)
  {
     printf("usage: %s <ip> <network>\n", argv[0]);
     exit(1);
  }
  char *ip = argv[1];
  char *network = argv[2];
  char *host = "localhost";
  char *user = "root";
  char *pass = "pass1";

  dbCompInput(ip, network, host, user, pass);
  return 0;
}
