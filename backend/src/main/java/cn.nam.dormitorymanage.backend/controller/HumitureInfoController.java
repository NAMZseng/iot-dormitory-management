package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.service.HumitureInfoService;
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
@RequestMapping("humiture")
public class HumitureInfoController {
    @Autowired
    private HumitureInfoService humitureInfoService;

    /**
     * 添加温湿度数据
     * url示例
     * http://49.232.57.160:8080/DormitoryManage/humiture/addData?
     * macAddr=9C625305004B1200&temperature=30.360001&humidity=67.099998
     * @param macAddress  传感器MAC地址
     * @param temperature 温度
     * @param humidity    相对湿度
     * @return 添加成功返回1，失败返回0
     */
    @RequestMapping("addData")
    @ResponseBody
    public int addData(@RequestParam("macAddr") String macAddress, @RequestParam("temperature") float temperature,
                       @RequestParam("humidity") float humidity) {
        return humitureInfoService.addData(macAddress, temperature, humidity);
    }

}
