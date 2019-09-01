package nuc.edu.dormitorydemo.domain;

/**
 * 宿舍员工对象
 */

public class Staff {
    private int num;
    private String name;
    private String tel;
    private int buildingNum;
    private String title;
    private String passward;

    @Override
    public String toString() {
        return "Staff{" +
                "num=" + num +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", buildingNum='" + buildingNum + '\'' +
                ", title='" + title + '\'' +
                ", passward='" + passward + '\'' +
                '}';
    }

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

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }
}
