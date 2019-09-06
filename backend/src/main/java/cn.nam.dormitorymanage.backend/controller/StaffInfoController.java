package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.entity.StaffInfo;
import cn.nam.dormitorymanage.backend.entity.User;
import cn.nam.dormitorymanage.backend.service.StaffInfoService;
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
@RequestMapping("staff")
public class StaffInfoController {

    @Autowired
    private StaffInfoService staffInfoService;

    /**
     * 登陆，获取职工信息
     *
     * @param tel      职工电话
     * @param password 登陆密码
     * @return 手机密码匹配，返回职工信息实体类对象；否则，返回null
     */
    @RequestMapping("login")
    @ResponseBody
    public StaffInfo login(@RequestParam("tel") String tel,
                           @RequestParam("password") String password) {
        tel = tel.trim();
        password = password.trim();

        return staffInfoService.login(tel, password);
    }

    /**
     * 修改密码
     *
     * @param tel         职工电话
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 手机号与旧密码匹配，修改成功返回1；匹配失败返回-1
     */
    @RequestMapping("updatePassword")
    @ResponseBody
    public int updatePassword(@RequestParam("tel") String tel,
                              @RequestParam("oldPassword") String oldPassword,
                              @RequestParam("newPassword") String newPassword) {
        tel = tel.trim();
        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();

        return staffInfoService.updatePassword(tel, oldPassword, newPassword);
    }

    /**
     * 修改手机号
     * url示例
     *
     * @param oldTel   旧手机号
     * @param password 密码
     * @param newTel   新手机号
     * @return 新电话已注册返回0；修改成功返回1，旧手机与密码不匹配返回-1
     */
    @RequestMapping("updateTel")
    @ResponseBody
    public int updateTel(@RequestParam("oldTel") String oldTel,
                         @RequestParam("password") String password,
                         @RequestParam("newTel") String newTel) {
        oldTel = oldTel.trim();
        password = password.trim();
        newTel = newTel.trim();

        return staffInfoService.updateTel(oldTel, password, newTel);
    }

    /**
     * 获取所有职工信息
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("list")
    public String list(Model model, HttpSession session) {

//        未配置栏截器前时，需在每个方法里加上下面处理
        User userObj = (User) session.getAttribute("user");
        if (userObj != null) {
            // 正确登陆
            List<StaffInfo> staffList = staffInfoService.list();
            model.addAttribute("staffList", staffList);

            return "backend/staffList";

        } else {
            return "redirect:/login";
        }
//
//        List<StaffInfo> staffList = staffInfoService.list();
//        model.addAttribute("staffList", staffList);
//
//        return "backend/staffList";

    }

    /**
     * 添加职工
     *
     * @param session
     * @param staffInfo
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(HttpSession session, StaffInfo staffInfo) {
        staffInfoService.add(staffInfo);

        return "redirect:/staff/list";
    }

    /**
     * 删除职工
     *
     * @param model
     * @param staffNum
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public String delete(Model model, @RequestParam("staffNum") String staffNum) {
        int flag = 0;
        if (staffNum != null && !"".equals(staffNum)) {
            String[] selectIds = staffNum.split(" ");
            try {
                flag = staffInfoService.delete(selectIds);
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
