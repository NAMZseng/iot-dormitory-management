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
}
