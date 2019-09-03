#include "topologywidget.h"
#include "ui_topologywidget.h"
#include <string.h>
#include <QList>
#include <QTextCodec>
#include <QFile>
#include <QDebug>
#include <node_config.h>

//#define wsncomm_find_nodeTypeString(code)   (qDebug()<<"funcCode="<<code,wsncomm_find_nodeTypeString(code))
namespace TOPOLOGY {
#include <stdlib.h>
static int sort_node_list_helper(const void *n1, const void *n2)
{
    const WSNCOMM_NODE *node1 = (const WSNCOMM_NODE *)n1;
    const WSNCOMM_NODE *node2 = (const WSNCOMM_NODE *)n2;
    if(node1->nwkAddr == 0x0000)
        return -1;
    if(node2->nwkAddr == 0x0000)
        return 1;
    if(node1->parAddr == node2->parAddr)
        return ((int)node1->nwkAddr - (int)node2->nwkAddr);
    else
        return ((int)node1->parAddr - (int)node2->parAddr);
}
void sort_node_list(WSNCOMM_NODE *nodes, int count)
{
    qsort(nodes, count, sizeof(WSNCOMM_NODE), sort_node_list_helper);
}
typedef struct node_level_info_t {
    int depth;
    int start;
    int end;
} NODE_LEVEL;
static int find_in_area(WSNCOMM_NODE *nodes, int start, int end, unsigned short nwkAddr)
{
    int index;
    for(index = start; index <= end; index++)
    {
        if(nodes[index].nwkAddr == nwkAddr)
            break;
    }
    if(index > end)
        index = -1;
    return index;
}
int calc_node_level(WSNCOMM_NODE *nodes_sorted, int nodeCount, NODE_LEVEL *levels, int maxCount)
{
    int count = 0;
    int index = 1;
    NODE_LEVEL pLevel = { 0, 0, 0 };
    NODE_LEVEL cLevel = { 1, 1, 1 };
    if(nodes_sorted == NULL)
        return 0;
    if(nodes_sorted[0].nwkAddr != 0x0000)
        return 0;
    count = 1;
    if((levels != NULL) && (maxCount >= count))
        memcpy(&levels[0], &pLevel, sizeof(NODE_LEVEL));

    while((index < nodeCount) && ((maxCount < 0) || (count <= maxCount)))
    {
        // 如果当前节点的父节点不属于上一层,但是属于当前层,表示当前层实际已经结束,需要进入新一层
        int pIndex = find_in_area(nodes_sorted, pLevel.start, pLevel.end, nodes_sorted[index].parAddr);
        if(pIndex == -1)
        {
            pIndex = find_in_area(nodes_sorted, cLevel.start, cLevel.end, nodes_sorted[index].parAddr);
            if(pIndex != -1)
            {
                // 当前节点是新一层的节点
                cLevel.end = index - 1;
                if(levels != NULL)
                    memcpy(&levels[count], &cLevel, sizeof(NODE_LEVEL));
                count++;
                memcpy(&pLevel, &cLevel, sizeof(NODE_LEVEL));
                cLevel.depth++;
                cLevel.start = index;
                cLevel.end = index;
            }
        }
        // 将当前节点标记为当前层的结束节点
        cLevel.end = index;
        // 进入下一个节点
        index++;
    }
    if((maxCount < 0) || (count < maxCount))
    {
        cLevel.end = index - 1;
        if(levels != NULL)
            memcpy(&levels[count], &cLevel, sizeof(NODE_LEVEL));
        count++;
    }
    return count;
}

//#define NODE_WIDTH		60
//#define NODE_HEIGHT		60
//#define NODE_H_SPACE	20
//#define NODE_V_SPACE	NODE_HEIGHT


static int nodelist_init(NODELISTHEAD *h)
{
    h->head = NULL;
    return 0;
}
static NODELIST *nodelist_create_node(int depth, int count)
{
    NODELIST *node = (NODELIST *)malloc(sizeof(NODELIST));
    memset(node, 0, sizeof(NODELIST));
    node->depth = depth;
    if(count > 0)
    {
        node->count = count;
        node->nodes = NULL;
        node->node_points = (POINT *)malloc(sizeof(POINT) * count);
        memset(node->node_points, 0, sizeof(POINT) * count);
        node->node_lines = (LINE *)malloc(sizeof(LINE) * count);
        memset(node->node_lines, 0, sizeof(LINE) * count);
    }
    return node;
}
static int nodelist_destroy_node(NODELIST *node)
{
    if(node->nodes)
        wsncomm_delete_node_list(node->nodes, node->count);
    if(node->node_points)
        free(node->node_points);
    if(node->node_lines)
        free(node->node_lines);
    free(node);
    return 0;
}
static int nodelist_destroy_list(NODELISTHEAD *head)
{
    if(head == NULL)
        return 0;
    NODELIST *node = head->head;
    while(node)
    {
        NODELIST *next = node->next;
        nodelist_destroy_node(node);
        node = next;
    }
    head->head = NULL;
    return 0;
}
static int nodelist_append(NODELISTHEAD *head, NODELIST *newNode)
{
    NODELIST *node = head->head;
    if(node == NULL)
        head->head = newNode;
    else
    {
        while(node->next != NULL)
            node = node->next;
        node->next = newNode;
        newNode->next = NULL;
    }
    return 0;
}
// 从数据库加载节点信息,并按层次关系保存到链表
// TODO: 最好在这里统计合并同一节点不同传感器的情况
static int nodelist_load(const char *ip, NODELISTHEAD *head, int *maxWidth)
{
    NODELIST *node = NULL;
    int w = 0;
    int depth = 0;
    WSNCOMM_NODE *nodes;
    NODE_LEVEL *levels;
    int count = wsncomm_getAllNode(ip, &nodes);
    if(count <= 0)
        return -1;

    sort_node_list(nodes, count);
    depth = calc_node_level(nodes, count, NULL, -1);
    if(depth <= 0)
        return -2;
    levels = (NODE_LEVEL *)malloc(sizeof(NODE_LEVEL) * depth);
    depth = calc_node_level(nodes, count, levels, depth);
    int i;
    for(i = 0; i < depth; i++)
    {
        node = nodelist_create_node(i, levels[i].end - levels[i].start + 1);
        node->nodes = wsncomm_node_list_dup(&nodes[levels[i].start], node->count);
        nodelist_append(head, node);
        if(w < node->count)
            w = node->count;
    }
    wsncomm_delete_node_list(nodes, count);
    free(levels);
    if(maxWidth != NULL)
        *maxWidth = w;
    return depth;
}
// 根据节点链表计算每个节点的坐标和连线关系
static int calc_nodemap(int w, NODELIST *nodes, NODELIST *prev_line)
{
    if(nodes->depth == 0)
    {
        if(nodes->count != 1)
            return -1;
        nodes->node_points[0].x = (w - NODE_WIDTH) / 2;
        nodes->node_points[0].y = 0;
        // 根节点不需要填连线数据
    }
    else if(prev_line != NULL)
    {
        if(nodes->count <= 0)
            return -1;
        // 计算该层节点的起始位置和节点间的空白等信息
        int hor_space = (w - nodes->count * NODE_WIDTH) / (nodes->count + 1);
        int left = hor_space;
//		int top = nodes->depth * 2 * NODE_HEIGHT;
        int top = nodes->depth * (NODE_HEIGHT + NODE_V_SPACE);
        int parIndex = 0;
        int index = 0;
        while(index < nodes->count)
        {
            // 计算每一个节点的坐标信息
            // fill the node positon first
            nodes->node_points[index].x = left;
            nodes->node_points[index].y = top;
            // line start is always from the current node
            nodes->node_lines[index].start.x = left + NODE_WIDTH / 2;
            nodes->node_lines[index].start.y = top + NODE_HEIGHT / 2;
            left = left + NODE_WIDTH + hor_space;
            // find out the parent node
            while(prev_line->nodes[parIndex].nwkAddr != nodes->nodes[index].parAddr)
            {
                parIndex++;
                if(parIndex >= prev_line->count)
                    break;
            }
            if(parIndex >= prev_line->count)
                break;
            // got a parent node, then fill the line end position with the parent positon
            nodes->node_lines[index].end.x = prev_line->node_points[parIndex].x + NODE_WIDTH / 2;
            nodes->node_lines[index].end.y = prev_line->node_points[parIndex].y + NODE_HEIGHT / 2;
            // 一个节点的完整坐标信息到这里填充结束

            // 接下来需要统计合并相同地址的节点
            // merge the same node with different type
            while((index + 1 < nodes->count)
                && (nodes->nodes[index + 1].nwkAddr == nodes->nodes[index].nwkAddr))
            {
                // 直接从上一个节点拷贝坐标信息
                memcpy(&(nodes->node_points[index + 1]),
                        &(nodes->node_points[index]), sizeof(POINT));
                memcpy(&(nodes->node_lines[index + 1]),
                        &(nodes->node_lines[index]), sizeof(LINE));
                index++;
            }
            index++;
        }
        return 0;
    }
    return -1;
}
int generate_topology(const char *ip, int &w, int &h, NODELISTHEAD *nodeListHead)
{
    int ret = -1;
    do {
        nodelist_destroy_list(nodeListHead);
        // 检索数据库
        int minW, minH;
        minH = nodelist_load(ip, nodeListHead, &minW);
        if(minH <= 0)
            break;

        minH = NODE_HEIGHT * minH + NODE_V_SPACE * (minH - 1);
        minW = NODE_WIDTH * minW + NODE_H_SPACE * (minW - 1);

        if(w < minW)
            w = minW;
        if(h < minH)
            h = minH;

        NODELIST *cur, *prev;
        // 逐层计算节点坐标
        cur = prev = nodeListHead->head;
        calc_nodemap(w, cur, NULL);
        cur = cur->next;
        while(cur)
        {
            calc_nodemap(w, cur, prev);
            prev = cur;
            cur = cur->next;
        }
#if 0
        // 逐层输出节点坐标
        cur = nodeListHead.head;
        while(cur)
        {
            print_nodes(cur);
            cur = cur->next;
        }
        // 逐层输出连线坐标
        cur = nodeListHead.head;
        while(cur)
        {
            print_lines(cur);
            cur = cur->next;
        }
#endif
        ret = 0;
    } while(0);
//    printf("</TOPOLOGY>\r\n");
//    nodelist_destroy_list(&nodeListHead);
    return ret;
}
}

