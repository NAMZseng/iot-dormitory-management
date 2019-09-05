import json
import time
import urllib
import pymysql
import urllib.parse
import urllib.request
log = 0  # 设一个log变量用于记录单次接收次数
db = pymysql.connect("localhost", "easy", "root", "android")  # 打开数据库，配置数据库
cursor = db.cursor()  # 数据库操作
cursor.execute("DROP TABLE IF EXISTS Monitor_Data")  # 如果存在表则重新创建
creatTab = """CREATE TABLE Monitor_Data( # 创建表
    LogId INT NOT NULL,
    PhyAddress CHAR(20),
    Time CHAR(50)  NOT NULL,
    Temperature FLOAT,
    Humidity FLOAT
    )"""
cursor.execute(creatTab)  # 执行数据库语句

while True:  # 无限循环读取数据
    resp = urllib.request.urlopen('http://192.168.0.1/cgi-bin/node.cgi')
    HardwareData = json.loads(resp.read())
    for counter in HardwareData:
        print(counter['macAddr'])
        if counter['macAddr'] == '9D635305004B1200':
            print("此为协调器")
        elif counter['macAddr'] == 'DD625305004B1200':
            print("此为路由1")
        elif counter['macAddr'] == '03DD5305004B1200':
            print("此为路由2")
        elif counter['macAddr'] == '9C625305004B1200' or counter['macAddr'] == 'E0DE5305004B1200':
            temperature = counter['funcList'][0]
            humidity = counter['funcList'][1]
            print("温度为：", temperature['data'])
            print("湿度为：", humidity['data'])
        # elif counter['macAddr'] == '0BE05305004B1200':
        #     Control = counter['funcList'][1]
        #     print("控制节点", Control['data'])
        else:
            print("waiting...")
        localtime = time.asctime(time.localtime(time.time()))  # time包操作，打印本地时间
        local_time = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())  # 规整本地时间的格式
        phyAddress = counter['macAddr']
        log += 1  # 传输次数记录+1
        try:
            Temperature = temperature['data']  # 分类取有效数据
            Humidity = humidity['data']
            sql = "INSERT INTO Monitor_Data(LogId,phyAddress,Time,Temperature,Humidity)VALUES('%d','%s','%s','%f','%f')" % (
                log, phyAddress, local_time, Temperature, Humidity)  # 存入数据库
            cursor.execute(sql)  # 执行数据库语句
            db.commit()  # 提交
        except  NameError:
            print("waiting...")
        time.sleep(2)
cursor.close()
db.close()