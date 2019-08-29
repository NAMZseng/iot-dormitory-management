package cn.nam.dormitorymanage.backend.entity;

import java.util.Date;

/**
 * 被阻访问记录实体
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class BlockInfo {
    private String studentName;
    private int num;
    private String teacherTel;
    private int buildingNum;
    private Date accessTime;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTeacherTel() {
        return teacherTel;
    }

    public void setTeacherTel(String teacherTel) {
        this.teacherTel = teacherTel;
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

    @Override
    public String toString() {
        return "BlockInfo{" +
                "studentName='" + studentName + '\'' +
                ", num=" + num +
                ", teacherTel='" + teacherTel + '\'' +
                ", buildingNum=" + buildingNum +
                ", accessTime=" + accessTime +
                '}';
    }
}
