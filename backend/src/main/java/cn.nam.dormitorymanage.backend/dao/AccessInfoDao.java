package cn.nam.dormitorymanage.backend.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 学生出入宿舍楼记录DAO
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface AccessInfoDao {

    /**
     * 判断刷卡学生的学号与楼号是否匹配
     *
     * @param buildingNum
     * @param studentNum
     * @return 属于该楼返回房间号，否则返回null
     */
    Integer judgeAccess(@Param("buildingNum") int buildingNum, @Param("studentNum") int studentNum);

    /**
     * 获取最近一次学生进入宿舍楼的状态，从而判断这次是进还是出
     *
     * @param buildingNum 访问楼号
     * @param studentNum 学生学号
     * @return 1/-1，若无进出记录，则返回null
     */
    Integer getPreStatus(@Param("buildingNum") int buildingNum, @Param("studentNum") int studentNum);

    /**
     * 添加被接收的访问记录，访问时间默认为系统当前时间
     *
     * @param buildingNum  访问楼号
     * @param studentNum   学生学号
     * @param accessStatus 进出状态 in/out
     * @return 添加成功返回1，失败返回0
     */
    int addAccess(@Param("buildingNum") int buildingNum, @Param("studentNum") int studentNum,
                  @Param("accessStatus") int accessStatus);

    /**
     * 添加被阻的访问记录，访问时间默认为系统当前时间
     *
     * @param buildingNum  访问楼号
     * @param studentNum   学生学号
     * @return 添加成功返回1，失败返回0
     */
    int addBlock(@Param("buildingNum") int buildingNum, @Param("studentNum") int studentNum);
}
