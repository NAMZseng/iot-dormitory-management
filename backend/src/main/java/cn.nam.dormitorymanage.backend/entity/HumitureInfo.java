package cn.nam.dormitorymanage.backend.entity;

import java.util.Date;

/**
 * 温湿度传感器数据记录表
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class HumitureInfo {
    private String macAddress;
    private int buildingNum;
    private String location;
    private Date collectTime;
    private float temperature;
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
