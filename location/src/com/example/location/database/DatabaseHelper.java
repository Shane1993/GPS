package com.example.location.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;//�������ݿ�汾��
	private static final String DBNAME = "location.db";//�������ݿ�����
	
	public DatabaseHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//���ﴴ��������һ�������洢����λ��Ϣ��һ���Ǵ洢������Ϊ��λ��Ϣ��
		//�������ݿ�ı���area��ע���Ǳ��������ݿ�����������Ѿ����ˣ���location���������������area��������Ų�����������Ϣ�ģ�
			//����һЩ�Ǹñ��������ԣ���id��_id��,���ƣ�name�����ĸ�����ľ�γ��
		db.execSQL("create table area (id integer primary key, name varchar(10),"
				+ "longitude1 decimal, longitude2 decimal, longitude3 decimal, longitude4 decimal, "
				+ "latitude1 decimal, latitude2 decimal, latitude3 decimal, latitude4 decimal )");
		//�������˶�λ����person����id��Ϣ����γ����Ϣ���ٶ���Ϣ��ʱ����Ϣ
		db.execSQL("create table person (id integer primary key, "
				+ "longitude decimal, latitude decimal, speed decimal, time varchar(20),areaname varchar(20) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	

}
