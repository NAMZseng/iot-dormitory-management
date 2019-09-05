#include "smoke.h"
#include "ui_smoke.h"
#include <QTextCodec>
#include <QDebug>
#include <QDesktopWidget>

Smoke::Smoke(const QString &ip, quint8 id, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Smoke),
    funcID(id),
    wsnSrvUser(NULL),
    curNwkAddr(0xFFFF)
{
    ui->setupUi(this);
    setWindowFlags(windowFlags() & ~Qt::WindowMinMaxButtonsHint);
    this->setGeometry(10,QApplication::desktop()->height()/2+15
                      ,this->width(),this->height());

    pic[0].load(":/pic/secure.png");
    pic[1].load(":/pic/smoke.png");
    ui->labelPic->setPixmap(pic[0]);
    ui->labelPic->setScaledContents(true);

    connect(this, SIGNAL(nodeInfoChanged(unsigned short,unsigned short,unsigned char*)), this, SLOT(updateNodeInfo(unsigned short,unsigned short,unsigned char*)));
    connect(this, SIGNAL(gotNewNodeData(char)), this, SLOT(updateNodeData(char)));

    reconnect(ip, id);
}

Smoke::~Smoke()
{
    delete ui;
}

void Smoke::reconnect(const QString &ip, quint8 id)
{

    wsnSrvIp = ip;
    funcID = id;
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                    NULL, cbNewFunc, cbNewData, cbNodeGone,
                    NULL, (void *)this);

    WSNCOMM_NODE *node = wsncomm_getNode_byType(ip.toUtf8().constData(), DevSmoke, funcID);
    if(node != NULL)
    {
        curNwkAddr = node->nwkAddr;
        updateNodeInfo(node->nwkAddr, node->parAddr, node->hwAddr);
        char *data = NULL;
        int len = wsncomm_getNodeData_byType(ip.toUtf8().constData(), DevSmoke, funcID, &data);
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
    QString title = QTextCodec::codecForName("UTF-8")->toUnicode(wsncomm_find_nodeTypeString(DevSmoke));
    title += " - ";
    if(funcID != 0xFF)
        title += QString::number(funcID);
    else
        title += tr("Any");
    this->setWindowTitle(title);
}

void Smoke::showEvent(QShowEvent *)
{
    if(wsnSrvUser == NULL)
    {
        wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                        NULL, cbNewFunc, cbNewData, cbNodeGone,
                        NULL, (void *)this);
    }
}

void Smoke::hideEvent(QHideEvent *)
{
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = NULL;
}

void Smoke::updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr)
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

void Smoke::updateNodeData(char v)
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

Smoke *smoke;
void Smoke::showOut(const QString &ip, quint8 id)
{
    if(smoke == NULL)
        smoke = new Smoke(ip);
    else
        smoke->reconnect(ip, id);
    smoke->show();
}

void Smoke::cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
    Smoke *This = qobject_cast<Smoke *>((QObject *)arg);
    if(This == NULL)
        return;
    int i;
    for(i = 0; i < funcNum; i++)
    {
        if((funcList[i].funcCode == DevSmoke) && ((This->funcID == 0xFF) || (This->funcID == funcList[i].funcID)))
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

void Smoke::cbNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len)
{
    if(funcCode != DevSmoke)
        return;
    Smoke *This = qobject_cast<Smoke *>((QObject *)arg);
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

void Smoke::cbNodeGone(void *arg, unsigned short nwkAddr)
{
    Smoke *This = qobject_cast<Smoke *>((QObject *)arg);
    if(This == NULL)
        return;
    if(nwkAddr != This->curNwkAddr)
        return;
    This->curNwkAddr = 0xFFFF;
    emit This->nodeInfoChanged(0xFFFF, 0xFFFF, NULL);
}
