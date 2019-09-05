# -------------------------------------------------
# Project created by QtCreator 2011-08-17T22:26:30
# -------------------------------------------------
QT += core \
    gui
TARGET = TopoDisplaySystem
TEMPLATE = app
INCLUDEPATH += ../TopoDisplaySystem/include \
    ../TopoDisplaySystem/nodeman

LIBS += -L../TopoDisplaySystem/lib -lwsncomm

SOURCES += main.cpp \
    widget.cpp \
    ../TopoDisplaySystem/nodeman/topologywidget.cpp \
    ../TopoDisplaySystem/nodeman/secure.cpp \
    ../TopoDisplaySystem/nodeman/temphumi.cpp \
    ../TopoDisplaySystem/nodeman/light.cpp \
    ../TopoDisplaySystem/nodeman/rain.cpp \
    ../TopoDisplaySystem/nodeman/distance.cpp \
    ../TopoDisplaySystem/nodeman/smoke.cpp \
    ../TopoDisplaySystem/nodeman/gas.cpp \
    ../TopoDisplaySystem/nodeman/voice.cpp \
    ../TopoDisplaySystem/nodeman/fire.cpp \
    ../TopoDisplaySystem/nodeman/excute.cpp \
    ../TopoDisplaySystem/nodeman/iccard.cpp \
    ../TopoDisplaySystem/nodeman/remoter.cpp
HEADERS += widget.h \
    ../TopoDisplaySystem/nodeman/topologywidget.h \
    ../TopoDisplaySystem/nodeman/secure.h \
    ../TopoDisplaySystem/nodeman/temphumi.h \
    ../TopoDisplaySystem/nodeman/light.h \
    ../TopoDisplaySystem/nodeman/rain.h \
    ../TopoDisplaySystem/nodeman/distance.h \
    ../TopoDisplaySystem/nodeman/smoke.h \
    ../TopoDisplaySystem/nodeman/gas.h \
    ../TopoDisplaySystem/nodeman/voice.h \
    ../TopoDisplaySystem/nodeman/fire.h \
    ../TopoDisplaySystem/nodeman/excute.h \
    ../TopoDisplaySystem/nodeman/iccard.h \
    ../TopoDisplaySystem/nodeman/remoter.h
FORMS += widget.ui \
    ../TopoDisplaySystem/nodeman/topologywidget.ui \
    ../TopoDisplaySystem/nodeman/secure.ui \
    ../TopoDisplaySystem/nodeman/temphumi.ui \
    ../TopoDisplaySystem/nodeman/light.ui \
    ../TopoDisplaySystem/nodeman/rain.ui \
    ../TopoDisplaySystem/nodeman/distance.ui \
    ../TopoDisplaySystem/nodeman/smoke.ui \
    ../TopoDisplaySystem/nodeman/gas.ui \
    ../TopoDisplaySystem/nodeman/voice.ui \
    ../TopoDisplaySystem/nodeman/fire.ui \
    ../TopoDisplaySystem/nodeman/excute.ui \
    ../TopoDisplaySystem/nodeman/remoter.ui \
    ../TopoDisplaySystem/nodeman/iccard.ui
RESOURCES += resource.qrc
