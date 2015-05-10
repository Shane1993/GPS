package com.example.location.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;//定义数据库版本号
	private static final String DBNAME = "location.db";//定义数据库名称
	
	public DatabaseHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//这里创建两个表，一个用来存储区域定位信息，一个是存储个人行为定位信息的
		//创建数据库的表名area（注意是表名，数据库的名称上面已经定了，叫location，这里这个表名是area，用来存放测量的区域信息的）
			//后面一些是该表名的属性，有id（_id）,名称（name）和四个角落的经纬度
		db.execSQL("create table area (id integer primary key, name varchar(10),"
				+ "longitude1 decimal, longitude2 decimal, longitude3 decimal, longitude4 decimal, "
				+ "latitude1 decimal, latitude2 decimal, latitude3 decimal, latitude4 decimal )");
		//创建个人定位表名person，有id信息，经纬度信息，速度信息，时间信息
		db.execSQL("create table person (id integer primary key, "
				+ "longitude decimal, latitude decimal, speed decimal, time varchar(20),areaname varchar(20) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	

}
