#define _BSD_SOURCE 1
#include <stdio.h>
#include <unistd.h>
#include <time.h>
#include <libnet.h>
#include <pcap.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

/* This program runs a simple syn scan on a specified IP address */

/* Original authors: Justin Clarke, Nitesh Dhanjani */
/* Book: Network Security Tools, Section 11.4. Combining libnet and libpcap */
/* books.gigatux.nl/mirror/networksecuritytools/0596007949/toc.html */

//int answer = 0;            /* flag for scan timeout */
struct portInfo //holds number and open/closed/filter id
{
  int portNum;
  int id;
  int timeOut;
}portInfo;

struct portInfo storePort[65535]; //array of # of ports to store ports scanned
int portCount = 0;   //number of stored ports
pthread_mutex_t mutex1;

/* usage */
void
usage (char *name)
{
  printf ("Usage: %s -i ip_address -t type -p portMin:portMax\n", name);
  printf ("    -i    IP address to scan\n");
  printf ("    -t    0 for specific ports, 1 for range of ports\n");
  printf ("    -p    Port range to check\n");
  exit (1);
}

void
packet_handler (u_char * user, const struct pcap_pkthdr *header,
        const u_char * packet)
{
  struct libnet_tcp_hdr *tcp =
    (struct libnet_tcp_hdr *) (packet + LIBNET_IPV4_H + LIBNET_ETH_H);
  if (tcp->th_flags == 0x14)
    {
      pthread_mutex_lock(&mutex1); 
      //printf ("Port %d appears to be closed\n", ntohs (tcp->th_sport));
      storePort[portCount].portNum = ntohs (tcp->th_sport);
      storePort[portCount].id = 1;
      storePort[portCount].timeOut = 0;
//      portCount++;
      pthread_mutex_unlock(&mutex1); 
     // answer = 0;
    }
  else
    {
      if (tcp->th_flags == 0x12)
      {
        pthread_mutex_lock(&mutex1); 
        //printf ("Port %d appears to be open\n", ntohs (tcp->th_sport));
        storePort[portCount].portNum = ntohs(tcp->th_sport); 
        storePort[portCount].id = 2;
        storePort[portCount].timeOut = 0;
//        portCount++;
       // answer = 0;
        pthread_mutex_unlock(&mutex1); 
      }
    }
}

int portStringConvert(char *portInit, int *ports, int type)
{
    int size = strlen(portInit);
    char portA[size];//char array size of char *
    char *delim = ":";//break point in string
    char *token = "";
    char *a;//used only for strtol(), stores rest of string which is null in this case
    int i = 0; 
    int j;
    //before forloop
    for (j = 0; j < size+1; j++)//converts char * into char[]
    {
      portA[j] = portInit[j];
    }
    //after forloop
    token = strtok(portA, delim);//initial token
    while (token != NULL)//parses through token and inputs into port array
    {
      ports[i] = strtol(token, &a, 10);
      token = strtok(NULL, delim);    
      i++;
    }
    if(i != 2 && type == 1)
    {
      return 0;
    }

    return i;
}

typedef struct Bundle//struct to hold all values needed for threads
{
  int port;
  libnet_ptag_t tcp;
  libnet_t *l;
  libnet_ptag_t ipv4;
  u_int32_t myipaddr;
  in_addr_t ipaddr;
  time_t tv;
  pcap_t *handle;

} Bundle;
  
