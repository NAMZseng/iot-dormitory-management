#include <stdio.h>
#include <string.h>
#include <string>
#include <windows.h>
#pragma warning(disable:4996)
using namespace std;

unsigned char str[5];
char no[22];
HANDLE hCom;

int main()
{
	hCom = CreateFile(TEXT("COM1"),      //编辑打开的串口
		GENERIC_READ | GENERIC_WRITE,    //允许读写
		0,                               //独占方式
		NULL,
		OPEN_EXISTING,                   //打开而不是创建
		0,                               //同步方式
		NULL);
	if (hCom == (HANDLE)-1) {
		printf("打开COM失败\n");
		return FALSE;
	} else {
		printf("打开COM成功\n");
	}
	SetupComm(hCom, 1024, 1024);          //输入缓冲区与输出缓冲区大小都是1024
	COMMTIMEOUTS TimeOuts;
	//设定读超时
	TimeOuts.ReadIntervalTimeout = 1000;
	TimeOuts.ReadTotalTimeoutMultiplier = 5000;
	TimeOuts.ReadTotalTimeoutConstant = 5000;
	//设定写超时
	TimeOuts.WriteTotalTimeoutMultiplier = 5000;
	TimeOuts.WriteTotalTimeoutConstant = 2000;
	SetCommTimeouts(hCom, &TimeOuts);
	DCB dcb;
	GetCommState(hCom, &dcb);
	dcb.BaudRate = 4800;                   //波特率设置为4800
	dcb.ByteSize = 8;                      //每字节8位
	dcb.Parity = NOPARITY;                 //无奇偶校验位
	dcb.StopBits = ONE5STOPBITS;           //1个停止位
	SetCommState(hCom, &dcb);
	DWORD wCount = 4;                      //读取的字节数 
	BOOL bReadStat;

	while (1)
	{
		PurgeComm(hCom, PURGE_TXCLEAR |    //清空缓冲区
						PURGE_RXCLEAR);
		bReadStat = ReadFile(hCom, str, 4, &wCount, NULL);
		if (!bReadStat) {
			printf("读取串口失败!");
			return FALSE;
		} else {
			//数据处理
			str[4] = '\0';
			int tar = (int)str[0];         //卡号的首位位标记位
			bool flag = true;              //防止误读标记
			if (tar == 144 || tar == 16) {
				strcpy(no, "1607094270");  //A card
			}
			else if (tar == 17 || tar == 49) {
				strcpy(no, "1607094271");  //B card
			}
			else if (tar == 135 || tar == 199) {
				strcpy(no, "1607094272");  //C card
			}
			else if (tar == 233) {
				strcpy(no, "1607094273");  //D card
			}
			else {
				flag = false;              //未检测到数据
			}

			if (flag) {
				printf("已检测到学生学号：%s, 状态：%d", no, tar);
			}
			else {
				printf("error：读卡失误请重新放置卡片");
			}
		}
		printf("\n");
		Sleep(500);
	}
}
