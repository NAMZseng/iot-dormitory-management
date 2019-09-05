#include <QtGui/QApplication>
#include <QWSServer>
#include "widget.h"
#define MAIN_NODE_CONFIG
#include <node_config.h>

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <unistd.h>
#include <arpa/inet.h>
#include "temphumi.h"
#include "excute.h"

void function(char c)
{
    wsncomm_sendNode_byType("127.0.0.1", 11, 3, &c, 1);
}


int main(int argc, char *argv[])
{
    printf("server initialization...\n");
    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        perror("socket");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    server_addr.sin_addr.s_addr = inet_addr(SERVER_IP);
    bzero(&(server_addr.sin_zero),sizeof(server_addr.sin_zero));

    if (connect(sockfd, (struct sockaddr *)&server_addr,sizeof(struct sockaddr_in)) == -1){
         perror("connect error");
         exit(1);
     }

    while (1) {

        bzero(buf,MAXDATASIZE);
        printf("\nBegin receive...\n");
        if ((numbytes = recv(sockfd, buf, MAXDATASIZE, 0)) == -1){
            perror("recv");
            exit(1);
        } else if (numbytes > 0) {
            int len, bytes_sent;
            buf[numbytes] = '\0';
           printf("Received: %s\n",buf);
           if (buf[0] >= '1' && buf[0] <= '4') {
               function(buf[0]);
               printf("apply end\n");
           }
       } else {
           printf("soket end!\n");
           break;
       }
    }
    close(sockfd);

    QApplication a(argc, argv);
    if(QApplication::GuiServer == a.type())
    {
    	QImage img = QImage("/root/applogo.png");
    	QBrush brush = QBrush(img);
//    	QWSServer::setBackground(brush);
    }
    Widget w("127.0.0.1");
    w.showMaximized();

    return a.exec();
}
