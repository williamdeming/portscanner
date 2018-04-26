#include <mysql/my_global.h>
#include <mysql/mysql.h>
#include <dbPortInput.h>
#include <string.h>

void dbPortInput(int id, int port, int status, int expected_status, char *host, char *user, char *pass)
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

  sprintf(buffer, "INSERT INTO port VALUES ('%d', '%d', '%d', '%d')", id, port, status, expected_status);
  query = buffer;
  mysql_real_query(con, query, strlen(query));
  printf("===============Database updated.===============\n");

  mysql_close(con);
}
int main()
{
  int id = 1;
  int id2 = 2;
  int port = 22;
  int port2 = 33;
  int port3 = 44;
  int port4 = 11;
  int port5 = 55;
  int port6 = 66;
  int status = 0;
  int status2 = 1;
  int expected_status = 0;
  int expected_status2 = 1;
  char *host = "localhost";
  char *user = "root";
  char *pass = "pass1";

  dbPortInput(id, port, status, expected_status, host, user, pass); 
  dbPortInput(id, port2, status, expected_status, host, user, pass);
  dbPortInput(id, port3, status2, expected_status2, host, user, pass); 
  dbPortInput(id, port4, status, expected_status2, host, user, pass);
  dbPortInput(id2, port5, status, expected_status, host, user, pass); 
  dbPortInput(id2, port6, status, expected_status2, host, user, pass); 
  return 0;
}
