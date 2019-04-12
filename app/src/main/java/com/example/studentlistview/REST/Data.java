package com.example.studentlistview.REST;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("deviceId")
    private int deviceId;
    @SerializedName("pm25")
    private Double pm25;
    @SerializedName("pm10")
    private Double pm10;
    @SerializedName("co2")
    private int co2;
    @SerializedName("o3")
    private int o3;
    @SerializedName("pressure")
    private Double pressure;
    @SerializedName("temperature")
    private Double temperature;
    @SerializedName("humidity")
    private Double humidity;
    @SerializedName("userId")
    private String userId;

    public Data(){

    }



    public Data(int deviceId, Double pm25, Double pm10, int co2, int o3,
                Double pressure, Double temperature, Double humidity, String userId){
        this.deviceId = deviceId;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.co2 = co2;
        this.o3 = o3;
        this.pressure = pressure;
        this.temperature  = temperature;
        this.humidity = humidity;
        this.userId = userId;

    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
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

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Data{" +
                "deviceId=" + deviceId +
                ", pm25=" + pm25 +
                ", pm10=" + pm10 +
                ", co2=" + co2 +
                ", o3=" + o3 +
                ", pressure=" + pressure +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", userId='" + userId + '\'' +
                '}';
    }
}


