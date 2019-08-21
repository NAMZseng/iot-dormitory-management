/*客户端
**SocketClient.cpp
**2019/8/21
**Author:杨益泽（改）
**注：由于注重解释学习，所以请忽略该代码的格式
*/
#include <WINSOCK2.H>   
#include<iostream>
using namespace std;

#pragma comment(lib, "ws2_32.lib")      

int main()
{
	WSADATA wsaData;
	WSAStartup(0x0202, &wsaData);//申请Windows Sockets API支援
	SOCKET clientSocket;//创建并初始化套接字
	clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	SOCKADDR_IN server;//填充服务器地址信息
	server.sin_family = PF_INET;
	server.sin_port = htons(8888);
	server.sin_addr.s_addr = inet_addr("192.168.31.29");
	//过了一遍服务器的代码，上面肯定就看得懂了。不懂，就回头再看看吧。

	//连接服务器   
	connect(clientSocket, (struct sockaddr *) &server, sizeof(server));
	/*
	作用:连接后可以用clientSocket来使用这个连接
	用法:要记录的套接字，保存了远程地址信息的server，地址大小。
	P.S：跟服务器里的recv()几乎相同。
	*/

	//发送信息
	while (TRUE) {
		char Msg[128];//定义发送缓存区
		cout << "Send:"; cin >> Msg;//输入要发送的字节
		send(clientSocket, Msg, strlen(Msg), 0);
		/*
		作用：snd()从发送缓冲区拷贝数据。
		用法：前三个请找上述的参数定义。最后一个参数为0的意义：默认是普通接受数据模式
		*/
	}

	//结束客户端;      
	closesocket(clientSocket);
	WSACleanup();
	return 0;
}
