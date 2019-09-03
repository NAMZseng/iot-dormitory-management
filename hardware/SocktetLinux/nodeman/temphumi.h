#ifndef TEMPHUMI_H
#define TEMPHUMI_H

#include <QWidget>
#include <libwsncomm.h>
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

#define SERVER_PORT 8888
#define MAXDATASIZE 100
#define SERVER_IP "192.168.0.100"

static bool flag0 = false;
static bool flag1 = false;


#ifdef _WIN32
#include <winsock2.h>
#endif
namespace Ui {
    class TempHumi;
}

class TempHumi : public QWidget
{
    Q_OBJECT

public:
    explicit TempHumi(const QString &ip, quint8 tempId = 0xFF, quint8 hummId = 0xFF, QWidget *parent = 0);
    ~TempHumi();

public slots:
    void reconnect(const QString &ip, quint8 tempId = 0xFF, quint8 hummId = 0xFF);

private:
    Ui::TempHumi *ui;
    QString wsnSrvIp;
    quint8 tempFuncID;
    quint8 hummFuncID;
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
    void gotNewTempData(unsigned short temp);
    void gotNewHumData(unsigned short hum);

protected slots:
    void updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr);
    void updateTempData(unsigned short temp);
    void updateHumData(unsigned short hum);

public:
    static  void showOut(const QString &ip, quint8 tempId = 0xFF, quint8 hummId = 0xFF);
};

#endif // TEMPHUMI_H
