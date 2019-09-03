/*************************************************

Copyright:bupt

Author:杨益泽

Date:2019-08-17

Description:串口接收IC卡数据并上传数据库处理

**************************************************/
#include "pch.h"
#include <windows.h>
#include <mysql.h>
#include <stdio.h>
#include <string>
#include<time.h>
#include <string.h>
#include <iostream>

#pragma comment(lib,"libmysql.lib")
#pragma warning(disable:4996)
using namespace std;

typedef long long LL;
typedef long long Datatype;

const char user[] = "easy"; //root
const char pswd[] = "root"; //mysqlRoot.50
const char host[] = "127.0.0.1"; //49.232.57.160
const char db_name[] = "android"; //student_test
unsigned int port = 3306;

HANDLE hCom;
unsigned char str[5];
char no[22];

/*************************************************

Function:                // Test_Write_In_Mysql

Description:             // 进行数据的本地测试，经接收到的数据存储置本地数据库

Calls:                   // 

Table Accessed:          // card

Table Updated:           // card

Input:                   // myCont 数据库指针

                         // no     学生学号

Output:                  // 

Return:                  // 

Others:                  // 

*************************************************/
void Test_Write_In_Mysql(MYSQL &myCont, Datatype no, char *tim)
{
	char sql[105] = "insert into card(UID, TIME) values(\'";
	strcat_s(sql, to_string(no).c_str());
	strcat_s(sql, "\',\'");
	strcat_s(sql, tim);
	strcat_s(sql, "\')\0");
	//cout << sql << endl;
	if (!mysql_query(&myCont, sql)) {
		cout << "插入成功" << endl;
	}
	else {
		cout << "插入失败" << endl;
		cout << mysql_error(&myCont) << endl;
	}
}


int ChangeIntoInt(char *str)
{
	int ans = 0;
	for (int i = 0; i < strlen(str); i++) {
		ans = ans * 10 + (int)(str[i] - '0');
	}
	return ans;
}


/*************************************************

Function:                // Uart_Get_Data

Description:             // 处理从串口接收到的数据

Calls:                   //

Table Accessed:          // student

Table Updated:           // student

Input:                   // 

                         // 

Output:                  //

Return:                  //

Others:                  //

*************************************************/
void Uart_Get_Data(MYSQL &myCont, int &res, MYSQL_RES *result, MYSQL_ROW &sql_row)
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
		exit(0);
	}
	else {
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
	DWORD yCount = 1;                      //写入的字节数
	BOOL bReadStat;
	
	while (1)
	{
		PurgeComm(hCom, PURGE_TXCLEAR |    //清空缓冲区
			PURGE_RXCLEAR);
		bReadStat = ReadFile(hCom, str, 8, &wCount, NULL);
		if (!bReadStat) {
			printf("读取串口失败!");
			exit(0);
		}
		else {
			//数据处理
			str[4] = '\0';
			LL val = 0;
			char cstr[3];
			int len = 0;
			int tmp = 0;
			memset(cstr, 0, sizeof(cstr));
			for (int i = 0; i < 4; i++) {
				sprintf(cstr, "%d", str[i]);
				tmp = ChangeIntoInt(cstr);
				if (tmp < 10)
					val *= 10;
				else if (tmp >= 10 && tmp < 100)
					val *= 100;
				else if (tmp >= 100)
					val *= 1000;
				val += tmp;
			}
			cout << val << endl;
			time_t now_time = time(NULL);                 //获取系统时间                       
			tm *t_tm = localtime(&now_time);              //获取本地时间  
			char temp[105];
			sprintf(temp, "%s", asctime(t_tm));
			Test_Write_In_Mysql(myCont, val, temp);
		}
		Sleep(100);
	}
}


int main()
{
	MYSQL myCont;
	MYSQL_RES *result = NULL;
	MYSQL_ROW sql_row;
	int res = 0;
	mysql_init(&myCont);
	if (mysql_real_connect(&myCont, host, user, pswd, db_name, port, NULL, 0))
	{
		mysql_query(&myCont, "SET NAMES UTF8");       //设置编码格式
		cout << "链接数据库成功" << endl;
		system("pause");                            
		
		Uart_Get_Data(myCont, res, result, sql_row);  //串口检测
	}
	else
	{
		cout << "连接数据库失败" << endl;
		cout << mysql_error(&myCont) << endl;         //打印错误原因
	}
	return 0;
}

