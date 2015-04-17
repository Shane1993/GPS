package com.example.location.model;

import cn.bmob.v3.BmobObject;

/**
 * 该类是用来封装定位的区域的具体信息的，包括了名字和四个经纬度
 * 
 * @author lenovo
 *
 */
public class AreaLocationInfo extends BmobObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8192196570389421721L;
	private int id;
	private String name;
	private double longitude1,longitude2,longitude3,longitude4;
	private double latitude1,latitude2,latitude3,latitude4;
	
	

	public AreaLocationInfo() {
		super();
	}



	public AreaLocationInfo(int id, String name, double longitude1,
			double longitude2, double longitude3, double longitude4,
			double latitude1, double latitude2, double latitude3,
			double latitude4) {
		super();
		this.id = id;
		this.name = name;
		this.longitude1 = longitude1;
		this.longitude2 = longitude2;
		this.longitude3 = longitude3;
		this.longitude4 = longitude4;
		this.latitude1 = latitude1;
		this.latitude2 = latitude2;
		this.latitude3 = latitude3;
		this.latitude4 = latitude4;
	}

	

	public int getid() {
		return id;
	}



	public void setid(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public double getLongitude1() {
		return longitude1;
	}



	public void setLongitude1(double longitude1) {
		this.longitude1 = longitude1;
	}



	public double getLongitude2() {
		return longitude2;
	}



	public void setLongitude2(double longitude2) {
		this.longitude2 = longitude2;
	}



	public double getLongitude3() {
		return longitude3;
	}



	public void setLongitude3(double longitude3) {
		this.longitude3 = longitude3;
	}



	public double getLongitude4() {
		return longitude4;
	}



	public void setLongitude4(double longitude4) {
		this.longitude4 = longitude4;
	}



	public double getLatitude1() {
		return latitude1;
	}



	public void setLatitude1(double latitude1) {
		this.latitude1 = latitude1;
	}



	public double getLatitude2() {
		return latitude2;
	}



	public void setLatitude2(double latitude2) {
		this.latitude2 = latitude2;
	}



	public double getLatitude3() {
		return latitude3;
	}



	public void setLatitude3(double latitude3) {
		this.latitude3 = latitude3;
	}



	public double getLatitude4() {
		return latitude4;
	}



	public void setLatitude4(double latitude4) {
		this.latitude4 = latitude4;
	}



	@Override
	public String toString() {
		return "name=" + name + "\n"
				+"longitude1=" + longitude1 + ", latitude1=" + latitude1 + "\n"
				+"longitude2=" + longitude2 + ", latitude2=" + latitude2 + "\n"
				+"longitude3=" + longitude3 + ", latitude3=" + latitude3 + "\n"
				+"longitude4=" + longitude4 + ", latitude4=" + latitude4 ;
	}
	
	
	
}