//Entry thread for func. Adds a tcp packet to each thread created
void *entry(void * arg)
{

  Bundle *bundle = (Bundle*) arg;
  /* build the TCP header */
  bundle->tcp = libnet_build_tcp (libnet_get_prand (LIBNET_PRu16),    	/* src port */
                  bundle->port,    					/* destination port */
                  libnet_get_prand (LIBNET_PRu16),    			/* sequence number */
                  0,    						/* acknowledgement */
                  TH_SYN,    						/* control flags */
                  7,    						/* window */
                  0,    				      		/* checksum - 0 = autofill */
                  0,    						/* urgent */
                  LIBNET_TCP_H,    					/* header length */
                  NULL,    						/* payload */
                  0,    						/* payload length */
                  bundle->l,    					/* libnet context */
                  bundle->tcp);    					/* protocol tag */

  if (bundle->tcp == -1)
  {
    fprintf (stderr,
           "Unable to build TCP header: %s\n", libnet_geterror (bundle->l));
    exit (1);
  }
  /* build the IP header */
  bundle->ipv4 = libnet_build_ipv4 (LIBNET_TCP_H + LIBNET_IPV4_H,    	/* length */
                0,    							/* TOS */
                libnet_get_prand (LIBNET_PRu16),    			/* IP ID */
                0,    							/* frag offset */
                127,    						/* TTL */
                IPPROTO_TCP,    					/* upper layer protocol */
                0,    							/* checksum, 0=autofill */
                bundle->myipaddr,    					/* src IP */
                bundle->ipaddr,    					/* dest IP */
                NULL,    						/* payload */
                0,    							/* payload len */
                bundle->l,    						/* libnet context */
                bundle->ipv4);    					/* protocol tag */

  if (bundle->ipv4 == -1)
  {
    fprintf (stderr,
           "Unable to build IPv4 header: %s\n", libnet_geterror (bundle->l));
    exit (1);
  }

  /* write the packet */
  if ((libnet_write (bundle->l)) == -1)
  {
    fprintf (stderr, "Unable to send packet: %s\n",
         libnet_geterror (bundle->l));
    exit (1);
  }
  /* set variables for flag/counter */
  pthread_mutex_lock(&mutex1);
  storePort[portCount].timeOut = 1;
  pthread_mutex_unlock(&mutex1);
 // answer = 1;
  bundle->tv = time (NULL);
  /* capture the reply */

  while (storePort[portCount].timeOut)
  { 
    pcap_dispatch (bundle->handle, -1, packet_handler, NULL);
    pthread_mutex_lock(&mutex1);
    if ((time (NULL) - bundle->tv) > 2)
    {
      storePort[portCount].timeOut = 0;
      //answer = 0;    /* timed out */
      //printf ("Port %d appears to be filtered\n", bundle->port);
      storePort[portCount].portNum = bundle->port;
      storePort[portCount].id = 3;
      portCount++;
    }
    pthread_mutex_unlock(&mutex1);
  }
  pthread_mutex_lock(&mutex1);
  if(storePort[portCount].id == 1 || storePort[portCount].id == 2)
  {
    portCount++;
  }
  pthread_mutex_unlock(&mutex1);
  return NULL;
}

