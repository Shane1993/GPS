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
	
	//声明LocationManager对象
	private LocationManager locationManager;

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//要新建一个线程来完成任务
		
		//创建LocationManager对象并获取系统的定位服务，注意向下转型
		locationManager = (LocationManager) LocationService.this.getSystemService(Context.LOCATION_SERVICE);
		//设置定位方式为GPS，时间更新的最小时间间隔(意思是多长时间更新用户的位置，单位是毫秒)，
		//两次定位之间的最小位移(意思是触发更新定位所需的最小位移，单位是米)，绑定定位监听器（在接口重写方法里面获取位置）
		//注：对于追踪用户的位置关键就是看中间那两个参数，即更新的最小时间间隔和最小位移
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
			//location.getLongitude()是经度
			System.out.println(location.getLongitude());
			//location.getLatitude()是纬度
			System.out.println(location.getLatitude());
			
			System.out.println("This is LocationService!!");

//			text.setText("经度：" + location.getLongitude() + "纬度：" + location.getLatitude()
//					+ "时间：" + location.getTime() + "速度：" + location.getSpeed());

//			Calendar calendar = Calendar.getInstance();
			Time time = new Time();
			time.setToNow();
			String currentTime = time.format("%Y-%m-%d %H:%M:%S");
			
			//将数据添加进数据库
			LocationDAO locationDAO = new LocationDAO(LocationService.this);
			
			locationInfo = new LocationInfo( locationDAO.getMaxId() + 1 , location.getLongitude(), location.getLatitude(), 
						location.getSpeed(), currentTime
//						calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH)+1 + "-" + calendar.get(Calendar.DAY_OF_MONTH) 
//							+ " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)
							);
			locationDAO.add(locationInfo);
			
			//将接收到的数据广播出去用于区域定位以及刷新信息列表
			Intent intent = new Intent("com.example.location.RECEIVER");
			intent.putExtra("locationInfo", locationInfo);
			sendBroadcast(intent);
			
/*
			//获取两个具有经纬度的点的方法：前面四个参数表示两点的经纬度，最后一个float数组，其中float[0]就是我们要的米的结果
				//Location.distanceBetween(double startLatitude,double startLongitude,double endLatitude,double endLongitude,float[] results)
			//使用例子：
			final float[] results = new float[3];
			Location.distanceBetween(20.12,13.24,34.12,34.12,results);
			System.out.println(results[0]);

*/
			
			//关闭服务
			LocationService.this.stopSelf();
		}
		
	}
	private class TestLocationListener implements LocationListener
	{

		/**
		 * 当设备位置发生改变时就会调用该函数
		 * 通过参数里面的location可得到当前用户的经度和纬度
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
