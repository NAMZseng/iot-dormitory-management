#ifndef WIDGET_H
#define WIDGET_H

#include <QWidget>
#include "topologywidget.h"

namespace Ui {
    class Widget;
}

class Widget : public QWidget
{
    Q_OBJECT

public:
    explicit Widget(const QString &ip, QWidget *parent = 0);
    ~Widget();

private:
    Ui::Widget *ui;
    QString wsnSrvIp;

private slots:
    void onNodeClicked(const WSNCOMM_NODE *node);
    void on_pushButton_clicked();
};

#endif // WIDGET_H