int
main (int argc, char *argv[])
{
  char *device = NULL;        			/* device for sniffing/sending */
  char o;
  char *a;//for string tol            		/* for option processing */
  in_addr_t ipaddr;        			/* ip address to scan */
  u_int32_t myipaddr;        			/* ip address of this host */
  libnet_t *l;            			/* libnet context */
  libnet_ptag_t tcp = 0, ipv4 = 0;    		/* libnet protocol blocks */
  char libnet_errbuf[LIBNET_ERRBUF_SIZE];    	/* libnet error messages */
  char libpcap_errbuf[PCAP_ERRBUF_SIZE];    	/* pcap error messages */
  pcap_t *handle;        			/* libpcap handle */
  bpf_u_int32 netp, maskp;    			/* netmask and ip */
  /* if the SYN and RST or ACK flags are set */
  char *filter = "(tcp[13] == 0x14) || (tcp[13] == 0x12)";
  struct bpf_program fp;    			/* compiled filter */
  /* ports to scan */
  char *portInit = argv[6];
  int starterPort[2];
  char *typeString = argv[4];
  int i, portMin, portMax, type;
  time_t tv = 0;
  int size = 0;

  type = strtol(typeString, &a, 10);//makes -t input an int

  if(pthread_mutex_init(&mutex1, NULL) != 0)
  {
    fprintf(stderr, "mutex init error\n");
    exit(1);
  }

  if (argc != 7)
    usage (argv[0]);

  /* open context */
  l = libnet_init (LIBNET_RAW4, device, libnet_errbuf);
  if (l == NULL)
    {
      fprintf (stderr, "Error opening context: %s", libnet_errbuf);
      exit (1);
    }

  while ((o = getopt (argc, argv, "i:t:p:")) > 0)
    {
      switch (o)
    {
    case 'i':
      if ((ipaddr = libnet_name2addr4 (l, optarg, LIBNET_RESOLVE)) == -1)
      {
        fprintf (stderr, "Invalid address: %s\n", libnet_geterror (l));
        usage (argv[0]);
      }
      break;
    case 't':
      if (type != 0 && type != 1)//checks for correct value
      {
        usage (argv[0]);
      }
      break;
    case 'p':
      if (portInit == NULL)
      {
        printf ("Must enter ports");
        usage (argv[0]);
      }
      size = portStringConvert(portInit, starterPort, type);//convert string to int
      if(size == 0)
      {
        usage (argv[0]);
      }
      break;
    default:
      usage (argv[0]);
      break;
    }
    }
    
  if(type == 1)//only use this code for port range
  {
    if(starterPort[0] > starterPort[1])//checks if min is first
    {
       usage (argv[0]);
    }

    portMin = starterPort[0];
    portMax = starterPort[1];
    size = portMax - (portMin - 1);//array size and amount to loop
  }

  int ports[size];//int after check for port range for size check
  
  if(type == 1)//check again for port range vs specific port
  {
    for(i = 0; i < size; i++)//makes an array first port to second port
    {    
      ports[i] = portMin;
      portMin++;
    }
  }
  else
  {
    for (i = 0; i < size; i++)//makes array for specific ports
    {
      ports[i] = starterPort[i];
    }
  }

  /* get the ip address of the device */
  if ((myipaddr = libnet_get_ipaddr4 (l)) == -1)
    {
      fprintf (stderr, "Error getting IP: %s", libnet_geterror (l));
      exit (1);
    }

  printf ("IP: %s\n", libnet_addr2name4 (ipaddr, LIBNET_DONT_RESOLVE));

/* get the device we are using for libpcap */
  if ((device = libnet_getdevice (l)) == NULL)
    {
      fprintf (stderr, "Device is NULL. Packet capture may be broken\n");
    }

  /* open the device with pcap */
  if ((handle =
       pcap_open_live (device, 1500, 0, 2000, libpcap_errbuf)) == NULL)
    {
      fprintf (stderr, "Error opening pcap: %s\n", libpcap_errbuf);
      exit (1);
    }

  if ((pcap_setnonblock (handle, 1, libnet_errbuf)) == -1)
    {
      fprintf (stderr, "Error setting nonblocking: %s\n", libpcap_errbuf);
      exit (1);
    }

  /* set the capture filter */
  if (pcap_lookupnet (device, &netp, &maskp, libpcap_errbuf) == -1)
    {
      fprintf (stderr, "Net lookup error: %s\n", libpcap_errbuf);
      exit (1);
    }

  if (pcap_compile (handle, &fp, filter, 0, maskp) == -1)
    {
      fprintf (stderr, "BPF error: %s\n", pcap_geterr (handle));
      exit (1);
    }

  if (pcap_setfilter (handle, &fp) == -1)
    {
      fprintf (stderr, "Error setting BPF: %s\n", pcap_geterr (handle));
      exit (1);
    }

  pcap_freecode (&fp);
  Bundle temp = {0, tcp, l, ipv4, myipaddr, ipaddr, tv, handle};//init the bundle
  Bundle bundle[size];
  for (i = 0; i < size; i++)//array of struct to prevent all threads from being the last port
  {
    bundle[i] = temp;
  }
//  int testCount = 0;
  //Creates threads to number of ports 
  pthread_t thread[size];
  for (i = 0; i < size; i++)//loop that assigns port and creates thread
    {
      //int sleeptime = 1000*50; //5 milliseconds
      //usleep(sleeptime);
      bundle[i].port = ports[i];
      if (pthread_create(&thread[i], NULL,  entry, &bundle[i]))
      {
        fprintf(stderr, "Error creating thead\n");
        return 1;
      } 
   //   testCount++;
    }
 // printf("testCount create: %d\n", testCount);
 // testCount = 0;
  for (i = 0; i < size; i++)//joins the thread
  {
    if (pthread_join(thread[i], NULL))
    {
      fprintf(stderr, "Error joining thread\n");
      return 2;
    } 
  //  testCount++;
  }

 // printf("testCount join: %d\n", testCount);
  printf("portCount: %d\n", portCount);
  struct portInfo printPort[portCount];
  for (i = 0; i < portCount; i++)//puts ports into an exact array to sort
  {
    printPort[i].portNum = storePort[i].portNum;
    printPort[i].id = storePort[i].id; 
  }

  int j;
  int temp1;
  int temp2;
  for(i = 0; i < portCount; i++)//simple n^2 sort array, lowest to highest
  {
    for(j = i + 1; j < portCount; j++)
    {
      if(printPort[i].portNum > printPort[j].portNum)
      {
        temp1 = printPort[i].portNum;
        temp2 = printPort[i].id;
        printPort[i].portNum = printPort[j].portNum;
        printPort[i].id = printPort[j].id;
        printPort[j].portNum = temp1;
        printPort[j].id = temp2;
      }
    }
  }
  int testCount = 0;
  for (i = 0; i < portCount; i++)//loops through array to print the ports
  {
    if(printPort[i].id == 1)
    {
       printf("Port %d appears to be closed\n", printPort[i].portNum);
    }
    else if(printPort[i].id == 2)
    {
      printf("Port %d appears to be open\n", printPort[i].portNum);
    }
    else if(printPort[i].id == 3)
    {
      printf("Port %d appears to be filtered\n", printPort[i].portNum);
    } 
    testCount++; 
  }
  printf("Ports scanned: %d\n", testCount);
  /* exit cleanly */

  pthread_mutex_destroy(&mutex1);
  libnet_destroy (l);
  return 0;
}
