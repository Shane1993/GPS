package com.example.location.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.location.model.LocationInfo;

/**
 * 该类是为了将数据和数据库之间的通信方法（如增删改查方法）封装起来，
 * 	封装起来的原因是因为数据的增删改查都要写一大句数据库语言很麻烦，封装起来后只要调用
 * 		相应的方法就可以实现增删改查了
 * 	所以总之对于数据在数据库里的增删改查等等只需要用该类对象就可以实现了
 * 
 * 起名为DAO的原因：
 * Dao:Data Access Object 数据访问对象
 * Dao类容纳了所有访问数据库的方法，并同时管理者数据库的连接、关闭等内容。
 * 
 * @author lenovo
 *
 */
public class LocationDAO {

	private DatabaseHelper helper;	//创建DatabaseHelper对象
	private SQLiteDatabase db;		//创建SQLiteDatabase对象
	
	public LocationDAO(Context context)
	{
		helper = new DatabaseHelper(context);
	}
	

	/**
	 * 添加位置信息
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
	 * 通过AreaLocationInfo对象在数据库里的id修改相应位置信息
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
	 * 通过id查找相应的LocationInfo对象
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
	 * 删除一条或多条LocationInfo对象信息
	 * @param integers
	 */
	public void detele(Integer...  ids)
	{
		if(ids.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			//先通过for循环得到要删除的信息的个数并以'?'形式添加到StringBuffer里面，
				//因为就像上面的add()中的SQLite语句(?,?,?,?,?,?,?,?,?,?)需要疑问号表示指定的东西
			for(int i = 0;i < ids.length; i++)//遍历要删除的id集合
			{
				sb.append('?').append(',');		//将删除条件添加到StringBuffer里
			}
			sb.deleteCharAt(sb.length() - 1);	//删除掉最后的‘,’不然在SQLite语句会出错
			db = helper.getWritableDatabase();
			db.execSQL("delete from person where id in (" + sb + ")", 
					(Object[]) ids);
		
		}
	}
	
	/**
	 * 获取数据库里指定的多条数据
	 * 
	 * @param start
	 * 			起始位置
	 * @param count
	 * 			每页显示数量
	 * @return
	 */
	public List<LocationInfo> getScrollData(int start,int count)
	{
		List<LocationInfo> infoList = new ArrayList<LocationInfo>();
		db = helper.getWritableDatabase();
		//先得到指针
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
	 * 获取总记录数
	 * @return
	 */
	public long getCount()
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(id) from person", null);//获取信息的记录数
		if(cursor.moveToNext())
		{
			return cursor.getLong(0);
		}
		
		return 0;//如果没有数据则返回0
	}
	
	/**
	 * 获取最大编号
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
		
		return 0;//如果没有数据则返回0
	}
	
}