TopologyWidget::TopologyWidget(const QString &ip, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::TopologyWidget),
    wsnSrvIp(ip),
    timer(NULL),
    wsnSrvUser(NULL)
{
    TOPOLOGY::nodelist_init(&layerList);
    ui->setupUi(this);
    w = this->geometry().width();
    h = this->geometry().height();

    connect(this, SIGNAL(needReconnect()), this, SLOT(reConnect()));
    connect(this, SIGNAL(needUpdate()), this, SLOT(updateTopology()));

    reConnect();
}

TopologyWidget::~TopologyWidget()
{
    if(wsnSrvUser)
        wsncomm_unregister(wsnSrvUser);
    if(timer)
        delete timer;
    delete ui;
    TOPOLOGY::nodelist_destroy_list(&layerList);
}

void TopologyWidget::reConnect()
{
    if(timer == NULL)
    {
        timer = new QTimer;
        connect(timer, SIGNAL(timeout()), this, SLOT(reConnect()));
        timer->setInterval(100);
        timer->setSingleShot(true);
        timer->start();
    }
    else
    {
        if(wsnSrvUser != NULL)
            wsncomm_unregister(wsnSrvUser);
        wsnSrvUser = wsncomm_register(wsnSrvIp.toAscii().constData(), NULL, cbNewFunc, NULL, cbNodeGone, cbServerGone, (void *)this);
        if(wsnSrvUser == NULL)
        {
            timer->setInterval(5000);
            timer->start();
        }
        else
        {
            timer->stop();
        }
    }
}

