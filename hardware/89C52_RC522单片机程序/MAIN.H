//////////////////////////////////
//端口定义
/////////////////////////////////////////////////////////////////////
//MFRC522
sbit     MF522_NSS  =    P3^2;
sbit     MF522_SCK  =    P3^3;
sbit     MF522_SI   =    P3^4;
sbit     MF522_SO   =    P3^5;
sbit     MF522_RST  =    P3^6;                   //RC500片选

sbit     ff         =    P3^7;                   //检测灯，当寻卡成功后闪烁
sbit     led        =    P3^0;                   //报警
/////////////////////////////////////////////////////////////////////
//函数原型
/////////////////////////////////////////////////////////////////////
void InitializeSystem();                                                                   
#define OSC_FREQ          22118400L
#define  RCAP2_50us      65536L - OSC_FREQ/40417L
#define  RCAP2_1ms       65536L - OSC_FREQ/2000L
#define  RCAP2_10ms      65536L - OSC_FREQ/1200L
#define  TIME0_500us     65536L - OSC_FREQ/8000L
#define  TIME0_10ms      65536L - OSC_FREQ/200
#define CALL_isr_UART()         TI = 1
#define TRUE 1
#define FALSE 0

