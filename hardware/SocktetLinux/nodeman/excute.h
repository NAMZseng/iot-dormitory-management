#ifndef EXCUTE_H
#define EXCUTE_H


#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/shm.h>

#define MAXDATASIZE 10
#define SERVER_PORT 8888
#define SERVER_IP "192.168.87.1"
static int sockfd, numbytes;
static char buf[MAXDATASIZE];
static struct sockaddr_in server_addr;

#include <QWidget>
#include <libwsncomm.h>
#include <node_config.h>
#ifdef _WIN32
#include <winsock2.h>
#endif

namespace Ui {
    class Excute;
}

class Excute : public QWidget
{
    Q_OBJECT

public:
    explicit Excute(const QString &ip, quint8 id = 0xFF, QWidget *parent = 0);
    ~Excute();

public slots:
    void reconnect(const QString &ip, quint8 id = 0xFF);

private slots:
    void on_applyBtn_clicked();

private:
    Ui::Excute *ui;
    quint8 funcID;
    QString wsnSrvIp;
    void *wsnSrvUser;
    unsigned short curNwkAddr;

protected:
    void showEvent(QShowEvent *);
    void hideEvent(QHideEvent *);
    static void cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList);
    static void cbNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len);
    static void cbNodeGone(void *arg, unsigned short nwkAddr);

signals:
    void nodeInfoChanged(unsigned short nwkAddr, unsigned short parAddr, unsigned char *mac);
    void gotNewNodeData(char v);

protected slots:
    void updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr);
    void updateNodeData(char v);

public:
     static void showOut(const QString &ip, quint8 id = 0xFF);
};

#define EXE_AU 0x01
#define EXE_AD 0x02
#define EXE_BU 0x04
#define EXE_BD 0x08

#endif // EXCUTE_H
