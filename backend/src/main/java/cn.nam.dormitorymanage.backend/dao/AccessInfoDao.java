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
     * 添加被接收的访问记录，访问时间默认为系统当前时间
     *
     * @param buildingNum  访问楼号
     * @param studentNum   学生学号
     * @param accessStatus 进出状态 in/out
     * @return 添加成功返回1，失败返回0
     */
    int addAccess(@Param("buildingNum") int buildingNum,
                  @Param("studentNum") int studentNum,
                  @Param("accessStatus") String accessStatus);

    /**
     * 添加被阻的访问记录，访问时间默认为系统当前时间
     *
     * @param buildingNum  访问楼号
     * @param studentNum   学生学号
     * @param accessStatus 进出状态 in/out
     * @return 添加成功返回1，失败返回0
     */
    int addBlock(@Param("buildingNum") int buildingNum,
                 @Param("studentNum") int studentNum,
                 @Param("accessStatus") String accessStatus);
}
