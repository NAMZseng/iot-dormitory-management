package cn.nam.dormitorymanage.backend.entity;

/**
 * 出入人流总数统计类
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class AccessCount {
    /**
     * 出入类型 in/out
     */
    private String type;
    /**
     * 出/入的总人数
     */
    private long num;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "AccessCount{" +
                "type='" + type + '\'' +
                ", num=" + num +
                '}';
    }
}
