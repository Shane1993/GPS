package com.example.location.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 该类是用来封装定位信息的
 * 
 * @author lenovo
 * 
 */
public class LocationInfo extends BmobObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 515533548467366073L;

	private String deviceId;
	private int id;
	private double longitude, latitude;
	private float speed;
	private String time;
	private String areaName;

	public LocationInfo() {
		super();
	}

	public LocationInfo(int id, double longitude, double latitude, float speed,
			String time) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.speed = speed;
		this.time = time;
	}
	
	
	public LocationInfo(int id, double longitude,
			double latitude, float speed, String time, String areaName) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.speed = speed;
		this.time = time;
		this.areaName = areaName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getid() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String toJSONString() {
		return "{id:" + id + ",longitude:" + longitude + ",latitude:"
				+ latitude + ",speed:" + speed + ",time:" + time + "}";
	}

	@Override
	public String toString() {
		return "longitude=" + longitude + "\n" + "latitude=" + latitude + "\n"
				+ "speed=" + speed + "\n" + "time=" + time + "\n" + "locate=" + areaName;
	}

}
