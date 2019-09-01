package nuc.edu.dormitorydemo.domain;

/**
 * 未归寝人员对象
 */

public class BlockInfo {
    @Override
    public String toString() {
        return "BlockInfo{" +
                "studentName='" + studentName + '\'' +
                ", num=" + num +
                ", teacherTel='" + teacherTel + '\'' +
                ", buildingNum=" + buildingNum +
                ", accessTime='" + accessTime + '\'' +
                '}';
    }

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

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    private String studentName;
    private int num;
    private String teacherTel;
    private int buildingNum;
    private String accessTime;
}
