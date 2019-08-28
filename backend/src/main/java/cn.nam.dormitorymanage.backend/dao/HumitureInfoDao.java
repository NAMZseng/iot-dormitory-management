package cn.nam.dormitorymanage.backend.dao;

import cn.nam.dormitorymanage.backend.entity.HumitureInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 温湿度数据表DAO
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface HumitureInfoDao {

    /**
     * 添加温湿度数据，访问时间默认为系统当前时间
     *
     * @param macAddress  传感器MAC地址
     * @param temperature 温度
     * @param humidity    相对湿度
     * @return 添加成功返回1，失败返回0
     */
    int addData(@Param("macAddress") String macAddress, @Param("temperature") float temperature,
                @Param("humidity") float humidity);


    /**
     * 获取指代楼号当天的温湿度数据
     *
     * @param buildingNum 宿舍楼号
     * @param dawnTime    当天凌晨的时间
     * @return List<HumitureInfo>
     */
    List<HumitureInfo> getTodayData(@Param("buildingNum") int buildingNum, @Param("dawnTime") String dawnTime);
}
