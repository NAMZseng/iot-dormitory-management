package cn.nam.dormitorymanage.backend.dao;

import cn.nam.dormitorymanage.backend.entity.AccessCount;
import cn.nam.dormitorymanage.backend.entity.AccessInfo;
import cn.nam.dormitorymanage.backend.entity.BlockInfo;
import cn.nam.dormitorymanage.backend.entity.StudentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生出入宿舍楼记录DAO
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface AccessInfoDao {

    /**
     * 根据卡号查询对应的学号
     *
     * @param cardNum 卡号
     * @return 卡号对应的学号，没有则返回null
     */
    Integer getStudnetNum(@Param("cardNum") String cardNum);

    /**
     * 判断刷卡学生的学号与楼号是否匹配
     *
     * @param buildingNum 访问楼号
     * @param studentNum  学生学号
     * @return 属于该楼返回房间号，否则返回null
     */
    Integer judgeAccess(@Param("buildingNum") int buildingNum,
                        @Param("studentNum") int studentNum);

    /**
     * 获取最近一次学生进入宿舍楼的状态，从而判断这次是进还是出
     *
     * @param buildingNum 访问楼号
     * @param studentNum  学生学号
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
    int addAccess(@Param("buildingNum") int buildingNum,
                  @Param("studentNum") int studentNum,
                  @Param("accessStatus") int accessStatus);

    /**
     * 添加被阻的访问记录，访问时间默认为系统当前时间
     *
     * @param buildingNum 访问楼号
     * @param studentNum  学生学号
     * @return 添加成功返回1，失败返回0
     */
    int addBlock(@Param("buildingNum") int buildingNum,
                 @Param("studentNum") int studentNum);

    /**
     * 获取当天出入人流总数, key为出入状态(in/out),value为总数
     *
     * @param buildingNum 宿舍楼号
     * @param dawnTime    当天凌晨的时间
     * @return 查询为空返回null, 否则返回封装了出入总人数的List<AccessCount>
     */
    List<AccessCount> getTodayInOutSum(@Param("buildingNum") int buildingNum,
                                       @Param("dawnTime") String dawnTime);

    /**
     * 获取当天被阻访问的学生信息
     *
     * @param buildingNum 宿舍楼号
     * @param dawnTime    当天凌晨的时间
     * @return 查询为空返回null, 否则返回封装了被阻访问的学生信息的List<BlockInfo>
     */
    List<BlockInfo> getTodayBlockInfo(@Param("buildingNum") int buildingNum,
                                      @Param("dawnTime") String dawnTime);


    /**
     * 获取当晚未归寝的学生信息
     *
     * @param buildingNum 宿舍楼号
     * @return 查询为空返回null, 否则返回封装了未归寝的学生信息的List<StudentInfo>
     */
    List<StudentInfo> getOutStudentInfo(@Param("buildingNum") int buildingNum);

    /**
     * 列出所有被阻访问记录
     *
     * @return 返回封装了被阻访问信息的List<BlockInfo>
     */
    List<BlockInfo> listBlock();

    /**
     * 列出所有出入记录
     *
     * @return 返回封装了出入记录信息的List<AccessInfo>
     */
    List<AccessInfo> listAccess();

    /**
     * 根据时间删除被阻记录
     *
     * @param accessTime 访问时间
     * @return 删除成功返回1，否则0
     */
    int deleteBlock(@Param("accessTime") String accessTime);

    /**
     * 根据时间删除出入记录
     *
     * @param accessTime 访问时间
     * @return 删除成功返回1，否则0
     */
    int deleteAccess(@Param("accessTime") String accessTime);
}
