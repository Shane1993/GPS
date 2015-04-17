package com.example.location.service;

import java.util.List;

import cn.bmob.v3.listener.SaveListener;

import com.example.location.database.AreaLocationDAO;
import com.example.location.database.LocationDAO;
import com.example.location.model.AreaLocationInfo;
import com.example.location.model.LocationInfo;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class SendDataServer extends IntentService {

	public SendDataServer() {
		super("HelloIntentServer");
		// TODO Auto-generated constructor stub
	}

	//记录上一次上传的信息Id，这里一定要用静态变量static，不然每次上传一次数据变量又变回0，造成重复上传
	public static int last_LocationId = 0;
	public static int last_AreaLocationId = 0;
		
	//手机唯一识别码，用于上传之后便于分析不同的设备的行为
	String deviceId;
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		System.out.println("last_LocationId :" + last_LocationId + "\nlast_AreaLocationId :" + last_AreaLocationId);
		
		LocationDAO locationDAO = new LocationDAO(SendDataServer.this);
		AreaLocationDAO areaLocationDAO = new AreaLocationDAO(SendDataServer.this);
		
		List<LocationInfo> locationList = locationDAO.getScrollData(last_LocationId, locationDAO.getMaxId()-last_LocationId);
		List<AreaLocationInfo> areaLocationList = areaLocationDAO.getScrollData(last_AreaLocationId, areaLocationDAO.getMaxId()-last_AreaLocationId);
		
		deviceId = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		
		
		for(LocationInfo locationInfo : locationList)
		{
			locationInfo.setDeviceId(deviceId);
			
			locationInfo.save(SendDataServer.this,new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					System.out.println("上传成功");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("上传失败：" + arg1);
				}
			});
			System.out.println(locationInfo.getid() + "\n" + locationInfo.toString());
		}
		last_LocationId = locationDAO.getMaxId();
		
		for(AreaLocationInfo areaLocationInfo : areaLocationList)
		{
			areaLocationInfo.save(SendDataServer.this,new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					System.out.println("上传成功");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("上传失败：" + arg1);
				}
			});
			System.out.println(areaLocationInfo.getid() + "\n" + areaLocationInfo.toString());
		}
		last_AreaLocationId = areaLocationDAO.getMaxId();
		
		System.out.println("last_LocationId :" + last_LocationId + "\nlast_AreaLocationId :" + last_AreaLocationId);
		System.out.println("This is SendDataServer");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("SendDataServer is stop");
	}

	
	
}
