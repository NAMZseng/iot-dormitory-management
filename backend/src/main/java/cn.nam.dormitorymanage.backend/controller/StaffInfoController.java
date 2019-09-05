package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.entity.StaffInfo;
import cn.nam.dormitorymanage.backend.entity.User;
import cn.nam.dormitorymanage.backend.service.StaffInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("list")
    public String list(Model model, HttpSession session){
        Object userObj = session.getAttribute("user");
        if(userObj != null) {
            // 正确登陆
            List<StaffInfo> staffList = staffInfoService.list();
            model.addAttribute("staffList", staffList);

            return "backend/staffList";
        } else {
            return "redirect:login";
        }
    }
}
