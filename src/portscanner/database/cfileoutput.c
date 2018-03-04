#include <stdio.h>
#include <string.h>
#include "cfileoutput.h"

void cfileoutput(int id, string ports)
{
  FILE *file;
  file = fopen("portscanoutput", "w");
  
  fputs((string)id, file);
  fputs("\n", file);
  fputs(ports, file);
  fclose(file);
  
}
