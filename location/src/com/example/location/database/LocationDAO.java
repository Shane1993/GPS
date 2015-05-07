package com.example.location.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.location.model.LocationInfo;

/**
 * ������Ϊ�˽����ݺ����ݿ�֮���ͨ�ŷ���������ɾ�Ĳ鷽������װ������
 * 	��װ������ԭ������Ϊ���ݵ���ɾ�Ĳ鶼Ҫдһ������ݿ����Ժ��鷳����װ������ֻҪ����
 * 		��Ӧ�ķ����Ϳ���ʵ����ɾ�Ĳ���
 * 	������֮�������������ݿ������ɾ�Ĳ�ȵ�ֻ��Ҫ�ø������Ϳ���ʵ����
 * 
 * ����ΪDAO��ԭ��
 * Dao:Data Access Object ���ݷ��ʶ���
 * Dao�����������з������ݿ�ķ�������ͬʱ���������ݿ�����ӡ��رյ����ݡ�
 * 
 * @author lenovo
 *
 */
public class LocationDAO {

	private DatabaseHelper helper;	//����DatabaseHelper����
	private SQLiteDatabase db;		//����SQLiteDatabase����
	
	public LocationDAO(Context context)
	{
		helper = new DatabaseHelper(context);
	}
	

	/**
	 * ���λ����Ϣ
	 * @param locationInfo
	 */
	public void add(LocationInfo locationInfo)
	{
		db = helper.getWritableDatabase();
//		System.out.println("This is add" + locationInfo.toString());
		db.execSQL("insert into person (id,longitude,latitude,speed,time,areaname) values (?,?,?,?,?,?)",
		new Object[]{locationInfo.getid(),locationInfo.getLongitude(),locationInfo.getLatitude(),
				locationInfo.getSpeed(),locationInfo.getTime(),locationInfo.getAreaName()
				});
	}
	
	
	/**
	 * ͨ��AreaLocationInfo���������ݿ����id�޸���Ӧλ����Ϣ
	 * @param locationInfo
	 */
	public void update(LocationInfo locationInfo)
	{
		db = helper.getWritableDatabase();
		db.execSQL("update person set longitude = ?,latitude = ?,"
				+ "speed = ?,time = ? areaname = ? where id = ?",
		new Object[]{locationInfo.getLongitude(),locationInfo.getLatitude(),
						locationInfo.getSpeed(),locationInfo.getTime(),
						locationInfo.getAreaName(),locationInfo.getid()});
	}
	
	/**
	 * ͨ��id������Ӧ��LocationInfo����
	 * @param id
	 * @return
	 */
	public LocationInfo find(int id)
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select id, longitude, latitude, speed,"
				+ "time, areaname from person where id = ?",
		new String[]{String.valueOf(id)});
		
		if(cursor.moveToNext())
		{
			return new LocationInfo(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("longitude")), 
					cursor.getDouble(cursor.getColumnIndex("latitude")), 
					cursor.getFloat(cursor.getColumnIndex("speed")), 
					cursor.getString(cursor.getColumnIndex("time")),
					cursor.getString(cursor.getColumnIndex("areaname")));
		}
		
		return null;
	}
	
	/**
	 * ɾ��һ�������LocationInfo������Ϣ
	 * @param integers
	 */
	public void detele(Integer...  ids)
	{
		if(ids.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			//��ͨ��forѭ���õ�Ҫɾ������Ϣ�ĸ�������'?'��ʽ��ӵ�StringBuffer���棬
				//��Ϊ���������add()�е�SQLite���(?,?,?,?,?,?,?,?,?,?)��Ҫ���ʺű�ʾָ���Ķ���
			for(int i = 0;i < ids.length; i++)//����Ҫɾ����id����
			{
				sb.append('?').append(',');		//��ɾ��������ӵ�StringBuffer��
			}
			sb.deleteCharAt(sb.length() - 1);	//ɾ�������ġ�,����Ȼ��SQLite�������
			db = helper.getWritableDatabase();
			db.execSQL("delete from person where id in (" + sb + ")", 
					(Object[]) ids);
		
		}
	}
	
	/**
	 * ��ȡ���ݿ���ָ���Ķ�������
	 * 
	 * @param start
	 * 			��ʼλ��
	 * @param count
	 * 			ÿҳ��ʾ����
	 * @return
	 */
	public List<LocationInfo> getScrollData(int start,int count)
	{
		List<LocationInfo> infoList = new ArrayList<LocationInfo>();
		db = helper.getWritableDatabase();
		//�ȵõ�ָ��
		Cursor cursor = db.rawQuery("select * from person limit ?,?", 
				new String[]{String.valueOf(start),String.valueOf(count)});
		while(cursor.moveToNext())
		{
			infoList.add(new LocationInfo(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("longitude")), 
					cursor.getDouble(cursor.getColumnIndex("latitude")), 
					cursor.getFloat(cursor.getColumnIndex("speed")), 
					cursor.getString(cursor.getColumnIndex("time")),
					cursor.getString(cursor.getColumnIndex("areaname"))));
		}
		
		return infoList;
	}
	
	/**
	 * ��ȡ�ܼ�¼��
	 * @return
	 */
	public long getCount()
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(id) from person", null);//��ȡ��Ϣ�ļ�¼��
		if(cursor.moveToNext())
		{
			return cursor.getLong(0);
		}
		
		return 0;//���û�������򷵻�0
	}
	
	/**
	 * ��ȡ�����
	 * @return
	 */
	public int getMaxId()
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select max(id) from person", null);
		while(cursor.moveToLast())
		{
			return cursor.getInt(0);
		}
		
		return 0;//���û�������򷵻�0
	}
	
}
