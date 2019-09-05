package cn.nam.dormitorymanage.backend.utils;

import cn.nam.dormitorymanage.backend.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆功能拦截器
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    private final String loginName = "admin";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && loginName.equals(user.getUsername())) {
            return true;
        }
        // 没有登陆，拦截，跳转登陆界面
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
