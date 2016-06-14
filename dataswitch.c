/*This Application is created to intract with DataSwitch Service which is running on the android device.
 *Service starts a server in forground.
 *And this application works like client.
 *This is an gui app
 */
#include <gtk/gtk.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include<pthread.h>
#include<stdlib.h>
#include<unistd.h>
int i;
pthread_t tid;
int error(const char *msg)
{
    perror(msg);
    return 5;
}

int sendMessage(const char* msg)
{
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    char buffer[256];
    
    portno = atoi("2500");
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    server = gethostbyname("192.168.43.1");
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        return 4;
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
         (char *)&serv_addr.sin_addr.s_addr,
         server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
        return error("ERROR connecting");
    
    bzero(buffer,256);
    strcpy(buffer,msg);
    n = write(sockfd,buffer,strlen(buffer));
    if (n < 0) 
         return error("ERROR writing to socket");
    bzero(buffer,256);
    n = read(sockfd,buffer,255);
    if (n < 0) 
         return error("ERROR reading from socket");
   
    close(sockfd);
return 2;
}
/*
 * Function data_switch is started in another thread.
 * This function  checks the changes in globle variable "i" 
 * If i=1 then it passes "on" string to sendMessage function which is send to android service using networking.
 * If i=0 then it passes "off".
 * Chages is done due to client_switch function
 */

void* data_switch(void *arg)
{
while(1)
{
if(i==1)
{
i=sendMessage("on");
}
else if(i==0)
{
i=sendMessage("off");
}
}
}
/*
 * client_switch is called when user intract with switch1 (gui description is given in switch.glade)
 *
 */
static void
client_switch (GtkWidget *widget,
             gpointer   data)
{
  if(gtk_switch_get_active ((GtkSwitch*)widget))
  i=1;
  else
  i=0;
}

int
main (int   argc,
      char *argv[])
{
  GtkBuilder *builder;
  GObject *window;
  GObject *button;
  
  i=0;
  gtk_init (&argc, &argv);
  
  /* Constructing a GtkBuilder instance and loading our UI description */
  builder = gtk_builder_new ();
  gtk_builder_add_from_file (builder, "switch.glade", NULL);

  /* Connecting signal handlers to the constructed widgets. */
  window = gtk_builder_get_object (builder, "window1");
  g_signal_connect (window, "destroy", G_CALLBACK (gtk_main_quit), NULL);

  button = gtk_builder_get_object (builder, "switch1");
  /*Turning off switch */
  gtk_switch_set_active ((GtkSwitch*)button,FALSE);
  /*Connecting signal handlers (client_switch) to switch1 */
  g_signal_connect (button, "notify::active", G_CALLBACK (client_switch), NULL);
  /*
   *Starting Itraction work in another thread so that it can not freeze the gui.
   *The function data_switch is started in andother thread. 
   */
  pthread_create(&tid, NULL, &data_switch, NULL);

  

  gtk_main ();

  return 0;
}
