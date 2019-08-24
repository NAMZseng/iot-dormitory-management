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
     * 添加访问记录，时间默认为系统当前时间
     *
     * @param buildingNum
     * @param studentNum
     * @param accessStatus
     * @return 添加成功返回1，失败返回0
     */
    int addAccess(@Param("buildingNum") int buildingNum,
                  @Param("studentNum") int studentNum,
                  @Param("accessStatus") String accessStatus);
}
