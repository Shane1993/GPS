package com.example.location.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.location.model.AreaLocationInfo;

/**
 * 该类是用来处理区域数据和数据库的关系的
 * 
 * @author lenovo
 *
 */
public class AreaLocationDAO {

	private DatabaseHelper helper;	//创建DatabaseHelper对象
	private SQLiteDatabase db;		//创建SQLiteDatabase对象
	
	public AreaLocationDAO(Context context)
	{
		helper = new DatabaseHelper(context);
	}
	

	/**
	 * 添加位置信息
	 * @param areaLocationInfo
	 */
	public void add(AreaLocationInfo areaLocationInfo)
	{
		db = helper.getWritableDatabase();
		db.execSQL("insert into area (id,name,longitude1,longitude2,longitude3,longitude4," 
		+"latitude1,latitude2,latitude3,latitude4) values (?,?,?,?,?,?,?,?,?,?)",
		new Object[]{areaLocationInfo.getid(),areaLocationInfo.getName(),
				areaLocationInfo.getLongitude1(),areaLocationInfo.getLongitude2(),areaLocationInfo.getLongitude3(),areaLocationInfo.getLongitude4(),
				areaLocationInfo.getLatitude1(),areaLocationInfo.getLatitude2(),areaLocationInfo.getLatitude3(),areaLocationInfo.getLatitude4()});
	}
	
	
	/**
	 * 通过AreaLocationInfo对象在数据库里的id修改相应位置信息
	 * @param areaLocationInfo 
	 */
	public void update(AreaLocationInfo areaLocationInfo)
	{
		db = helper.getWritableDatabase();
		db.execSQL("update area set name = ?,longitude1 = ?,longitude2 = ?,longitude3 = ?,longitude4 = ?," 
		+"latitude1 = ?,latitude2 = ?,latitude3 = ?,latitude4 = ? where id = ?",
		new Object[]{areaLocationInfo.getName(),
				areaLocationInfo.getLongitude1(),areaLocationInfo.getLongitude2(),areaLocationInfo.getLongitude3(),areaLocationInfo.getLongitude4(),
				areaLocationInfo.getLatitude1(),areaLocationInfo.getLatitude2(),areaLocationInfo.getLatitude3(),areaLocationInfo.getLatitude4(),
				areaLocationInfo.getid()});
	}
	
	/**
	 * 通过id查找相应的AreaLocationInfo对象
	 * @param id
	 * @return
	 */
	public AreaLocationInfo find(int id)
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select id,name,longitude1,longitude2,longitude3,longitude4," 
		+"latitude1,latitude2,latitude3,latitude4 from area where id = ?",
		new String[]{String.valueOf(id)});
		
		if(cursor.moveToNext())
		{
			return new AreaLocationInfo(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getDouble(cursor.getColumnIndex("longitude1")),
					cursor.getDouble(cursor.getColumnIndex("longitude2")),
					cursor.getDouble(cursor.getColumnIndex("longitude3")),
					cursor.getDouble(cursor.getColumnIndex("longitude4")),
					cursor.getDouble(cursor.getColumnIndex("latitude1")),
					cursor.getDouble(cursor.getColumnIndex("latitude2")),
					cursor.getDouble(cursor.getColumnIndex("latitude3")),
					cursor.getDouble(cursor.getColumnIndex("latitude4")));
		}
		
		return null;
	}
	
	/**
	 * 删除一条或多条AreaLocationInfo对象信息
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
			db.execSQL("delete from area where id in (" + sb + ")", 
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
	public List<AreaLocationInfo> getScrollData(int start,int count)
	{
		List<AreaLocationInfo> infoList = new ArrayList<AreaLocationInfo>();
		db = helper.getWritableDatabase();
		//先得到指针
		Cursor cursor = db.rawQuery("select * from area limit ?,?", 
				new String[]{String.valueOf(start),String.valueOf(count)});
		while(cursor.moveToNext())
		{
			infoList.add(new AreaLocationInfo(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getDouble(cursor.getColumnIndex("longitude1")),
					cursor.getDouble(cursor.getColumnIndex("longitude2")),
					cursor.getDouble(cursor.getColumnIndex("longitude3")),
					cursor.getDouble(cursor.getColumnIndex("longitude4")),
					cursor.getDouble(cursor.getColumnIndex("latitude1")),
					cursor.getDouble(cursor.getColumnIndex("latitude2")),
					cursor.getDouble(cursor.getColumnIndex("latitude3")),
					cursor.getDouble(cursor.getColumnIndex("latitude4"))));
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
		Cursor cursor = db.rawQuery("select count(id) from area", null);//获取信息的记录数
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
		Cursor cursor = db.rawQuery("select max(id) from area", null);
		while(cursor.moveToLast())
		{
			return cursor.getInt(0);
		}
		
		return 0;//如果没有数据则返回0
	}
	
}