void TopologyWidget::paintEvent(QPaintEvent *)
{
    QPainter p(this);
    p.setPen(QPen(Qt::black,3,Qt::SolidLine));
    TOPOLOGY::NODELIST *l = layerList.head;
    while(l != NULL)
    {
        int i;
        for(i = 0; i < l->count; i++)
        {
            p.drawLine(l->node_lines[i].start.x, l->node_lines[i].start.y
                       , l->node_lines[i].end.x, l->node_lines[i].end.y);
        }
        l = l->next;
    }
    p.setPen(QPen(Qt::black,0,Qt::NoPen));
    l = layerList.head;
    while(l != NULL)
    {
        int i;
        for(i = 0; i < l->count; i++)
        {
            QColor bgCr(255,0,0), fntCr(0,0,0), typeCr(0,0,0);
            if(l->nodes[i].funcNum <= 0)
                continue;
            if(l->nodes[i].funcInfo == NULL)
                continue;
            switch(l->nodes[i].funcInfo[0].funcCode)
            {
            case DevCoordinator:
                bgCr = QColor(0,0,255);
                fntCr = QColor(255,255,0);
                typeCr = QColor(204,204,0);
                break;
            case DevRouter:
                bgCr = QColor(0,255,0);
                fntCr = QColor(255,0,255);
                typeCr = QColor(204,0,204);
                break;
            default:    // End device
                bgCr = QColor(255,0,0);
                fntCr = QColor(0,255,255);
                typeCr = QColor(0,204,204);
                break;
            }
            p.setBrush(QBrush(bgCr, Qt::SolidPattern));
            p.drawRoundedRect(l->node_points[i].x, l->node_points[i].y, NODE_WIDTH, NODE_HEIGHT, 10, 10);
            p.setPen(QPen(fntCr, Qt::SolidLine));
            char tmpNum[5];
            sprintf(tmpNum, "%04X", l->nodes[i].nwkAddr);
            p.drawText(l->node_points[i].x, l->node_points[i].y, NODE_WIDTH, NODE_HEIGHT / 2, Qt::AlignHCenter|Qt::AlignVCenter, tmpNum);
            p.setPen(QPen(typeCr, Qt::SolidLine));
            QString tipText;
            if(l->nodes[i].funcNum <= 0)
                tipText = "?N?";
            else if(l->nodes[i].funcNum <= 2)
            {
                int j;
                tipText = "";
                for(j = 0; j < l->nodes[i].funcNum; j++)
                {
                    if(j != l->nodes[i].funcNum - 1)
                        tipText += QString::fromUtf8(wsncomm_find_nodeTypeString(l->nodes[i].funcInfo[j].funcCode)) + ",";
                    else
                        tipText += QString::fromUtf8(wsncomm_find_nodeTypeString(l->nodes[i].funcInfo[j].funcCode));
                }
            }
            else
            {
                tipText = QString::fromUtf8(wsncomm_find_nodeTypeString(l->nodes[i].funcInfo[0].funcCode)) + ",...";
            }
            p.drawText(l->node_points[i].x, l->node_points[i].y + NODE_HEIGHT / 2, NODE_WIDTH, NODE_HEIGHT / 2, Qt::AlignHCenter|Qt::AlignVCenter, tipText);
        }
        l = l->next;
    }
}

