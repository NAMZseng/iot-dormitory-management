package cn.nam.dormitorymanage.backend.dao;

import cn.nam.dormitorymanage.backend.entity.StaffInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 职工信息DAO
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface StaffInfoDao {

    /**
     * 获取职工信息
     *
     * @param tel      职工电话
     * @param password 登陆密码
     * @return 手机与密码匹配，返回职工信息实体类对象；否则，返回null
     */
    StaffInfo getStaffInfo(@Param("tel") String tel,
                           @Param("password") String password);

    /**
     * 修改密码
     *
     * @param tel         职工电话
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 手机号与旧密码匹配，返回1，否则返回0
     */
    int upadatePassord(@Param("tel") String tel,
                       @Param("oldPassword") String oldPassword,
                       @Param("newPassword") String newPassword);

    /**
     * 判断手机号是否存在
     *
     * @param tel 收集好
     * @return 手机号以存在，返回手机号；不存在，返回null
     */
    String fingTel(@Param("tel") String tel);

    /**
     * 修改手机号
     *
     * @param oldTel   旧手机号
     * @param password 密码
     * @param newTel   新手机号
     * @return 旧手机与密码匹配，返回1，否则返回0
     */
    int updateTel(@Param("oldTel") String oldTel,
                  @Param("password") String password,
                  @Param("newTel") String newTel);
}
