package com.example.location.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.location.model.AreaLocationInfo;

/**
 * ���������������������ݺ����ݿ�Ĺ�ϵ��
 * 
 * @author lenovo
 *
 */
public class AreaLocationDAO {

	private DatabaseHelper helper;	//����DatabaseHelper����
	private SQLiteDatabase db;		//����SQLiteDatabase����
	
	public AreaLocationDAO(Context context)
	{
		helper = new DatabaseHelper(context);
	}
	

	/**
	 * ���λ����Ϣ
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
	 * ͨ��AreaLocationInfo���������ݿ����id�޸���Ӧλ����Ϣ
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
	 * ͨ��id������Ӧ��AreaLocationInfo����
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
	 * ɾ��һ�������AreaLocationInfo������Ϣ
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
			db.execSQL("delete from area where id in (" + sb + ")", 
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
	public List<AreaLocationInfo> getScrollData(int start,int count)
	{
		List<AreaLocationInfo> infoList = new ArrayList<AreaLocationInfo>();
		db = helper.getWritableDatabase();
		//�ȵõ�ָ��
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
	 * ��ȡ�ܼ�¼��
	 * @return
	 */
	public long getCount()
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(id) from area", null);//��ȡ��Ϣ�ļ�¼��
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
		Cursor cursor = db.rawQuery("select max(id) from area", null);
		while(cursor.moveToLast())
		{
			return cursor.getInt(0);
		}
		
		return 0;//���û�������򷵻�0
	}
	
}
