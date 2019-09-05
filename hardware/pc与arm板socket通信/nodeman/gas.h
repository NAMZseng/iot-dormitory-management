#ifndef GAS_H
#define GAS_H

#include <QWidget>
#include <libwsncomm.h>
#include <node_config.h>
#ifdef _WIN32
#include <winsock2.h>
#endif

namespace Ui {
    class Gas;
}

class Gas : public QWidget
{
    Q_OBJECT

public:
    explicit Gas(const QString &ip, quint8 id = 0xFF, QWidget *parent = 0);
    ~Gas();

public slots:
    void reconnect(const QString &ip, quint8 id = 0xFF);

private:
    Ui::Gas *ui;
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

#endif // GAS_H
