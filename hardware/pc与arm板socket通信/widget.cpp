#include "widget.h"
#include "ui_widget.h"
#include "topologywidget.h"
#include "secure.h"
#include "temphumi.h"
#include "light.h"
#include "rain.h"
#include "distance.h"
#include "smoke.h"
#include "gas.h"
#include "voice.h"
#include "fire.h"
#include "excute.h"
#include "iccard.h"
#include "remoter.h"
#include <QDebug>

//构造函数
Widget::Widget(const QString &ip, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Widget),
    wsnSrvIp(ip)
{
    ui->setupUi(this);
    TopologyWidget::SetTopologyArea(ip, ui->topologyArea);
    TopologyWidget::UpdateTopologyArea(ui->topologyArea);
    connect(TopologyWidget::GetTopologyArea(ui->topologyArea), SIGNAL(nodeClicked(const WSNCOMM_NODE*)), this, SLOT(onNodeClicked(const WSNCOMM_NODE*)));
}

//稀构函数
Widget::~Widget()
{
    delete ui;
}

void Widget::onNodeClicked(const WSNCOMM_NODE *node)
{
    if(node->funcNum <= 0)
        return;
    switch(node->funcInfo[0].funcCode)
    {
    case DevIRPers:
        Secure::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevTemp://点击的是温度节点
        if((node->funcNum > 1) && (node->funcInfo[1].funcCode == DevHumm))
            TempHumi::showOut(wsnSrvIp, node->funcInfo[0].funcID, node->funcInfo[1].funcID);
        else
            TempHumi::showOut(wsnSrvIp, node->funcInfo[0].funcID, 254);
        break;
    case DevHumm://点击的是湿度节点
        if((node->funcNum > 1) && (node->funcInfo[1].funcCode == DevTemp))
            TempHumi::showOut(wsnSrvIp, node->funcInfo[1].funcID, node->funcInfo[0].funcID);
        else
            TempHumi::showOut(wsnSrvIp, 254, node->funcInfo[0].funcID);
        break;
    case DevILLum:
        Light::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevRain:
        Rain::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevIRDist:
        Distance::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevSmoke:
        Smoke::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevGas:
        Gas::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevVoice:
        Voice::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevFire:
        Fire::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevExecuteB:
        Excute::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case Dev125kReader:
        ICCard::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    case DevRemoter:
        Remoter::showOut(wsnSrvIp, node->funcInfo[0].funcID);
        break;
    }
}

void Widget::on_pushButton_clicked()
{
    TopologyWidget::UpdateTopologyArea(ui->topologyArea);
}
