package com.example.location;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;

import com.example.location.database.AreaLocationDAO;
import com.example.location.database.LocationDAO;
import com.example.location.model.LocationInfo;
import com.example.location.service.LocationService;
import com.example.location.service.SendDataServer;

public class StartLayout extends LinearLayout {

	public StartLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public StartLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	private Button button;

	public Intent mIntent;
	
	private static boolean isBound = false;
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		button = (Button) findViewById(R.id.locationBtn);
		
		setOnClickListener();
		
		Bmob.initialize(getContext(), "3233acd4c0aefbdc13bc96792208c71e");
		
	}
	
	private void setOnClickListener()
	{
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!isBound)
				{
					setMaxId();
					mIntent = new Intent(getContext(), LocationService.class);
//					mIntent = new Intent("com.example.location.action.SERVICE");
					getContext().startService(mIntent);
					
					toast("绑定成功");
					
					//利用Handler的sendEmptyMessageDelayed方法，可实现固定时间触发一次上传数据
					timerHandler.sendEmptyMessage(0);
					
					isBound = true;
				}
				else
				{
					toast("已经绑定监听器");
				}
			}

		});
		
	}
	
	/**
	 * 该类是防止清理后台再重启时将重复数据上传
	 */
	private void setMaxId()
	{
		LocationDAO locationDAO = new LocationDAO(getContext());
		AreaLocationDAO areaLocationDAO = new AreaLocationDAO(getContext());
		
		SendDataServer.last_LocationId = locationDAO.getMaxId();
		SendDataServer.last_AreaLocationId = areaLocationDAO.getMaxId();
	}
	
	private Handler timerHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			mIntent = new Intent(getContext(), SendDataServer.class);
			getContext().startService(mIntent);
			
			/**
			 * 在这里修改上传服务器的时间
			 */
			//每过一段时间自动执行一次handleMessage
				//其实就是每隔一段时间给自己发送一个数据以此来触发自己的handleMessage
			//注：1s == 1000; 1min == 60*1000; 1hour == 3600*1000; 1day == 24*3600*1000
				//参数是long类型，所以要在后面加一个L
			timerHandler.sendEmptyMessageDelayed(0, 30*1000L);
			
		}
		
	};
	private void toast(String msg)
	{
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}

}
