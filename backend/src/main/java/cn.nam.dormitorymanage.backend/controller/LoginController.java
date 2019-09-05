package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Controller
public class LoginController {

    private final String loginName = "admin";
    private final String loginPwd = "admin";

    @RequestMapping("login")
    public String login(){
        return "login";
    }

    @RequestMapping("logined.html")
    @ResponseBody
    public String logined(@RequestParam String user,HttpSession session){

        if(user == null || "".equals(user)){
            return "noData";
        }else{
            User userObj = JSONObject.parseObject(user, User.class);
            try {
                if(!loginName.equals(userObj.getUsername())){
                    return "noUsername";
                }else{
                    if(loginPwd.equals(userObj.getPassword())){
                        session.setAttribute("user", userObj);
                        return "success";
                    }else{
                        return "pwdError";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
    }

    @RequestMapping("logout.html")
    public String logout(HttpSession session){

        session.removeAttribute("user");
        session.invalidate();

        return "redirect:login";
    }

    @RequestMapping("main.html")
    public ModelAndView toMainPage(Model model,HttpSession session){

        Object userObj = session.getAttribute("user");
        if(userObj != null){
            return new ModelAndView("main");
        }else{
            return new ModelAndView("redirect:login");
        }
    }

}
