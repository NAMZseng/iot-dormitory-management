package cn.nam.dormitorymanage.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Controller
public class TestDemo {

    @RequestMapping("getDemo")
    @ResponseBody
    public int getDemo() {
        int res = 1607094250;

        return  res;
    }
}
