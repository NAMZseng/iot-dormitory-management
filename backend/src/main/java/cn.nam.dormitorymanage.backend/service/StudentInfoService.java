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
     *
     * @return
     */
    public List<StudentInfo> list(){
        return studentInfoDao.list();
    }

    /**
     * @param studentInfo
     * @return
     */
    public int add(StudentInfo studentInfo) {
        return studentInfoDao.add(studentInfo);
    }

    public int delete(String[] selectIds) {
        int sum = 0;

        for (int i = 0; i < selectIds.length; i++) {
            sum += studentInfoDao.delete(Integer.parseInt(selectIds[i]));
        }

        return sum;
    }
}