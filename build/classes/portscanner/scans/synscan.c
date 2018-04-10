#define _BSD_SOURCE 1
#include <stdio.h>
#include <unistd.h>
#include <time.h>
#include <libnet.h>
#include <pcap.h>
#include <string.h>

/* This program runs a simple syn scan on a specified IP address */

/* Original authors: Justin Clarke, Nitesh Dhanjani */
/* Book: Network Security Tools, Section 11.4. Combining libnet and libpcap */
/* books.gigatux.nl/mirror/networksecuritytools/0596007949/toc.html */

int answer = 0;            /* flag for scan timeout */

/* usage */
void
usage (char *name)
{
  printf ("Usage: %s -i ip_address -p ports\n", name);
  printf ("    -i    IP address to scan\n");
  printf ("    -p    Ports to check\n");
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
      printf ("Port %d appears to be closed\n", ntohs (tcp->th_sport));
      answer = 0;
    }
  else
    {
      if (tcp->th_flags == 0x12)
      {
      printf ("Port %d appears to be open\n", ntohs (tcp->th_sport));
      answer = 0;
      }
    }
}

void portStringConvert(char *portInit, int *ports)
{
    int size = strlen(portInit);
    char portA[size];//char array size of char *
    char *delim = ":";//break point in string
    char *token = "";
    char *a;//used only for strtol(), stores rest of string which is null in this case
   // int ports[strlen(portInit)];
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
}

int
main (int argc, char *argv[])
{
  char *device = NULL;        			/* device for sniffing/sending */
  char o;            				/* for option processing */
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
  char *portInit = argv[4];
  int ports[strlen(portInit)];
  int i;
  time_t tv;


  if (argc != 5)
    usage (argv[0]);

  /* open context */
  l = libnet_init (LIBNET_RAW4, device, libnet_errbuf);
  if (l == NULL)
    {
      fprintf (stderr, "Error opening context: %s", libnet_errbuf);
      exit (1);
    }

  while ((o = getopt (argc, argv, "i:p:")) > 0)
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
    case 'p':
      if (portInit == NULL)
      {
        printf ("Must enter ports");
        usage (argv[0]);
      }
      portStringConvert(portInit, ports);
      break;
    default:
      usage (argv[0]);
      break;
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

  /* seed the pseudo random number generator */
  libnet_seed_prand (l);

  for (i = 0; ports[i] != 0; i++)
    {
      /* build the TCP header */
      tcp = libnet_build_tcp (libnet_get_prand (LIBNET_PRu16),    	/* src port */
                  ports[i],    						/* destination port */
                  libnet_get_prand (LIBNET_PRu16),    			/* sequence number */
                  0,    						/* acknowledgement */
                  TH_SYN,    						/* control flags */
                  7,    						/* window */
                  0,    					/* checksum - 0 = autofill */
                  0,    						/* urgent */
                  LIBNET_TCP_H,    					/* header length */
                  NULL,    						/* payload */
                  0,    						/* payload length */
                  l,    						/* libnet context */
                  tcp);    						/* protocol tag */

    if (tcp == -1)
    {
      fprintf (stderr,
           "Unable to build TCP header: %s\n", libnet_geterror (l));
      exit (1);
    }

      /* build the IP header */
      ipv4 = libnet_build_ipv4 (LIBNET_TCP_H + LIBNET_IPV4_H,    	/* length */
                0,    							/* TOS */
                libnet_get_prand (LIBNET_PRu16),    			/* IP ID */
                0,    							/* frag offset */
                127,    						/* TTL */
                IPPROTO_TCP,    					/* upper layer protocol */
                0,    							/* checksum, 0=autofill */
                myipaddr,    						/* src IP */
                ipaddr,    						/* dest IP */
                NULL,    						/* payload */
                0,    							/* payload len */
                l,    							/* libnet context */
                ipv4);    						/* protocol tag */

    if (ipv4 == -1)
    {
      fprintf (stderr,
           "Unable to build IPv4 header: %s\n", libnet_geterror (l));
      exit (1);
    }

      /* write the packet */
      if ((libnet_write (l)) == -1)
    {
      fprintf (stderr, "Unable to send packet: %s\n",
           libnet_geterror (l));
      exit (1);
    }

      /* set variables for flag/counter */
      answer = 1;
      tv = time (NULL);

      /* capture the reply */
      while (answer)
    {
      pcap_dispatch (handle, -1, packet_handler, NULL);

      if ((time (NULL) - tv) > 2)
        {
          answer = 0;    /* timed out */
          printf ("Port %d appears to be filtered\n", ports[i]);
        }
    }

   // dbPortInput(id, ports[i], status, expected_status, host, user, pass);

    }
  /* exit cleanly */
  libnet_destroy (l);
  return 0;
}
