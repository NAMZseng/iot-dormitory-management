package cn.nam.dormitorymanage.backend.service;

import cn.nam.dormitorymanage.backend.dao.StaffInfoDao;
import cn.nam.dormitorymanage.backend.entity.StaffInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
@Service
public class StaffInfoService {

    @Autowired
    private StaffInfoDao staffInfoDao;

    /**
     * 登陆，获取职工信息
     *
     * @param tel      职工电话
     * @param password 登陆密码
     * @return 手机密码匹配，返回职工信息实体类对象；否则，返回null
     */
    public StaffInfo login(String tel, String password) {

        return staffInfoDao.getStaffInfo(tel, password);
    }

    /**
     * 修改密码
     *
     * @param tel         职工电话
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 手机号与旧密码匹配，修改成功返回1；匹配失败返回-1
     */
    public int updatePassword(String tel, String oldPassword, String newPassword) {
        int result = staffInfoDao.upadatePassord(tel, oldPassword, newPassword);
        if (result == 1) {
            // 修改成功
            return result;
        } else {
            // 手机号与旧密码不匹配
            return -1;
        }
    }

    /**
     * 修改手机号
     *
     * @param oldTel 旧电话
     * @param password 登陆密码
     * @param newTel 新电话
     * @return 新电话已注册返回0；修改成功返回1，旧手机与密码不匹配返回-1
     */
    public int updateTel(String oldTel, String password, String newTel) {
        String tel = staffInfoDao.fingTel(newTel);
        if (tel == null) {
            // 手机号未被注册
            int result = staffInfoDao.updateTel(oldTel, password, newTel);
            if (result == 1) {
                // 修改成功
                return result;
            } else {
                // 旧手机号与密码不匹配
                return -1;
            }
        } else {
            // 手机号已存在
            return 0;
        }
    }

    /**
     * 显示所有职工信息
     *
     * @return
     */
    public List<StaffInfo> list(){
        return staffInfoDao.list();
    }

    /**
     * @param staffInfo
     * @return
     */
    public int add(StaffInfo staffInfo) {
        return staffInfoDao.add(staffInfo);
    }

    public int delete(String[] selectIds) {
        int sum = 0;

        for (int i = 0; i < selectIds.length; i++) {
            sum += staffInfoDao.delete(Integer.parseInt(selectIds[i]));
        }

        return sum;
    }
}
