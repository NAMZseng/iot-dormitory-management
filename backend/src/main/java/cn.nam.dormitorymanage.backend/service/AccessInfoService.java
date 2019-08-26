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
     * 更新访问记录表
     *
     * @param buildingNum 宿舍楼号
     * @param studentNum 学号
     * @return 接受访问返回1，拒绝访问返回-1
     */
    public int updateAccessInfo(int buildingNum, int studentNum) {
        Integer roomNum = accessInfoDao.judgeAccess(buildingNum, studentNum);
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
