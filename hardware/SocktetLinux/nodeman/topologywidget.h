#ifndef TOPOLOGYWIDGET_H
#define TOPOLOGYWIDGET_H

#include <QWidget>
#include <QScrollArea>
#include <QPainter>
#include <QPen>
#include <QThread>
#include <QTimer>
#include <libwsncomm.h>
#include <QMouseEvent>
#ifdef _WIN32
#include <winsock2.h>
#endif

namespace TOPOLOGY {
    #define NODE_WIDTH		90
    #define NODE_HEIGHT		50
    #define NODE_H_SPACE	20
    #define NODE_V_SPACE	NODE_HEIGHT
typedef struct point_t {
    int x, y;
}POINT;
typedef struct line_t {
    POINT start;
    POINT end;
}LINE;
typedef struct node_list_t {
    struct node_list_t *next;
    int depth;
    WSNCOMM_NODE *nodes;
    POINT *node_points;
    LINE *node_lines;
    int count;
} NODELIST;

typedef struct node_list_head_t {
    NODELIST *head;
} NODELISTHEAD;
}

namespace Ui {
    class TopologyWidget;
}

class TopologyWidget : public QWidget
{
    Q_OBJECT

public:
    explicit TopologyWidget(const QString &ip = "", QWidget *parent = 0);
    ~TopologyWidget();

    static void SetTopologyArea(const QString &ip, QScrollArea *area);
    static void ClearTopologyArea(QScrollArea *area);
    static void UpdateTopologyArea(QScrollArea *area);
    static TopologyWidget *GetTopologyArea(QScrollArea *area);

public slots:
    void updateTopology();

protected slots:
    void reConnect();

signals:
    void nodeClicked(const WSNCOMM_NODE *node);
    void needUpdate();
    void needReconnect();

protected:
    void paintEvent(QPaintEvent *);
    void mouseReleaseEvent(QMouseEvent *e);
    void resizeEvent(QResizeEvent *);

    static void cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList);
    static void cbNodeGone(void *arg, unsigned short nwkAddr);
    static void cbServerGone(void *arg);

private:
    Ui::TopologyWidget *ui;
    QString wsnSrvIp;
    QTimer *timer;
    TOPOLOGY::NODELISTHEAD layerList;
//    TOPOLOGY::LAYER_LIST layerList;
    int w, h;
    void *wsnSrvUser;
};

#endif // TOPOLOGYWIDGET_H
