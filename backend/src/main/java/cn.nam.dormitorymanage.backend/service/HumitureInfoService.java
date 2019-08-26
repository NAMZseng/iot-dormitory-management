package cn.nam.dormitorymanage.backend.service;

import cn.nam.dormitorymanage.backend.dao.HumitureInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Service
public class HumitureInfoService {
    @Autowired
    private HumitureInfoDao humitureInfoDao;

    /**
     * 添加温湿度数据，访问时间默认为系统当前时间
     *
     * @param macAddress 传感器MAC地址
     * @param temperature 温度
     * @param humidity 相对湿度
     * @return 添加成功返回1，失败返回0
     */
    public int addData(String macAddress, float temperature, float humidity) {
        return humitureInfoDao.addData(macAddress, temperature, humidity);
    }
}
