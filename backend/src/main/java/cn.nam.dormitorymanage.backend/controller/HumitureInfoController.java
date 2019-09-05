package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.entity.HumitureInfo;
import cn.nam.dormitorymanage.backend.entity.User;
import cn.nam.dormitorymanage.backend.service.HumitureInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
     *
     * @param macAddress  传感器MAC地址
     * @param temperature 温度
     * @param humidity    相对湿度
     * @return 添加成功返回1，失败返回0
     */
    @RequestMapping("addData")
    @ResponseBody
    public int addData(@RequestParam("macAddr") String macAddress,
                       @RequestParam("temperature") float temperature,
                       @RequestParam("humidity") float humidity) {
        return humitureInfoService.addData(macAddress, temperature, humidity);
    }

    /**
     * 获取指代楼号当天的温湿度数据
     *
     * @param buildingNum 宿舍楼号
     * @return List<HumitureInfo>
     */
    @RequestMapping("getTodayData")
    @ResponseBody
    public List<HumitureInfo> getTodayData(@RequestParam("buildingNum") int buildingNum) {

        // 获取当天凌晨的时间
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        String dawnTime = sdf.format(new Date());

        return humitureInfoService.getTodayData(buildingNum, dawnTime);
    }

    @RequestMapping("list")
    public String list(Model model, HttpSession session) {
        User userObj = (User) session.getAttribute("user");
        if (userObj != null) {
            // 正确登陆
            List<HumitureInfo> humitureList = humitureInfoService.list();
            model.addAttribute("humitureList", humitureList);

            return "backend/humitureList";

        } else {
            return "redirect:/login";
        }
    }

    /**
     * @param model
     * @param collectTime
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public String delete(Model model, @RequestParam("collectTime") String collectTime) {
        int flag = 0;
        if (collectTime != null && !"".equals(collectTime)) {
            String[] selectIds = collectTime.split("/");
            try {
                flag = humitureInfoService.delete(selectIds);
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