void TopologyWidget::mouseReleaseEvent(QMouseEvent *e)
{
    if(layerList.head == NULL)
        return;
    TOPOLOGY::NODELIST *l = layerList.head;
    while(l != NULL)
    {
        int i;
        for(i = 0; i < l->count; i++)
        {
            const QPoint pnt(l->node_points[i].x, l->node_points[i].y);
            if((e->x() >= pnt.x()) && (e->x() <= (pnt.x() + NODE_WIDTH)))
                if((e->y() >= pnt.y()) && (e->y() <= (pnt.y() + NODE_HEIGHT)))
            {
                if((l->nodes[i].funcInfo != NULL)
                        && (l->nodes[i].funcInfo[0].funcCode != DevCoordinator)
                        && (l->nodes[i].funcInfo[0].funcCode != DevRouter))
                {
                    emit this->nodeClicked(&l->nodes[i]);
                }
                break;
            }
        }
        l = l->next;
    }
}

void TopologyWidget::resizeEvent(QResizeEvent *)
{
    updateTopology();
}

void TopologyWidget::updateTopology()
{
    QWidget *pParent = qobject_cast<QWidget*>(this->parent());
    if(pParent != NULL)
    {
        QRect rc = pParent->geometry();
        w = rc.width();
        h = rc.height();
    }
    int res = TOPOLOGY::generate_topology(wsnSrvIp.toAscii().constData(), w, h, &layerList);
    if(res == 0)
    {
        if(w > 0 && h > 0)
        {
            this->setMinimumWidth(w);
            this->setMinimumHeight(h);
        }
        this->repaint();
    }
}

