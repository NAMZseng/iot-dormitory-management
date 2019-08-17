// MysqlTestDemo.cpp : 此文件包含 "main" 函数。程序执行将在此处开始并结束。
//
//mysql -h49.232.57.160 -uroot -pmysqlRoot.50
#include "pch.h"
#include <windows.h>
#include <mysql.h>
#include <string>
#include <string.h>
#include <iostream>

using namespace std;

#pragma comment(lib,"libmysql.lib")

//const char user[] = "easy"; //root
//const char pswd[] = "root"; //mysqlRoot.50
//const char host[] = "127.0.0.1"; //49.232.57.160
//const char db_name[] = "android"; //student_test

const char user[] = "root"; //root
const char pswd[] = "mysqlRoot.50"; //mysqlRoot.50
const char host[] = "49.232.57.160"; //49.232.57.160
const char db_name[] = "student_test"; //student_test

unsigned int port = 3306;
HANDLE hCom;

int main()
{
	MYSQL myCont;
	MYSQL_RES *result = NULL;
	//MYSQL_ROW sql_row;
	int res = 0;
	mysql_init(&myCont);
	if (mysql_real_connect(&myCont, host, user, pswd, db_name, port, NULL, 0))
	{
		mysql_query(&myCont, "SET NAMES UTF8"); //设置编码格式
		/*
		res = mysql_query(&myCont, "select * from card");//查询
		if (!res)
		{
			result = mysql_store_result(&myCont);
			cout << "结果集数量:" << mysql_num_rows(result) << endl;
			if (result)
			{
				while (sql_row = mysql_fetch_row(result))//获取具体的数据
				{
					cout << "ID:" << *sql_row << endl;
				}
				if (!mysql_query(&myCont, "insert into card(UID) values('15200')")) {
					cout << "插入成功" << endl;
				}
				else {
					cout << "shibai" << endl;
					cout << mysql_error(&myCont) << endl;
				}
			}
			
		}
		else
		{
			cout << "query sql failed!" << endl;
		}*/
		cout << "链接数据库成功" << endl;
		system("pause");
		//串口操作
		hCom = CreateFile(TEXT("COM2"),//COM2口
			GENERIC_READ | GENERIC_WRITE, //允许读、写 
			0, //独占方式
			NULL,
			OPEN_EXISTING, //打开而不是创建
			0, //同步方式
			NULL);
		if (hCom == (HANDLE)-1)
		{
			printf("打开COM失败!\n");
			return FALSE;
		}
		else
		{
			printf("COM打开成功！\n");
		}
		SetupComm(hCom, 1024, 1024); //输入缓冲区和输出缓冲区的大小都是1024
		COMMTIMEOUTS TimeOuts;
		//设定读超时
		TimeOuts.ReadIntervalTimeout = 1000;
		TimeOuts.ReadTotalTimeoutMultiplier = 5000;
		TimeOuts.ReadTotalTimeoutConstant = 5000;
		//设定写超时
		TimeOuts.WriteTotalTimeoutMultiplier = 5000;
		TimeOuts.WriteTotalTimeoutConstant = 2000;
		SetCommTimeouts(hCom, &TimeOuts); //设置超时
		DCB dcb;
		GetCommState(hCom, &dcb);
		dcb.BaudRate = 4800; //波特率为4800 
		dcb.ByteSize = 8; //每个字节有8位
		dcb.Parity = NOPARITY; //无奇偶校验位
		dcb.StopBits = ONE5STOPBITS; //1个停止位

		SetCommState(hCom, &dcb);
		DWORD wCount = 5;//读取的字节数
		BOOL bReadStat;

		//DWORD dwWriteLen = 0; //写入长度 
		//if (!WriteFile(hCom, "A", 2, &dwWriteLen, NULL)) //向串口发送一个A 
		//{
		//	printf("串口发送数据失败！\n");
		//}
		//printf("串口发送数据成功！\n");
		while (1)
		{
			PurgeComm(hCom, PURGE_TXCLEAR | PURGE_RXCLEAR); //清空缓冲区
			char str[13] = { 0 };
			printf("%s\n", str);
			bReadStat = ReadFile(hCom, str, 13, &wCount, NULL);
			if (!bReadStat)
			{
				printf("读串口失败!");
				return FALSE;
			}
			else
			{
				string info;
				str[5] = '\0';
				for (int i = 0; str[i] != '\0'; i++) {
					int int_val = ((int)str[i] + 2000 ) % 3977;
					string str_val = to_string(int_val);
					info += str_val;
					//printf("%02x ", str[i]);
				}
				//cout << info << endl;
				//写入数据库
				char sql[105] = "insert into card(UID) values(\'";
				char temp[101];
				int k = 0;
				for (k = 0; k < info.length(); k++)
					temp[k] = info[k];
				temp[k] = '\0';
				strcat_s(sql, temp);
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
			printf("\n");
			Sleep(50);
		}
	}
	else
	{
		cout << "连接数据库失败" << endl;
		cout << mysql_error(&myCont) << endl; //打印错误原因
	}

	mysql_close(&myCont);
	system("pause");

	

	return 0;
}

