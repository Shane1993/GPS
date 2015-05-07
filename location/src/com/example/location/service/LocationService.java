package com.example.location.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;

import com.example.location.database.AreaLocationDAO;
import com.example.location.database.LocationDAO;
import com.example.location.model.AreaLocationInfo;
import com.example.location.model.LocationInfo;

public class LocationService extends Service{

	
	private LocationInfo locationInfo = null;
	
	//����LocationManager����
	private LocationManager locationManager;

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		//����LocationManager���󲢻�ȡϵͳ�Ķ�λ����ע������ת��
		locationManager = (LocationManager) LocationService.this.getSystemService(Context.LOCATION_SERVICE);
		//���ö�λ��ʽΪGPS��ʱ����µ���Сʱ����(��˼�Ƕ೤ʱ������û���λ�ã���λ�Ǻ���)��
		//���ζ�λ֮�����Сλ��(��˼�Ǵ������¶�λ�������Сλ�ƣ���λ����)���󶨶�λ���������ڽӿ���д���������ȡλ�ã�
		//ע������׷���û���λ�ùؼ����ǿ��м������������������µ���Сʱ��������Сλ��
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,new TestLocationListener());

		System.out.println("Service is started");
		
		return super.onStartCommand(intent, flags, startId);
	}

	class GetGpsThread implements Runnable
	{

		private Location location;
		
		public GetGpsThread(Location location) {
			super();
			this.location = location;
		}

		@Override
		public void run() {
			
			// TODO Auto-generated method stub
			//location.getLongitude()�Ǿ���
			System.out.println(location.getLongitude());
			//location.getLatitude()��γ��
			System.out.println(location.getLatitude());
			
			System.out.println("This is LocationService!!");

//			text.setText("���ȣ�" + location.getLongitude() + "γ�ȣ�" + location.getLatitude()
//					+ "ʱ�䣺" + location.getTime() + "�ٶȣ�" + location.getSpeed());

//			Calendar calendar = Calendar.getInstance();
			Time time = new Time();
			time.setToNow();
			String currentTime = time.format("%Y-%m-%d %H:%M:%S");
			
//			System.out.println(currentTime);
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			String areaName = getAreaName(longitude,latitude);
			
			//��������ӽ����ݿ�
			LocationDAO locationDAO = new LocationDAO(LocationService.this);
			
			locationInfo = new LocationInfo( locationDAO.getMaxId() + 1 , location.getLongitude(), location.getLatitude(), 
						location.getSpeed(), currentTime, areaName);
//			System.out.println(locationInfo.toString());
			
			locationDAO.add(locationInfo);
			System.out.println("This is before put Intent " + locationInfo.toString());
			//�����յ������ݹ㲥��ȥ��������λ�Լ�ˢ����Ϣ�б�
			Intent intent = new Intent("com.example.location.RECEIVER");
			intent.putExtra("locationInfo", locationInfo);
			sendBroadcast(intent);
			System.out.println("This is after put Intent " + locationInfo.toString());
			
/*
			//��ȡ�������о�γ�ȵĵ�ķ�����ǰ���ĸ�������ʾ����ľ�γ�ȣ����һ��float���飬����float[0]��������Ҫ���׵Ľ��
				//Location.distanceBetween(double startLatitude,double startLongitude,double endLatitude,double endLongitude,float[] results)
			//ʹ�����ӣ�
			final float[] results = new float[3];
			Location.distanceBetween(20.12,13.24,34.12,34.12,results);
			System.out.println(results[0]);

*/
			
//			//�رշ���
//			LocationService.this.stopSelf();
		}

		
	}
	
	//���ݾ�γ���ж���������
	private String getAreaName(double longitude, double latitude) {
		// TODO Auto-generated method stub
		AreaLocationDAO areaLocationDAO = new AreaLocationDAO(LocationService.this);
		//��¼��С�����ľ��Ȼ�����С������γ��
		double minLongitude = 0.0, maxLongitude = 0.0;
		double minLatitude = 0.0, maxLatitude = 0.0;
		
		List<AreaLocationInfo> list = areaLocationDAO.getScrollData(0, (int)areaLocationDAO.getCount());
		//��forѭ�������б�һ������ѯ��λ���ǲ��Ǵ��ڸ�������
		for(AreaLocationInfo info : list)
		{
			//��ȡ��С�ľ���
			double minLongitude12 = Math.min(info.getLongitude1(), info.getLongitude2());
			double minLongitude34 = Math.min(info.getLongitude3(), info.getLongitude4());
			minLongitude = Math.min(minLongitude12, minLongitude34);
			
			//��ȡ��С��γ��
			double minLatitude12 = Math.min(info.getLatitude1(), info.getLatitude2());
			double minLatitude34 = Math.min(info.getLatitude3(), info.getLatitude4());
			minLatitude = Math.min(minLatitude12, minLatitude34);
			
			//��ȡ���ľ���
			double maxLongitude12 = Math.max(info.getLongitude1(), info.getLongitude2());
			double maxLongitude34 = Math.max(info.getLongitude3(), info.getLongitude4());
			maxLongitude = Math.max(maxLongitude12, maxLongitude34);
			
			//��ȡ����γ��
			double maxLatitude12 = Math.max(info.getLatitude1(), info.getLatitude2());
			double maxLatitude34 = Math.max(info.getLatitude3(), info.getLatitude4());
			maxLatitude = Math.max(maxLatitude12, maxLatitude34);
			
			//��ʼ�жϸþ�γ���Ƿ��ڸ�������
			if((longitude > minLongitude) && (longitude < maxLongitude) &&
					(latitude > minLatitude) && (latitude < maxLatitude))
			{
				//���ظ���������
				return info.getName();
			}
		}
		//���ȫ���Ҳ���˵��������ĳ������������
		return "δ�ɼ�����";
	}
	
	private class TestLocationListener implements LocationListener
	{

		/**
		 * ���豸λ�÷����ı�ʱ�ͻ���øú���
		 * ͨ�����������location�ɵõ���ǰ�û��ľ��Ⱥ�γ��
		 */
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			new Thread(new GetGpsThread(location)).start();
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

	
	
}
