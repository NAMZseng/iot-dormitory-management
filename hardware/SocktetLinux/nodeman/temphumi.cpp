#include "temphumi.h"
#include "ui_temphumi.h"
#include <QTextCodec>
#include <QDebug>
#include <QDesktopWidget>
#include <stdio.h>


TempHumi::TempHumi(const QString &ip, quint8 tempId, quint8 hummId, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::TempHumi),
    tempFuncID(tempId),
    hummFuncID(hummId),
    wsnSrvUser(NULL),
    curNwkAddr(0xFFFF)
{
    ui->setupUi(this);
    setWindowFlags(windowFlags() & ~Qt::WindowMinMaxButtonsHint);
    this->setGeometry(QApplication::desktop()->width()/5,40,this->width(),this->height());

    connect(this, SIGNAL(nodeInfoChanged(unsigned short,unsigned short,unsigned char*)), this, SLOT(updateNodeInfo(unsigned short,unsigned short,unsigned char*)));
    connect(this, SIGNAL(gotNewTempData(unsigned short)), this, SLOT(updateTempData(unsigned short)));
    connect(this, SIGNAL(gotNewHumData(unsigned short)), this, SLOT(updateHumData(unsigned short)));

    reconnect(ip, tempFuncID, hummFuncID);

}

TempHumi::~TempHumi()
{
    delete ui;
}
//重新链接
void TempHumi::reconnect(const QString &ip, quint8 tempId, quint8 hummId)
{

    wsnSrvIp = ip;
    tempFuncID = tempId;
    hummFuncID = hummId;
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                    NULL, cbNewFunc, cbNewData, cbNodeGone,
                    NULL, (void *)this);

    WSNCOMM_NODE *node = wsncomm_getNode_byType(ip.toUtf8().constData(), DevTemp, tempFuncID);
    if(node != NULL)
    {
        curNwkAddr = node->nwkAddr;
        updateNodeInfo(node->nwkAddr, node->parAddr, node->hwAddr);
        wsncomm_delete_node(node);
        char *data = NULL;
        int len = wsncomm_getNodeData_byType(ip.toUtf8().constData(), DevTemp, tempFuncID, &data); //这里的data保存的是温度数据
        if(data != NULL)
        {
            updateTempData(*((unsigned short *)data)); //更新温度数据
            free(data);
        }
        data = NULL;
        len = wsncomm_getNodeData_byType(ip.toUtf8().constData(), DevHumm, hummId, &data); //这里获取的是湿度数据

        if(data != NULL)
        {
            updateHumData(*((unsigned short *)data)); //更新湿度数据
            free(data);
        }
    }
    else
    {
        curNwkAddr = 0xFFFF;
        updateNodeInfo(0xFFFF, 0xFFFF, NULL);
    }
    QString title = QTextCodec::codecForName("UTF-8")->toUnicode(wsncomm_find_nodeTypeString(DevTemp));
    title += " - ";
    if(tempFuncID != 0xFF)
        title += QString::number(tempFuncID);
    else
        title += tr("Any");
    title += " - ";
    if(hummFuncID != 0xFF)
        title += QString::number(hummFuncID);
    else
        title += tr("Any");
    this->setWindowTitle(title);
}

void TempHumi::showEvent(QShowEvent *)
{
    if(wsnSrvUser == NULL)
    {
        wsnSrvUser = wsncomm_register(wsnSrvIp.toUtf8().constData(),
                        NULL, cbNewFunc, cbNewData, cbNodeGone,
                        NULL, (void *)this);
    }
}

void TempHumi::hideEvent(QHideEvent *)
{
    wsncomm_unregister(wsnSrvUser);
    wsnSrvUser = NULL;
}