void TopologyWidget::SetTopologyArea(const QString &ip, QScrollArea *area)
{
    QWidget *oldWidget = area->widget();
    TopologyWidget *tpWidget = qobject_cast<TopologyWidget *>(oldWidget);
    if(tpWidget == NULL)
    {
        area->setStyleSheet("background-image: url(/root/applogo.png);");
        QRect rc = area->geometry();
        tpWidget = new TopologyWidget(ip);
        tpWidget->setMinimumWidth(rc.width());
        tpWidget->setMinimumHeight(rc.height());
        area->setWidget(tpWidget);
        QPalette palette;
        QBrush brush(QColor(255, 255, 255, 255), Qt::SolidPattern);
        palette.setBrush(QPalette::Active, QPalette::Base, brush);
        palette.setBrush(QPalette::Active, QPalette::Window, brush);
        palette.setBrush(QPalette::Inactive, QPalette::Base, brush);
        palette.setBrush(QPalette::Inactive, QPalette::Window, brush);
        palette.setBrush(QPalette::Disabled, QPalette::Base, brush);
        palette.setBrush(QPalette::Disabled, QPalette::Window, brush);
        area->setPalette(palette);
    }
    else
    {
        tpWidget->wsnSrvIp = ip;
        tpWidget->reConnect();
    }
}

void TopologyWidget::ClearTopologyArea(QScrollArea *area)
{
    QWidget *oldWidget = area->widget();
    TopologyWidget *tpWidget = qobject_cast<TopologyWidget *>(oldWidget);
    if(tpWidget == NULL)
        return;
    if(tpWidget->wsnSrvUser)
        wsncomm_unregister(tpWidget->wsnSrvUser);
    tpWidget->wsnSrvUser = NULL;
    if(tpWidget->timer)
        delete tpWidget->timer;
    tpWidget->timer = NULL;
}

void TopologyWidget::UpdateTopologyArea(QScrollArea *area)
{
    QWidget *oldWidget = area->widget();
    TopologyWidget *tpWidget = qobject_cast<TopologyWidget *>(oldWidget);
    if(tpWidget == NULL)
        return;
    tpWidget->updateTopology();
}

TopologyWidget *TopologyWidget::GetTopologyArea(QScrollArea *area)
{
    return qobject_cast<TopologyWidget *>(area->widget());
}

void TopologyWidget::cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
    Q_UNUSED(nwkAddr);Q_UNUSED(funcNum);Q_UNUSED(funcList);
    TopologyWidget *This = qobject_cast<TopologyWidget *>((QObject *)arg);
    if(This == NULL)
        return;
    emit This->needUpdate();
}

void TopologyWidget::cbNodeGone(void *arg, unsigned short nwkAddr)
{
    Q_UNUSED(nwkAddr);
    TopologyWidget *This = qobject_cast<TopologyWidget *>((QObject *)arg);
    if(This == NULL)
        return;
    emit This->needUpdate();
}

void TopologyWidget::cbServerGone(void *arg)
{
    TopologyWidget *This = qobject_cast<TopologyWidget *>((QObject *)arg);
    if(This == NULL)
        return;
    emit This->needReconnect();
}
