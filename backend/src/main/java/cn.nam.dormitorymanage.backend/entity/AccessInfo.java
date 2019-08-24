package cn.nam.dormitorymanage.backend.entity;

import java.util.Date;

/**
 * 学生出入宿舍楼记录表实体
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class AccessInfo {

    private int buildingNum;
    private int studentNum;
    private Date accessTime;
    private String accessStatus;

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    @Override
    public String toString() {
        return "AccessInfo{" +
                "buildingNum=" + buildingNum +
                ", studentNum=" + studentNum +
                ", accessTime=" + accessTime +
                ", accessStatus='" + accessStatus + '\'' +
                '}';
    }
}
