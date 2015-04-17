package com.example.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainActivity extends Activity {

	
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("This is onCreate");
		
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		tabHost.addTab(tabHost.newTabSpec("start").setIndicator("开始").setContent(R.id.startAtyId));
		tabHost.addTab(tabHost.newTabSpec("area").setIndicator("采集区域").setContent(R.id.areaLayoutId));
		tabHost.addTab(tabHost.newTabSpec("person").setIndicator("记录").setContent(R.id.locationLayoutId));
		
		
		Intent intent = getIntent();
		if(intent.hasExtra("msg"))
		{	
			switch (intent.getIntExtra("msg", 0)) {
			case 0:
				tabHost.setCurrentTab(0);
				break;
			case 1:
				tabHost.setCurrentTab(1);
			default:
				break;
			}
		}
		else
		{
			System.out.println("Extra is null");
		}
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("This is onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("This is onResume");
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("This is onPause");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
		System.out.println("This is onDestroy");
		
	}

	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			System.out.println("This is MainActivity's BACK");
			finish();
			return false;
		}
		
		
		return super.onKeyDown(keyCode, event);
	}

}
