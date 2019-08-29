package cn.nam.dormitorymanage.backend.controller;

import cn.nam.dormitorymanage.backend.entity.BlockInfo;
import cn.nam.dormitorymanage.backend.service.AccessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Controller
@RequestMapping("access")
public class AccessInfoController {

    @Autowired
    private AccessInfoService accessInfoService;

    /**
     * 学生出入宿舍楼权限判断
     *
     * @param buildingNum 宿舍楼号
     * @param studentNum  学生学号
     * @return 通过 1，拒绝 -1
     */
    @RequestMapping("judgeAccess")
    @ResponseBody
    public int judgeAccess(@RequestParam("buildingNum") int buildingNum,
                           @RequestParam("cardNum") String cardNum) {
        return accessInfoService.judgeAccess(buildingNum, cardNum);
    }

    /**
     * 获取当天出入人流总数, key为出入状态(in/out),value为总数
     *
     * @param buildingNum 宿舍楼号
     * @param dawnTime    当天凌晨的时间
     * @return 封装出入总人数的Map
     */
    @RequestMapping("getTodayInOutSum")
    @ResponseBody
    public Map<String, Long> getTodayInOutSum(@RequestParam("buildingNum") int buildingNum) {

        // 获取当天凌晨的时间
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        String dawnTime = sdf.format(new Date());
        return accessInfoService.getTodayInOutSum(buildingNum, dawnTime);
    }

    @RequestMapping("getTodayBlockInfo")
    @ResponseBody
    public List<BlockInfo> getTodayBlockInfo(@RequestParam("buildingNum") int buildingNum) {
        // 获取当天凌晨的时间
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        String dawnTime = sdf.format(new Date());

        return accessInfoService.getTodayBlockInfo(buildingNum, dawnTime);
    }
}

