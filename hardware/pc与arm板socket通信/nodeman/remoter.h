#ifndef REMOTER_H
#define REMOTER_H

#include <QWidget>
#include <libwsncomm.h>
#include <node_config.h>
#include <QTimer>
#ifdef _WIN32
#include <winsock2.h>
#endif

namespace Ui {
    class Remoter;
}

class Remoter : public QWidget
{
    Q_OBJECT

public:
    explicit Remoter(const QString &ip, quint8 id = 0xFF, QWidget *parent = 0);
    ~Remoter();

public slots:
    void reconnect(const QString &ip, quint8 id = 0xFF);

private slots:
    void on_applyBtn_clicked();
    void on_codeLenAddBtn_clicked();
    void on_codeLenReduceBtn_clicked();
    void on_randDataBtn_clicked();

private:
    Ui::Remoter *ui;
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
    void gotNewNodeData(QByteArray *v);

protected slots:
    void updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr);
    void updateNodeData(QByteArray *v);

public:
     static void showOut(const QString &ip, quint8 id = 0xFF);
};

#endif // REMOTER_H
