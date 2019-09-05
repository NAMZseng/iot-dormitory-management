import json
import urllib
import urllib.request
from socket import *
import threading
import time


HOST = ''
PORT = 8888
BUFSIZ = 1024
ADDR = (HOST, PORT)

tcpCliSock = 0

# 服务器发回的状态码
stats = -1
# 发给arm板的命令码

flag = False
order = 0

def function():

    while True:  # 无限循环读取数据
        time.sleep(3) #间隔3s采集一次
        resp = urllib.request.urlopen('http://192.168.0.1/cgi-bin/node.cgi')
        HardwareData = json.loads(resp.read())
        for counter in HardwareData:
            if counter['macAddr'] == '9C625305004B1200' or counter['macAddr'] == 'E0DE5305004B1200':
                print(counter['macAddr'])
                temperature = counter['funcList'][0]
                humidity = counter['funcList'][1]
                print("温度为：", temperature['data'])
                print("湿度为：", humidity['data'])
                global flag
                global order
                order = 0
                if temperature['data'] > 30:
                    flag = True
                    order = 1
                elif temperature['data'] < 29:
                    flag = True
                    order = 2

                if humidity['data'] > 70:
                    flag = True
                    order = order + 4
                elif humidity['data'] < 60:
                    flag = True
                    order = order + 8
            phyAddress = counter['macAddr']

            if phyAddress == '9C625305004B1200' or phyAddress == 'E0DE5305004B1200':
                # 提交到服务器数据
                url = 'http://49.232.57.160:8080/DormitoryManage/humiture/addData?'
                headers = {
                    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.146 Safari/537.36'
                }
                data = {
                    'macAddr': phyAddress,
                    'temperature': temperature['data'],
                    'humidity': humidity['data']
                }
                postdata = urllib.parse.urlencode(data).encode('utf-8')
                req = urllib.request.Request(url=url, data=postdata, headers=headers)
                res = urllib.request.urlopen(req)
                page_source = res.read().decode('gbk')

                print(page_source)


def instance():
    global flag
    global order
    tcpSerSock = socket(AF_INET, SOCK_STREAM)
    tcpSerSock.bind(ADDR)
    tcpSerSock.listen(5)
    print('waiting for connection...')
    tcpCliSock, addr = tcpSerSock.accept()
    print('...connnecting from:', addr)
    while True:
        while True:
            if flag == True:
                break
        tcpCliSock.send(str(order).encode())
        flag = False

if __name__ == '__main__':
    thread02 = threading.Thread(target=instance, args=())
    thread02.start()
    thread01 = threading.Thread(target=function, args=())
    thread01.start()


