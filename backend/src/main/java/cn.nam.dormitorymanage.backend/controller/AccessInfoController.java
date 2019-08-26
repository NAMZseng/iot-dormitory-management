package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.service.AccessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Controller
@RequestMapping("access")
public class AccessInfoController {

    @Autowired
    private AccessInfoService accessInfoService;

    @RequestMapping("judgeAccess")
    @ResponseBody
    public int updateAccessInfo(@RequestParam("buildingNum") int buildingNum,
                                @RequestParam("studentNum") int studentNum) {

        return accessInfoService.updateAccessInfo(buildingNum, studentNum);
    }

    @RequestMapping("test")
    @ResponseBody
    public int test(@RequestParam("buildingNum") int buildingNum,
                    @RequestParam("studentNum") int studentNum) {

        return buildingNum + studentNum;
    }
}
