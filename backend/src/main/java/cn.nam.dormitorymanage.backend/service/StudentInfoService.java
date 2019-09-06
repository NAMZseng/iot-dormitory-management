package cn.nam.dormitorymanage.backend.service;

import cn.nam.dormitorymanage.backend.dao.StudentInfoDao;
import cn.nam.dormitorymanage.backend.entity.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Service
public class StudentInfoService {

    @Autowired
    private StudentInfoDao studentInfoDao;

    /**
     * 查询某一楼里的所有学生信息
     *
     * @param buildingNum 查询楼号
     * @return 学生列表List<StudentInfo>
     */
    public List<StudentInfo> getStudents(int buildingNum) {
        return studentInfoDao.getStudents(buildingNum);
    }

    /**
     * 显示所有学生信息
     *
     * @return List<StudentInfo>
     */
    public List<StudentInfo> list() {
        return studentInfoDao.list();
    }

    /**
     * 添加学生信息
     *
     * @param studentInfo 封装了学生信息的类对象
     * @return 添加成功返回1，否则0
     */
    public int add(StudentInfo studentInfo) {
        return studentInfoDao.add(studentInfo);
    }

    /**
     * 删除学号对应的学生信息
     *
     * @param selectIds 待删除学生的学号数组
     * @return 删除成功返回总删除条数，否则0
     */
    public int delete(String[] selectIds) {
        int sum = 0;

        for (int i = 0; i < selectIds.length; i++) {
            sum += studentInfoDao.delete(Integer.parseInt(selectIds[i]));
        }

        return sum;
    }
}