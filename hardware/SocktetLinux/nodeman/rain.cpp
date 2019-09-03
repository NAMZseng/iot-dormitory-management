#include "rain.h"
#include "ui_rain.h"
#include <QTextCodec>
#include <QDebug>
#include <QDesktopWidget>

Rain::Rain(const QString &ip, quint8 id, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Rain),
    funcID(id),
    wsnSrvUser(NULL),
    curNwkAddr(0xFFFF)
{
    ui->setupUi(this);
    setWindowFlags(windowFlags() & ~Qt::WindowMinMaxButtonsHint);
    this->setGeometry(QApplication::desktop()->width()/5*3,40,this->width(),this->height());
    pic[0].load(":/pic/sun.png");
    pic[1].load(":/pic/rainUmbrella.png");
    ui->labelPic->setPixmap(pic[0]);
    ui->labelPic->setScaledContents(true);

    connect(this, SIGNAL(nodeInfoChanged(unsigned short,unsigned short,unsigned char*)), this, SLOT(updateNodeInfo(unsigned short,unsigned short,unsigned char*)));
    connect(this, SIGNAL(gotNewNodeData(char)), this, SLOT(updateNodeData(char)));
qDebug()<<"[Rain]====Rain ip:"<<ip<<"; func ID:"<<id;
    reconnect(ip, id);
}

Rain::~Rain()
{
    delete ui;
}

void Rain::reconnect(const QString &ip, quint8 id)
{

    wsnSrvIp = ip;
    funcID = id;
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                    NULL, cbNewFunc, cbNewData, cbNodeGone,
                    NULL, (void *)this);

    WSNCOMM_NODE *node = wsncomm_getNode_byType(ip.toUtf8().constData(), DevRain, funcID);
    if(node != NULL)
    {
        curNwkAddr = node->nwkAddr;
        updateNodeInfo(node->nwkAddr, node->parAddr, node->hwAddr);
        char *data = NULL;
        int len = wsncomm_getNodeData_byType(ip.toUtf8().constData(), DevRain, funcID, &data);
        if(data != NULL)
        {
            updateNodeData(*((unsigned short *)data));
            free(data);
        }
        wsncomm_delete_node(node);
    }
    else
    {
        curNwkAddr = 0xFFFF;
        updateNodeInfo(0xFFFF, 0xFFFF, NULL);
    }
    QString title = QTextCodec::codecForName("UTF-8")->toUnicode(wsncomm_find_nodeTypeString(DevRain));
    title += " - ";
    if(funcID != 0xFF)
        title += QString::number(funcID);
    else
        title += tr("Any");
    this->setWindowTitle(title);
}

void Rain::showEvent(QShowEvent *)
{
    if(wsnSrvUser == NULL)
    {
        wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                        NULL, cbNewFunc, cbNewData, cbNodeGone,
                        NULL, (void *)this);
    }
}

void Rain::hideEvent(QHideEvent *)
{
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = NULL;
}

void Rain::updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr)
{
    if(nwkAddr == 0xFFFF)
    {
        ui->nwkAddrEdit->setText(tr("Invalid"));
        ui->parAddrEdit->setText(tr("Invalid"));
        ui->hwAddrEdit->setText(tr("Invalid"));
    }
    else
    {
        char tmpString[20];
        sprintf(tmpString, "%04X", nwkAddr);
        ui->nwkAddrEdit->setText(tmpString);
        sprintf(tmpString, "%04X", parAddr);
        ui->parAddrEdit->setText(tmpString);
        ui->hwAddrEdit->setText(QByteArray((char *)hwAddr, 8).toHex());
    }
}

void Rain::updateNodeData(char v)
{
    switch(v)
    {
    case 0:
        ui->labelPic->setPixmap(pic[0]);
        ui->labelPic->setScaledContents(true);
        break;
    case 1:
        ui->labelPic->setPixmap(pic[1]);
        ui->labelPic->setScaledContents(true);
        break;
    default:
        ui->labelPic->setPixmap(QPixmap());
        break;
    }
}

Rain *rain = NULL;
void Rain::showOut(const QString &ip, quint8 id)
{
    qDebug()<<"Rain ip:"<<ip<<"; func ID:"<<id;
    if(rain == NULL)
        rain = new Rain(ip);
    else
        rain->reconnect(ip, id);
    rain->show();
}

void Rain::cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
    Rain *This = qobject_cast<Rain *>((QObject *)arg);
    if(This == NULL)
        return;
    int i;
    for(i = 0; i < funcNum; i++)
    {
        if((funcList[i].funcCode == DevRain) && ((This->funcID == 0xFF) || (This->funcID == funcList[i].funcID)))
        {
            WSNCOMM_NODE *node = wsncomm_getNode_byAddr(This->wsnSrvIp.toUtf8().constData(), nwkAddr);
            if(node != NULL)
            {
                This->curNwkAddr = nwkAddr;
                emit This->nodeInfoChanged(node->nwkAddr, node->parAddr, node->hwAddr);
                wsncomm_delete_node(node);
            }
        }
    }
}

void Rain::cbNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len)
{
    if(funcCode != DevRain)
        return;
    Rain *This = qobject_cast<Rain *>((QObject *)arg);
    if(This == NULL)
        return;
    if((This->funcID != 0xFF) && (funcID != This->funcID))
        return;
    if(This->curNwkAddr == 0xFFFF)
    {
        WSNCOMM_NODE *node = wsncomm_getNode_byAddr(This->wsnSrvIp.toUtf8().constData(), nwkAddr);
        if(node != NULL)
        {
            This->curNwkAddr = nwkAddr;
            emit This->nodeInfoChanged(node->nwkAddr, node->parAddr, node->hwAddr);
            wsncomm_delete_node(node);
        }
        else
            emit This->nodeInfoChanged(0xFFFF, 0xFFFF, NULL);
    }
    emit This->gotNewNodeData(data[0]);
}

void Rain::cbNodeGone(void *arg, unsigned short nwkAddr)
{
    Rain *This = qobject_cast<Rain *>((QObject *)arg);
    if(This == NULL)
        return;
    if(nwkAddr != This->curNwkAddr)
        return;
    This->curNwkAddr = 0xFFFF;
    emit This->nodeInfoChanged(0xFFFF, 0xFFFF, NULL);
}
