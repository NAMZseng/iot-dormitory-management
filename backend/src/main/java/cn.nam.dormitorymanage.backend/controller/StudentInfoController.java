package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.entity.StudentInfo;
import cn.nam.dormitorymanage.backend.service.StaffInfoService;
import cn.nam.dormitorymanage.backend.service.StudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Controller
@RequestMapping("student")
public class StudentInfoController {

    @Autowired
    private StudentInfoService studentInfoService;

    /**
     * 查询某一楼里的所有学生信息
     *
     * @param buildingNum 查询楼号
     * @return 学生列表List<StudentInfo>
     */
    @RequestMapping("getStudentsInfo")
    @ResponseBody
    public List<StudentInfo> getStudentsInfo(@RequestParam("buildingNum") int buildingNum) {

        return studentInfoService.getStudents(buildingNum);
    }
}
