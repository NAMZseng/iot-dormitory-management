#ifndef FIRE_H
#define FIRE_H

#include <QWidget>
#include <libwsncomm.h>
#include <node_config.h>
#ifdef _WIN32
#include <winsock2.h>
#endif

namespace Ui {
    class Fire;
}

class Fire : public QWidget
{
    Q_OBJECT

public:
    explicit Fire(const QString &ip, quint8 id = 0xFF, QWidget *parent = 0);
    ~Fire();

public slots:
    void reconnect(const QString &ip, quint8 id = 0xFF);

private:
    Ui::Fire *ui;
    QPixmap pic[2];
    QString wsnSrvIp;
    quint8 funcID;
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

#endif // FIRE_H
