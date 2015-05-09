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
import android.widget.Toast;

public class SendDataServer extends IntentService {

	public SendDataServer() {
		super("HelloIntentServer");
		// TODO Auto-generated constructor stub
	}

	//��¼��һ���ϴ�����ϢId������һ��Ҫ�þ�̬����static����Ȼÿ���ϴ�һ�����ݱ����ֱ��0������ظ��ϴ�
	public static int last_LocationId = 0;
	public static int last_AreaLocationId = 0;
		
	//�ֻ�Ψһʶ���룬�����ϴ�֮����ڷ�����ͬ���豸����Ϊ
	String deviceId;
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		System.out.println("last_LocationId :" + last_LocationId + "\nlast_AreaLocationId :" + last_AreaLocationId);
		
		final LocationDAO locationDAO = new LocationDAO(SendDataServer.this);
		final AreaLocationDAO areaLocationDAO = new AreaLocationDAO(SendDataServer.this);
		
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
					System.out.println("�ϴ��ɹ�");
					last_LocationId ++;
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("�ϴ�ʧ�ܣ�" + arg1);
					Toast.makeText(SendDataServer.this, arg1, Toast.LENGTH_SHORT).show();
				}
			});
			System.out.println(locationInfo.getid() + "\n" + locationInfo.toString());
		}
		
		for(AreaLocationInfo areaLocationInfo : areaLocationList)
		{
			areaLocationInfo.save(SendDataServer.this,new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					System.out.println("�ϴ��ɹ�");
					Toast.makeText(SendDataServer.this, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
					last_AreaLocationId ++;
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("�ϴ�ʧ�ܣ�" + arg1);
					Toast.makeText(SendDataServer.this, arg1, Toast.LENGTH_SHORT).show();
				}
			});
			System.out.println(areaLocationInfo.getid() + "\n" + areaLocationInfo.toString());
		}
		
		
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
