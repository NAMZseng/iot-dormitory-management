#include "excute.h"
#include "ui_excute.h"
#include <QDesktopWidget>
#include <QDebug>
#include <stdio.h>
#include <QTextCodec>
#include <iostream>

Excute::Excute(const QString &ip, quint8 id, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Excute),
    funcID(id),
    wsnSrvUser(NULL),
    curNwkAddr(0xFFFF)
{
    ui->setupUi(this);
    setWindowFlags(windowFlags() & ~Qt::WindowMinMaxButtonsHint);
    this->setGeometry(QApplication::desktop()->width()/5*4-85,QApplication::desktop()->height()/2-80+15
                      ,this->width(),this->height());

    connect(this, SIGNAL(nodeInfoChanged(unsigned short,unsigned short,unsigned char*)), this, SLOT(updateNodeInfo(unsigned short,unsigned short,unsigned char*)));
    connect(this, SIGNAL(gotNewNodeData(char)), this, SLOT(updateNodeData(char)));
    reconnect(ip, id);
}

Excute::~Excute()
{
    delete ui;
}

void Excute::reconnect(const QString &ip, quint8 id)
{

    wsnSrvIp = ip;
    funcID = id;
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                    NULL, cbNewFunc, cbNewData, cbNodeGone,
                    NULL, (void *)this);

    WSNCOMM_NODE *node = wsncomm_getNode_byType(ip.toUtf8().constData(), DevExecuteB, funcID);
    if(node != NULL)
    {
        curNwkAddr = node->nwkAddr;
        updateNodeInfo(node->nwkAddr, node->parAddr, node->hwAddr);
        char *data = NULL;
        int len = wsncomm_getNodeData_byType(ip.toUtf8().constData(), DevExecuteB, funcID, &data);
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
    QString title = QTextCodec::codecForName("UTF-8")->toUnicode(wsncomm_find_nodeTypeString(DevExecuteB));
    title += " - ";
    if(funcID != 0xFF)
        title += QString::number(funcID);
    else
        title += tr("Any");
    this->setWindowTitle(title);
}

void Excute::showEvent(QShowEvent *)
{
    if(wsnSrvUser == NULL)
    {
        wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                        NULL, cbNewFunc, cbNewData, cbNodeGone,
                        NULL, (void *)this);
    }
}

void Excute::hideEvent(QHideEvent *)
{
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = NULL;
}



void Excute::updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr)
{
    if(nwkAddr == 0xFFFF)
    {
        ui->nwkAddrEdit->setText(tr("Invalid"));
        ui->parAddrEdit->setText(tr("Invalid"));
        ui->hwAddrEdit->setText(tr("Invalid"));
        ui->currentStatus->setEnabled(false);
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
        ui->currentStatus->setEnabled(true);
        ui->setting->setEnabled(true);
    }


}

void Excute::updateNodeData(char v)
{
    ui->auStatus->setChecked(v & EXE_AU);
    ui->adStatus->setChecked(v & EXE_AD);
    ui->buStatus->setChecked(v & EXE_BU);
    ui->bdStatus->setChecked(v & EXE_BD);

}

void Excute::on_applyBtn_clicked()
{
    //printf("in\n");
    char v = 0;
    if(ui->auSetting->isChecked())
        v |= EXE_AU;
    if(ui->adSetting->isChecked())
        v |= EXE_AD;
    if(ui->buSetting->isChecked())
        v |= EXE_BU;
    if(ui->bdSetting->isChecked())
        v |= EXE_BD;
    //printf("%s, %d, %d, %d\n", this->wsnSrvIp.toUtf8().constData(), DevExecuteB, funcID, v);
    wsncomm_sendNode_byType(this->wsnSrvIp.toUtf8().constData(), DevExecuteB, funcID, &v, 1);

}

Excute *exe;
void Excute::showOut(const QString &ip, quint8 id)
{
    if(exe == NULL)
        exe = new Excute(ip, id);
    else
        exe->reconnect(ip, id);
    exe->show();
}

void Excute::cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
    Excute *This = qobject_cast<Excute *>((QObject *)arg);
    if(This == NULL)
        return;
    int i;
    for(i = 0; i < funcNum; i++)
    {
        if((funcList[i].funcCode == DevExecuteB) && ((This->funcID == 0xFF) || (This->funcID == funcList[i].funcID)))
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

void Excute::cbNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len)
{
    Q_UNUSED(endPoint);Q_UNUSED(len);
    if(funcCode != DevExecuteB)
        return;
    Excute *This = qobject_cast<Excute *>((QObject *)arg);
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

void Excute::cbNodeGone(void *arg, unsigned short nwkAddr)
{
    Excute *This = qobject_cast<Excute *>((QObject *)arg);
    if(This == NULL)
        return;
    if(nwkAddr != This->curNwkAddr)
        return;
    This->curNwkAddr = 0xFFFF;
    emit This->nodeInfoChanged(0xFFFF, 0xFFFF, NULL);
}
