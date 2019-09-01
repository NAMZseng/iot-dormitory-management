package nuc.edu.dormitorydemo.domain;

/**
 * 温湿度对象
 */

public class HumitureInfo implements Comparable<HumitureInfo>{
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

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    private String macAddress;
    private int buildingNum;
    private String location;
    private long collectTime;
    private Double temperature;
    private Double humidity;

    @Override
    public int compareTo(HumitureInfo o) {
        return (int) (Long.valueOf(this.getCollectTime()) - Long.valueOf(o.getCollectTime()));
    }
}
