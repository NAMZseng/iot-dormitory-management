package cn.nam.dormitorymanage.backend.entity;

import java.util.Date;

/**
 * 温湿度传感器数据记录表
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class HumitureInfo {
    /**
     * 温湿度传感器的物理地址
     */
    private String macAddress;
    /**
     * 传感器所在楼号
     */
    private int buildingNum;
    /**
     * 传感器在楼里的具体位置
     */
    private String location;
    /**
     * 数据采集时间采集时间
     */
    private Date collectTime;
    /**
     * 采集的温度（摄氏度）
     */
    private float temperature;
    /**
     * 采集的相对湿度
     */
    private float humidity;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "HumitureInfo{" +
                "macAddress='" + macAddress + '\'' +
                ", buildingNum=" + buildingNum +
                ", location='" + location + '\'' +
                ", collectTime=" + collectTime +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }
}
