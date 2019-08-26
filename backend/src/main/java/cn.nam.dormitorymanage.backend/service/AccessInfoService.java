package cn.nam.dormitorymanage.backend.service;

import cn.nam.dormitorymanage.backend.dao.AccessInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param studentNum 学生学号
     * @return 通过 1，拒绝 -1
     */
    public int judgeAccess(int buildingNum, String cardNum) {
        Integer studentNum = accessInfoDao.getStudnetNum(cardNum);
        if(studentNum == null) {
            // 学号不存在
            return -1;
        }

        Integer roomNum = accessInfoDao.judgeAccess(buildingNum, studentNum.intValue());
        if (roomNum != null) {
            // 学号与楼号匹配,允许通过

            Integer preStatus = accessInfoDao.getPreStatus(buildingNum, studentNum);
            if(preStatus == null) {
                // 该学生为第一次进入宿舍楼
                return accessInfoDao.addAccess(buildingNum, studentNum, 1);
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
}
