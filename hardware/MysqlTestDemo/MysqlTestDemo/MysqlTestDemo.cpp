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
#include <string.h>
#include <iostream>

#pragma comment(lib,"libmysql.lib")
#pragma warning(disable:4996)
using namespace std;

const char user[] = "easy"; //root
const char pswd[] = "root"; //mysqlRoot.50
const char host[] = "127.0.0.1"; //49.232.57.160
const char db_name[] = "android"; //student_test
unsigned int port = 3306;

//const char user[] = "root"; //root
//const char pswd[] = ""; //
//const char host[] = "49.232.57.160"; //49.232.57.160
//const char db_name[] = "student_test"; //student_test
//unsigned int port = 3306;


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
void Test_Write_In_Mysql(MYSQL &myCont, char *no)
{
	char sql[105] = "insert into card(UID) values(\'";
	strcat_s(sql, no);
	strcat_s(sql, "\')\0");
	if (!mysql_query(&myCont, sql)) {
		cout << "插入成功" << endl;
	}
	else {
		cout << "插入失败" << endl;
		cout << mysql_error(&myCont) << endl;
	}
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
	BOOL bReadStat;

	while (1)
	{
		PurgeComm(hCom, PURGE_TXCLEAR |    //清空缓冲区
			PURGE_RXCLEAR);
		bReadStat = ReadFile(hCom, str, 4, &wCount, NULL);
		if (!bReadStat) {
			printf("读取串口失败!");
			exit(0);
		}
		else {
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
				printf("已检测到学生学号：%s, 状态：%d\n", no, tar);
				char sql[255] = "select * from student where no = \'";
				strcat(sql, no);
				strcat(sql, "\'");
				cout << sql << endl;
				res = mysql_query(&myCont, sql);                    //查询
				if (!res)
				{
					result = mysql_store_result(&myCont);           //把查询的数据从服务器端取到客户端，然后缓存起来，放在句柄mysql里面
					if (result)
					{
						sql_row = mysql_fetch_row(result);          //将查询结构保存在一个数组里
						if (sql_row)                                //有该学生
						{
							cout << "该学生是本宿舍学生，通过" << endl;
							if (strcmp(sql_row[2], "0") == 0) {     //状态为0要更新为1
								strcpy(sql, "update student set status=1 where no=");
								strcat(sql, sql_row[0]);

								if (!mysql_query(&myCont, sql)) {
									cout << "该学生进入宿舍" << endl;
								}
								else {
									cout << "shibai1" << endl;
									cout << mysql_error(&myCont) << endl;
								}
							}
							else if (strcmp(sql_row[2], "1") == 0) { //状态为1要更新成0
								strcpy(sql, "update student set status=0 where no=");
								strcat(sql, sql_row[0]);

								if (!mysql_query(&myCont, sql)) {
									cout << "该学生离开宿舍" << endl;
								}
								else {
									cout << "shibai2" << endl;
									cout << mysql_error(&myCont) << endl;
								}
							}
						}
						else                                         //无该学生
							cout << "该学生不是本宿舍学生，警报" << endl;
					}
				}
				else
				{
					cout << "error：查询数据库失败" << endl;
				}
			}
			else {
				cout << "error：读卡失误请重新放置卡片" << endl;
			}
		}
		Sleep(500);
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

