#include "remoter.h"
#include "ui_remoter.h"
#include <QDesktopWidget>
#include <QDebug>
#include <stdio.h>
#include <QTextCodec>

Remoter::Remoter(const QString &ip, quint8 id, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Remoter),
    funcID(id),
    wsnSrvUser(NULL),
    curNwkAddr(0xFFFF)
{
    ui->setupUi(this);
    setWindowFlags(windowFlags() & ~Qt::WindowMinMaxButtonsHint);
    this->setGeometry(QApplication::desktop()->width()/5*4-85,QApplication::desktop()->height()/2-80+15
                      ,this->width(),this->height());

    connect(this, SIGNAL(nodeInfoChanged(unsigned short,unsigned short,unsigned char*)), this, SLOT(updateNodeInfo(unsigned short,unsigned short,unsigned char*)));
    connect(this, SIGNAL(gotNewNodeData(QByteArray*)), this, SLOT(updateNodeData(QByteArray*)));
    reconnect(ip, id);
}

Remoter::~Remoter()
{
    delete ui;
}

void Remoter::reconnect(const QString &ip, quint8 id)
{

    wsnSrvIp = ip;
    funcID = id;
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                NULL, cbNewFunc, cbNewData, cbNodeGone,
                NULL, (void *)this);

    WSNCOMM_NODE *node = wsncomm_getNode_byType(ip.toUtf8().constData(), DevRemoter, funcID);
    if(node != NULL)
    {
        curNwkAddr = node->nwkAddr;
        updateNodeInfo(node->nwkAddr, node->parAddr, node->hwAddr);
        wsncomm_delete_node(node);
    }
    else
    {
        curNwkAddr = 0xFFFF;
        updateNodeInfo(0xFFFF, 0xFFFF, NULL);
    }
    QString title = QTextCodec::codecForName("UTF-8")->toUnicode(wsncomm_find_nodeTypeString(DevRemoter));
    title += " - ";
    if(funcID != 0xFF)
        title += QString::number(funcID);
    else
        title += tr("Any");
    this->setWindowTitle(title);
}

void Remoter::updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char hwAddr[8])
{
    if(nwkAddr == 0xFFFF)
    {
        ui->nwkAddrEdit->setText(tr("Invalid"));
        ui->parAddrEdit->setText(tr("Invalid"));
        ui->hwAddrEdit->setText(tr("Invalid"));
        ui->valueEdit->setText(tr("Invalid"));
        ui->setting->setEnabled(false);
    }
    else
    {
        char tmpString[20];
        sprintf(tmpString, "%04X", nwkAddr);
        ui->nwkAddrEdit->setText(tmpString);
        sprintf(tmpString, "%04X", parAddr);
        ui->parAddrEdit->setText(tmpString);
        ui->hwAddrEdit->setText(QByteArray((char *)hwAddr, 8).toHex());
        ui->valueEdit->setText("");
        ui->setting->setEnabled(true);
    }
}

void Remoter::updateNodeData(QByteArray *v)
{
    if(v == NULL)
    {
        ui->valueEdit->setText(tr("Invalid"));
    }
    else
    {
        ui->valueEdit->setText(v->toHex());
        delete v;
    }
}

void Remoter::on_applyBtn_clicked()
{
    if(ui->codeEdit->text().isEmpty())
        on_randDataBtn_clicked();
    QByteArray v;
    v.append(quint8(ui->codeLength->value() << 3));
    v.append(QByteArray::fromHex(ui->codeEdit->text().toAscii()));
    wsncomm_sendNode_byType(this->wsnSrvIp.toUtf8().constData(), DevRemoter, funcID, v.constData(), v.size());
}

void Remoter::showEvent(QShowEvent *)
{
    if(wsnSrvUser == NULL)
    {
        wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                    NULL, cbNewFunc, cbNewData, cbNodeGone,
                    NULL, (void *)this);
    }
}

void Remoter::hideEvent(QHideEvent *)
{
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = NULL;
}

Remoter *r;
void Remoter::showOut(const QString &ip, quint8 id)
{
    if(r == NULL)
        r = new Remoter(ip, id);
    else
        r->reconnect(ip, id);
    r->show();
}

void Remoter::cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
    Remoter *This = qobject_cast<Remoter *>((QObject *)arg);
    if(This == NULL)
        return;
    int i;
    for(i = 0; i < funcNum; i++)
    {
        if((funcList[i].funcCode == DevRemoter) && ((This->funcID == 0xFF) || (This->funcID == funcList[i].funcID)))
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

void Remoter::cbNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len)
{
    Q_UNUSED(endPoint);Q_UNUSED(len);
    if(funcCode != DevRemoter)
        return;
    Remoter *This = qobject_cast<Remoter *>((QObject *)arg);
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
    QByteArray *v = new QByteArray(data, len);
    emit This->gotNewNodeData(v);
}

void Remoter::cbNodeGone(void *arg, unsigned short nwkAddr)
{
    Remoter *This = qobject_cast<Remoter *>((QObject *)arg);
    if(This == NULL)
        return;
    if(nwkAddr != This->curNwkAddr)
        return;
    This->curNwkAddr = 0xFFFF;
    emit This->nodeInfoChanged(0xFFFF, 0xFFFF, NULL);
}

void Remoter::on_codeLenAddBtn_clicked()
{
    ui->codeLength->setValue(ui->codeLength->value() + 1);
}

void Remoter::on_codeLenReduceBtn_clicked()
{
    ui->codeLength->setValue(ui->codeLength->value() - 1);
}

void Remoter::on_randDataBtn_clicked()
{
    int len = ui->codeLength->value();
    QByteArray a;
    while(len--)
    {
        a.append(rand() & 0xFF);
    }
    ui->codeEdit->setText(a.toHex());
}
