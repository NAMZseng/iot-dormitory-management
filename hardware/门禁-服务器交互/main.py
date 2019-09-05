import serial
import urllib.parse
import urllib.request
from time import sleep

def get_page(buildingnum, cardnum):
    url = 'http://49.232.57.160:8080/DormitoryManage/access/judgeAccess'
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.146 Safari/537.36'
    }
    data = {
        'buildingNum': buildingnum,
        'cardNum': cardnum
    }
    postdata = urllib.parse.urlencode(data).encode('utf-8')
    req = urllib.request.Request(url=url, data=postdata, headers=headers)
    res = urllib.request.urlopen(req)
    page_source = res.read().decode('gbk')
    return page_source


def recv(serial):
    while True:
        data = serial.read_all()
        if data == '':
            continue
        else:
            break
        sleep(0.02)
    return data

def getData(data):
    val = 0
    for i in range(0, 4):
        c = int(str(data[i]))
        if c < 10:
            val *= 10
        if c >= 10 and c < 100:
            val *= 100
        if c >= 100:
            val *= 1000
        val += c
    return  val

def chooseData(data):
    """
    A: 233721100
    B: 172432528
    C: 16912536
    D: 1351113628

    :param data:
    :return:
    """
    # Acard
    if data == 233721100 or 233721196 or 233721228 or 2331361228 or 2332001100 or 2332001228:
        return 233721100
    # Bcard
    if data == 172432528 or 172432560 or 172435728 or 492432528 or 1724315328:
        return 172432528
    # Ccard
    if data == 16912536 or 144912536 or 144925336 or 169125100 or 169253100 or 169253164 or 489253164 or 1449253164 or 1613725368 or 16137253100 or 16137253164:
        return 16912536
    # Dcard
    if data == 1351113628 or 1991113628 or 1991120028 or 13513913628 or 13513920028 or 13513920060 or 19913920028:
        return 1351113628

if __name__ == '__main__':
    serial = serial.Serial('COM1', 4800, timeout=0.5)  # /dev/ttyUSB0
    if serial.isOpen():
        print("open success")
    else:
        print("open failed")
    buildingnum = "10";
    var = ""
    while True:
        sleep(0.1)
        data = recv(serial)
        ans = 0
        if data != b'':
            stats = get_page(buildingnum, chooseData(getData(data)))
            if stats == "-1":    # 检测到非法人员进入
                print(stats)
                serial.write(1)  # 数据写回