void TempHumi::updateNodeInfo(unsigned short nwkAddr, unsigned short parAddr, unsigned char *hwAddr)
{
    if(nwkAddr == 0xFFFF)
    {
        ui->nwkAddrEdit->setText(tr("Invalid"));
        ui->parAddrEdit->setText(tr("Invalid"));
        ui->hwAddrEdit->setText(tr("Invalid"));
        ui->tempValueEdit->setText(tr("Invalid"));
        ui->hummValueEdit->setText(tr("Invalid"));
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


//数据转化
void change(char *num, float val)
{
        int i = 0;
        bool flag = false;
        if (val < 1 && val >= 0) {
                num[0] = '0';
                num[1] = '.';
                num[2] = (char)((int)(val*10)+'0');
                num[3] = (char)((((int)(val*100))%10)+'0');
                num[4] = '\0';
                return ;
        }
        else if (val < 0) {
                num[0] = '-';
                i = 1;
                val *= -1;
                flag = true;
        }
        int v = (int)(val * 100);
        while (v) {
                num[i] = (char)((v%10)+'0');
                v /= 10;
                i++;
        }
        int j;
        if (flag)
                j = 1;
        else
                j = 0;
        for (; j < i/2; j++) {
                char temp = num[j];
                num[j] = num[i-1-j];
                num[i-1-j] = temp;
        }
        num[i] = num[i-1];
        num[i-1] = num[i-2];
        num[i-2] = '.';
        num[i+1] = '\0';
}


//更新温度数据
void TempHumi::updateTempData(unsigned short temp)
{

    float v = float(temp) / 100;
    ui->tempValueEdit->setText(QString::number(v));
    //printf("Temp:%.2f\n", v); //在这里将数据打印


    //send
    /*
    char msg[50];
    int sockfd;
    struct sockaddr_in server_addr;

    printf("\n======================client initialization======================\n");
    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        perror("socket0");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    server_addr.sin_addr.s_addr = inet_addr(SERVER_IP);
    bzero(&(server_addr.sin_zero),sizeof(server_addr.sin_zero));

    if (::connect(sockfd, (struct sockaddr *)&server_addr,sizeof(struct sockaddr_in)) == -1){
         perror("connect error1");
         exit(1);
    }
    int len;
    //printf("Send:");
    //scanf("%s",msg);
    change(msg, v);
    len = strlen(msg);
    //sent to the server
    if(send(sockfd, msg,len,0) == -1) {
        perror("send error2");
    }
    printf("Finished:%s\n", msg);

    //::close(sockfd);
    */

}
//更新湿度数据
void TempHumi::updateHumData(unsigned short hum)
{
    float v = float(hum) / 100;
    ui->hummValueEdit->setText(QString::number(v) + "%");
    //printf("Humi:%.2f\n", v); //在这里将数据打印
}

static TempHumi *tempHumi = NULL;
//此函数的作用是将数据显示（温湿度）
void TempHumi::showOut(const QString &ip, quint8 tempId, quint8 hummId)
{
    if (tempHumi == NULL)
        tempHumi = new TempHumi(ip, tempId, hummId);
    else
        tempHumi->reconnect(ip, tempId, hummId);
    tempHumi->show();
}

void TempHumi::cbNewFunc(void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
    TempHumi *This = qobject_cast<TempHumi *>((QObject *)arg);
    if(This == NULL)
        return;
    int i;
    for(i = 0; i < funcNum; i++)
    {
        if(
            ((funcList[i].funcCode == DevTemp) && ((0xFF == This->tempFuncID) || (funcList[i].funcID == This->tempFuncID)))
          ||((funcList[i].funcCode == DevHumm) && ((0xFF == This->hummFuncID) || (funcList[i].funcID == This->hummFuncID)))
          )
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

void TempHumi::cbNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len)
{
    TempHumi *This = qobject_cast<TempHumi *>((QObject *)arg);
    if(This == NULL)
        return;
    if(
        ((funcCode == DevTemp) && ((0xFF == This->tempFuncID) || (funcID == This->tempFuncID)))
      ||((funcCode == DevHumm) && ((0xFF == This->hummFuncID) || (funcID == This->hummFuncID)))
      )
    {
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
        unsigned short v = *((unsigned short *)data);
        if(funcCode == DevTemp)
            emit This->gotNewTempData(v);
        else
            emit This->gotNewHumData(v);
    }
}

void TempHumi::cbNodeGone(void *arg, unsigned short nwkAddr)
{
    TempHumi *This = qobject_cast<TempHumi *>((QObject *)arg);
    if(This == NULL)
        return;
    if(nwkAddr != This->curNwkAddr)
        return;
    This->curNwkAddr = 0xFFFF;
    emit This->nodeInfoChanged(0xFFFF, 0xFFFF, NULL);
}
