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
     * @param buildingNum
     * @param studentNum
     * @param accessStatus
     * @return
     */
    public int updateAccessInfo(int buildingNum, int studentNum, String accessStatus) {
        int res = accessInfoDao.addAccess(buildingNum, studentNum, accessStatus);
        return res;
    }
}
