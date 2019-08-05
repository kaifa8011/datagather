package com.ciba.data.gather.entity;

/**
 * @author ciba
 * @description 自定义地理位置
 * @date 2018/12/3
 */
public class CustomLocation {
    private double lat;
    private double lng;
    private String country;
    private long time;
    private float accuracy;
    private int coordinateType = 1;

    public CustomLocation(double lat, double lng, String country, long time, float accuracy) {
        this.lat = lat;
        this.lng = lng;
        this.country = country;
        this.time = time;
        this.accuracy = accuracy;
    }

    public int getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(int coordinateType) {
        this.coordinateType = coordinateType;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
