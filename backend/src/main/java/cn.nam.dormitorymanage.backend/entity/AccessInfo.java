package cn.nam.dormitorymanage.backend.entity;

import java.util.Date;

/**
 * @author Nanrong Zeng
 * @version 1.0
 */
public class AccessInfo {

    private int studentNum;

    private String name;

    private int buildingNum;

    private Date accessTime;

    private String accessStatus;

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
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
}
