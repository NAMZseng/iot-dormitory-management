package cn.nam.dormitorymanage.backend.entity;

/**
 * 职工信息实体
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class StaffInfo {

    /**
     * 职工工号
     */
    private int num;
    /**
     * 职工姓名
     */
    private String name;
    /**
     * 职工电话
     */
    private String tel;
    /**
     * 职工工作楼号
     */
    private int buildingNum;
    /**
     * 职工职称
     */
    private String title ;
    /**
     * 职工的登陆密码
     */
    private String password;

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "StaffInfo{" +
                "num=" + num +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", buildingNum=" + buildingNum +
                ", title='" + title + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
