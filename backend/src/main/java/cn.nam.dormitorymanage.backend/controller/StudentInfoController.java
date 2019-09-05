package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.entity.StudentInfo;
import cn.nam.dormitorymanage.backend.entity.User;
import cn.nam.dormitorymanage.backend.service.StaffInfoService;
import cn.nam.dormitorymanage.backend.service.StudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
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

    /**
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("list")
    public String list(Model model, HttpSession session) {
        User userObj = (User) session.getAttribute("user");
        if (userObj != null) {
            // 正确登陆
            List<StudentInfo> studentList = studentInfoService.list();
            model.addAttribute("studentList", studentList);

            return "backend/studentList";

        } else {
            return "redirect:/login";
        }
    }

    /**
     * @param session
     * @param studentInfo
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(HttpSession session, StudentInfo studentInfo) {
        studentInfoService.add(studentInfo);

        return "redirect:/student/list";
    }

    /**
     * @param model
     * @param staffNum
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public String delete(Model model, @RequestParam("studentNum") String studentNum) {
        int flag = 0;
        if (studentNum != null && !"".equals(studentNum)) {
            String[] selectIds = studentNum.split(" ");
            try {
                flag = studentInfoService.delete(selectIds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (flag > 0) {
            return "success";
        } else {
            return "failed";
        }
    }
}
