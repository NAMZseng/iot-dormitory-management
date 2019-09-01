package cn.nam.dormitorymanage.backend.service;

import cn.nam.dormitorymanage.backend.dao.AccessInfoDao;
import cn.nam.dormitorymanage.backend.entity.AccessCount;
import cn.nam.dormitorymanage.backend.entity.BlockInfo;
import cn.nam.dormitorymanage.backend.entity.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Service
public class AccessInfoService {

    @Autowired
    private AccessInfoDao accessInfoDao;

    /**
     * 根据卡号和宿舍楼号，判断卡号对应的学生是否属于该宿舍楼。
     * 若属于该宿舍楼，更新出入记录access_info表，并返回1表示通过；
     * 若不属于，更新被阻记录block_info表，并返回-1表示拒绝。
     *
     * @param buildingNum 宿舍楼号
     * @param studentNum  学生学号
     * @return 通过 1，拒绝 -1
     */
    public int judgeAccess(int buildingNum, String cardNum) {
        Integer studentNum = accessInfoDao.getStudnetNum(cardNum);
        if (studentNum == null) {
            // 学号不存在
            return -1;
        }

        Integer roomNum = accessInfoDao.judgeAccess(buildingNum, studentNum.intValue());
        if (roomNum != null) {
            // 学号与楼号匹配,允许通过

            Integer preStatus = accessInfoDao.getPreStatus(buildingNum, studentNum);
            if (preStatus == null) {
                // 该学生为第一次出宿舍楼
                return accessInfoDao.addAccess(buildingNum, studentNum, -1);
            } else {
                // 状态取反
                preStatus = -1 * preStatus;
                return accessInfoDao.addAccess(buildingNum, studentNum, preStatus);
            }
        } else {
            // 学号与楼号不匹配，将访问记录写入被阻访问记录表
            accessInfoDao.addBlock(buildingNum, studentNum);
            return -1;
        }
    }

    /**
     * 获取当天出入人流总数, key为出入状态(in/out),value为总数
     *
     * @param buildingNum 宿舍楼号
     * @param dawnTime    当天凌晨的时间
     * @return 封装出入总人数的Map
     */
    public Map<String, Long> getTodayInOutSum(int buildingNum, String dawnTime) {
        List<AccessCount> list = accessInfoDao.getTodayInOutSum(buildingNum, dawnTime);
        if (list == null) {
            return null;
        }

        int size = list.size();
        Map<String, Long> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            map.put(list.get(i).getType(), list.get(i).getNum());
        }

        return map;
    }

    /**
     * 获取当天被阻访问的学生信息
     *
     * @param buildingNum 宿舍楼号
     * @param dawnTime    当天凌晨的时间
     * @return 查询为空返回null, 否则访问封装了被阻访问的学生信息的List<BlockInfo>
     */
    public List<BlockInfo> getTodayBlockInfo(int buildingNum, String dawnTime) {
        return accessInfoDao.getTodayBlockInfo(buildingNum, dawnTime);
    }

    /**
     * 获取当晚未归寝的学生信息
     *
     * @param buildingNum 宿舍楼号
     * @return 查询为空返回null, 否则返回封装了未归寝的学生信息的List<StudentInfo>
     */
    public List<StudentInfo> getOutStudentInfo(int buildingNum) {
        return accessInfoDao.getOutStudentInfo(buildingNum);
    }
}
