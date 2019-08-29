package cn.nam.dormitorymanage.backend.entity;

/**
 * 学生信息实体
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class StudentInfo {

    /**
     * 学生学号
     */
    private int num;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 学生所在学院
     */
    private String school;
    /**
     * 学生专业
     */
    private String major;
    /**
     * 学生的班主任姓名
     */
    private String teacherName;
    /**
     * 学生的班主任电话
     */
    private String teacherTel;
    /**
     * 学生的所在的宿舍楼号
     */
    private int buildingNum;
    /**
     * 学生所在的房间号
     */
    private int roomNum;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "num=" + num +
                ", name='" + name + '\'' +
                ", school='" + school + '\'' +
                ", major='" + major + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", teacherTel='" + teacherTel + '\'' +
                ", buildingNum=" + buildingNum +
                ", roomNum=" + roomNum +
                '}';
    }
}
