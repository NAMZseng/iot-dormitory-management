package cn.nam.dormitorymanage.backend.dao;

import cn.nam.dormitorymanage.backend.entity.StudentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生信息Dao
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface StudentInfoDao {

    /**
     * 查询某一楼里的所有学生信息
     *
     * @param buildingNum 查询楼号
     * @return 学生列表List<StudentInfo>
     */
    List<StudentInfo> getStudents(@Param("buildingNum") int buildingNum);

    /**
     * 显示所有学生信息
     *
     * @return List<StudentInfo>
     */
    List<StudentInfo> list();

    /**
     * 添加学生信息
     *
     * @param studentInfo 封装了学生信息的类对象
     * @return 添加成功返回1，否则0
     */
    int add(StudentInfo studentInfo);

    /**
     * 删除学号对应的学生信息
     *
     * @param num 学号
     * @return 删除成功返回1，否则0
     */
    int delete(@Param("num") int num);
}
