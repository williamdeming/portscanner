#include <mysql/my_global.h>
#include <mysql/mysql.h>
#include <string.h>


void dbFilterPort(int id, char *host, char *user, char *pass)
{
  printf("===============Filtering port by id...===============\n");
  MYSQL *con = mysql_init(NULL);
  char *query = "";
  char buffer[100];
  int i;

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
  
  query = "USE portscan";
  mysql_real_query(con, query, strlen(query));

  sprintf(buffer, "SELECT * FROM port WHERE id = %d", id);
  query = buffer;
  mysql_real_query(con, query, strlen(query));

  MYSQL_RES *result = mysql_store_result(con);//gets table from database
  int num_fields = mysql_num_fields(result);
  MYSQL_ROW row;
  printf("|id |port|st |est|\n");
               
  while ((row = mysql_fetch_row(result)))//loops through table data
  {
     for(i = 0; i < num_fields; i++)
     {
       printf("| %s ", row[i] ? row[i] : "NULL");
     } 
     printf("|\n");
  }

  printf("===============Ports Filtered===============\n");
  mysql_free_result(result);
  mysql_close(con);
}

int main(int argc, char *argv[])
{
  if (argc != 2)
  {
     printf("usage: %s <id>\n", argv[0]);
     exit(1);
  }
  char *a;
  int id = strtol(argv[1], &a, 10);
  char *host = "localhost";
  char *user = "root";
  char *pass = "pass1";

  dbFilterPort(id, host, user, pass);
  return 0;
}
