#ifndef ICCARD_H
#define ICCARD_H

#include <QWidget>
#include <libwsncomm.h>
#include <node_config.h>
#ifdef _WIN32
#include <winsock2.h>
#endif
#include <QTimer>

namespace Ui {
    class ICCard;
}

class ICCard : public QWidget
{
    Q_OBJECT

public:
    explicit ICCard(const QString &ip, quint8 id = 0xFF, QWidget *parent = 0);
    ~ICCard();

public slots:
    void reconnect(const QString &ip, quint8 id = 0xFF);

private:
    Ui::ICCard *ui;
    QTimer *timer;
    QString wsnSrvIp;
    quint8 funcID;
    void *wsnSrvUser;
    unsigned short curNwkAddr;

signals:
    void nodeInfoChanged(unsigned short nwkAddr, unsigned short parAddr, unsigned char *mac);
    void gotNewNodeData(const QString &id);

protected:
    void showEvent(QShowEvent *);
    void hideEvent(QHideEvent *);
    static void cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList);
    static void cbNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len);
    static void cbNodeGone(void *arg, unsigned short nwkAddr);

protected slots:
    void updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr);
    void updateNodeData(const QString &id);
    void onTimeOut();

public:
    static void showOut(const QString &ip, quint8 id = 0xFF);
};

#endif // ICCARD_H
