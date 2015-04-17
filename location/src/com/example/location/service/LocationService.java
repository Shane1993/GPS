package com.example.location.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;

import com.example.location.database.LocationDAO;
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
		//Ҫ�½�һ���߳����������
		
		//����LocationManager���󲢻�ȡϵͳ�Ķ�λ����ע������ת��
		locationManager = (LocationManager) LocationService.this.getSystemService(Context.LOCATION_SERVICE);
		//���ö�λ��ʽΪGPS��ʱ����µ���Сʱ����(��˼�Ƕ೤ʱ������û���λ�ã���λ�Ǻ���)��
		//���ζ�λ֮�����Сλ��(��˼�Ǵ������¶�λ�������Сλ�ƣ���λ����)���󶨶�λ���������ڽӿ���д���������ȡλ�ã�
		//ע������׷���û���λ�ùؼ����ǿ��м������������������µ���Сʱ��������Сλ��
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5000,new TestLocationListener());

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
			
			//��������ӽ����ݿ�
			LocationDAO locationDAO = new LocationDAO(LocationService.this);
			
			locationInfo = new LocationInfo( locationDAO.getMaxId() + 1 , location.getLongitude(), location.getLatitude(), 
						location.getSpeed(), currentTime
//						calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH)+1 + "-" + calendar.get(Calendar.DAY_OF_MONTH) 
//							+ " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)
							);
			locationDAO.add(locationInfo);
			
			//�����յ������ݹ㲥��ȥ��������λ�Լ�ˢ����Ϣ�б�
			Intent intent = new Intent("com.example.location.RECEIVER");
			intent.putExtra("locationInfo", locationInfo);
			sendBroadcast(intent);
			
/*
			//��ȡ�������о�γ�ȵĵ�ķ�����ǰ���ĸ�������ʾ����ľ�γ�ȣ����һ��float���飬����float[0]��������Ҫ���׵Ľ��
				//Location.distanceBetween(double startLatitude,double startLongitude,double endLatitude,double endLongitude,float[] results)
			//ʹ�����ӣ�
			final float[] results = new float[3];
			Location.distanceBetween(20.12,13.24,34.12,34.12,results);
			System.out.println(results[0]);

*/
			
			//�رշ���
			LocationService.this.stopSelf();
		}
		
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
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("LocationServer is stop");
		
	}

	
	
}
