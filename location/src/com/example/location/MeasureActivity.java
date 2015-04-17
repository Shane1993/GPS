package com.example.location;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.location.database.AreaLocationDAO;
import com.example.location.model.AreaLocationInfo;
import com.example.location.model.LocationInfo;

public class MeasureActivity extends Activity implements OnClickListener{

	private Button zuoshangMSBtn,youshangMSBtn,zuoxiaMSBtn,youxiaMSBtn,
				zuoshangOkBtn,youshangOkBtn,zuoxiaOkBtn,youxiaOkBtn,
				measureOkBtn,measureCancelBtn;
	
	private TextView zuoshangTv,youshangTv,zuoxiaTv,youxiaTv;
	private EditText locationNameEt;
	
	private MsgReceiver msgReceiver;
	
	private LocationInfo locationInfo;
	private AreaLocationInfo areaLocationInfo = new AreaLocationInfo();
	
	//设置常量用来便于判断是当前哪个角落需要定位显示
	private static final int KEY_ZUOSHANGTV = 1;
	private static final int KEY_YOUSHANGTV = 2;
	private static final int KEY_ZUOXIATV = 3;
	private static final int KEY_YOUXIATV = 4;
	private static final int KEY_NO_SELECT = 0;
	
	private boolean VALUE_ZUOSHANGTV = false;
	private boolean VALUE_YOUSHANGTV = false;
	private boolean VALUE_ZUOXIATV = false;
	private boolean VALUE_YOUXIATV = false;
	
	private int selectKey = KEY_NO_SELECT;
	
	private int msg;	//设置变量用来判断是从哪个入口进入这个界面的
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure);
		
		Init();
		
		Intent intent = getIntent();
		msg = intent.getIntExtra("msg", 0);
		
		switch (msg) {
		case 0:
			
			break;
		//如果是1则说明是通过点击选项进入这个界面的，所以要从数据库中读取相应的数据显示出来
		case 1:
			//修改文字信息
			zuoshangMSBtn.setText("重新定位");
			youshangMSBtn.setText("重新定位");
			zuoxiaMSBtn.setText("重新定位");
			youxiaMSBtn.setText("重新定位");
			measureOkBtn.setText("修改");
			
			//修改变量说明都已经有值了
			VALUE_ZUOSHANGTV = true;
			VALUE_YOUSHANGTV = true;
			VALUE_ZUOXIATV = true;
			VALUE_YOUXIATV = true;
			
			//从数据库里面读取相关的数据，先从intent中获取id信息
			int id = intent.getIntExtra("id", 0);
			AreaLocationDAO areaLocationDAO = new AreaLocationDAO(MeasureActivity.this);
			areaLocationInfo = areaLocationDAO.find(id);
			//开始显示相应信息
			zuoshangTv.setText("经度：" + areaLocationInfo.getLongitude1() + "\n纬度：" + areaLocationInfo.getLatitude1());
			youshangTv.setText("经度：" + areaLocationInfo.getLongitude2() + "\n纬度：" + areaLocationInfo.getLatitude2());			
			zuoxiaTv.setText("经度：" + areaLocationInfo.getLongitude3() + "\n纬度：" + areaLocationInfo.getLatitude3());
			youxiaTv.setText("经度：" + areaLocationInfo.getLongitude4() + "\n纬度：" + areaLocationInfo.getLatitude4());
			locationNameEt.setText(areaLocationInfo.getName());
			
		}
		
		msgReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.location.RECEIVER");
		registerReceiver(msgReceiver, intentFilter);
		
	}

	private class MsgReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			locationInfo = (LocationInfo) intent.getSerializableExtra("locationInfo");
			
			switch (selectKey) {
			case KEY_ZUOSHANGTV:
				zuoshangTv.setText("经度：" + locationInfo.getLongitude() + "\n纬度：" + locationInfo.getLatitude());
				break;
			case KEY_YOUSHANGTV:
				youshangTv.setText("经度：" + locationInfo.getLongitude() + "\n纬度：" + locationInfo.getLatitude());			
				break;
			case KEY_ZUOXIATV:
				zuoxiaTv.setText("经度：" + locationInfo.getLongitude() + "\n纬度：" + locationInfo.getLatitude());
				break;
			case KEY_YOUXIATV:
				youxiaTv.setText("经度：" + locationInfo.getLongitude() + "\n纬度：" + locationInfo.getLatitude());
				break;
			default:
				break;
			}
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			System.out.println("This is BACK");
			//退出时记得注销这个Activity的接收器
			unregisterReceiver(msgReceiver);
			finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}

	

	/**
	 * 初始化函数
	 */
	private void Init()
	{
		zuoshangMSBtn = (Button) findViewById(R.id.zuoshangMSBtn);
		youshangMSBtn = (Button) findViewById(R.id.youshangMSBtn);
		zuoxiaMSBtn = (Button) findViewById(R.id.zuoxiaMSBtn);
		youxiaMSBtn = (Button) findViewById(R.id.youxiaMSBtn);
		
		zuoshangOkBtn = (Button) findViewById(R.id.zuoshangOkBtn);
		youshangOkBtn = (Button) findViewById(R.id.youshangOkBtn);
		zuoxiaOkBtn = (Button) findViewById(R.id.zuoxiaOkBtn);
		youxiaOkBtn = (Button) findViewById(R.id.youxiaOkBtn);
		
		measureOkBtn = (Button) findViewById(R.id.measureOkBtn);
		measureCancelBtn = (Button) findViewById(R.id.measureCancelBtn);
		
		zuoshangTv = (TextView) findViewById(R.id.zuoshangTv);
		youshangTv = (TextView) findViewById(R.id.youshangTv);
		zuoxiaTv = (TextView) findViewById(R.id.zuoxiaTv);
		youxiaTv = (TextView) findViewById(R.id.youxiaTv);
		
		locationNameEt = (EditText) findViewById(R.id.locationNameEt);
		
		zuoshangMSBtn.setOnClickListener(this);
		youshangMSBtn.setOnClickListener(this);
		zuoxiaMSBtn.setOnClickListener(this);
		youxiaMSBtn.setOnClickListener(this);
		zuoshangOkBtn.setOnClickListener(this);
		youshangOkBtn.setOnClickListener(this);
		zuoxiaOkBtn.setOnClickListener(this);
		youxiaOkBtn.setOnClickListener(this);
		measureOkBtn.setOnClickListener(this);
		measureCancelBtn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zuoshangMSBtn:
			
			selectKey = KEY_ZUOSHANGTV;
			
			clearText();
			
			//判断之前是否已经定位过，如果已经定位过则说明现在是重新定位，所以要清除标志位
			if(VALUE_ZUOSHANGTV)
			{
				VALUE_ZUOSHANGTV = false;
			}
			
			break;
		case R.id.youshangMSBtn:
			
			selectKey = KEY_YOUSHANGTV;
				
			clearText();
			
			if(VALUE_YOUSHANGTV)
			{
				VALUE_YOUSHANGTV = false;
			}
			
			break;
		case R.id.zuoxiaMSBtn:
			
			selectKey = KEY_ZUOXIATV;
			
			clearText();
			
			if(VALUE_ZUOXIATV)
			{
				VALUE_ZUOXIATV = false;
			}
			
			break;
		case R.id.youxiaMSBtn:
			
			selectKey = KEY_YOUXIATV;
			
			clearText();
			
			if(VALUE_YOUXIATV)
			{
				VALUE_YOUXIATV = false;
			}
			
			break;
		case R.id.zuoshangOkBtn:
			
			if(zuoshangTv.getText().toString().isEmpty())
			{
				Toast.makeText(MeasureActivity.this, "请先进行定位", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				zuoshangMSBtn.setText("重新定位");
				//修改变量说明定位成功
				VALUE_ZUOSHANGTV = true;
				//隐藏确定按键
				zuoshangOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youshangOkBtn:
			
			if(youshangTv.getText().toString().isEmpty())
			{
				Toast.makeText(MeasureActivity.this, "请先进行定位", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				youshangMSBtn.setText("重新定位");
				//修改变量说明定位成功
				VALUE_YOUSHANGTV = true;
				youshangOkBtn.setVisibility(View.INVISIBLE);
			}	
			break;
		case R.id.zuoxiaOkBtn:
			
			if(zuoxiaTv.getText().toString().isEmpty())
			{
				Toast.makeText(MeasureActivity.this, "请先进行定位", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				zuoxiaMSBtn.setText("重新定位");
				//修改变量说明定位成功
				VALUE_ZUOXIATV = true;
				zuoxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youxiaOkBtn:
			
			if(youxiaTv.getText().toString().isEmpty())
			{
				Toast.makeText(MeasureActivity.this, "请先进行定位", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				youxiaMSBtn.setText("重新定位");
				//修改变量说明定位成功
				VALUE_YOUXIATV = true;
				youxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.measureOkBtn:
			
			if(locationNameEt.getText().toString().isEmpty() )
			{
				Toast.makeText(MeasureActivity.this, "请输入相应的名称", Toast.LENGTH_SHORT).show();
			}
			else if( !isPrepared() )
			{
				Toast.makeText(MeasureActivity.this, "请先全部定位", Toast.LENGTH_SHORT).show();
			}
			else
			{
				AreaLocationDAO areaLocationDAO = new AreaLocationDAO(MeasureActivity.this);
				//使用最新的数据
				areaLocationInfo.setName(locationNameEt.getText().toString());
				areaLocationInfo.setLongitude1(locationInfo.getLongitude());
				areaLocationInfo.setLatitude1(locationInfo.getLatitude());
				areaLocationInfo.setLongitude2(locationInfo.getLongitude());
				areaLocationInfo.setLatitude2(locationInfo.getLatitude());
				areaLocationInfo.setLongitude3(locationInfo.getLongitude());
				areaLocationInfo.setLatitude3(locationInfo.getLatitude());
				areaLocationInfo.setLongitude4(locationInfo.getLongitude());
				areaLocationInfo.setLatitude4(locationInfo.getLatitude());
				
				switch (msg) {
				//如果是0则说明现在是创建数据
				case 0:
					areaLocationInfo.setid(areaLocationDAO.getMaxId() + 1);
					//将数据添加进数据库
					areaLocationDAO.add(areaLocationInfo);
					Toast.makeText(MeasureActivity.this, "数据添加成功", Toast.LENGTH_SHORT).show();
					break;
				//如果是1则说明现在是修改数据
				case 1:
					//更新数据库中的数据
					areaLocationDAO.update(areaLocationInfo);
					Toast.makeText(MeasureActivity.this, "数据修改成功", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				
				//退出时记得注销这个Activity的接收器
				unregisterReceiver(msgReceiver);
				
				Intent intent = new Intent(MeasureActivity.this,MainActivity.class);
				//设置携带数据，为了能在确定时启动的是Mainactivity中Tab中的第二个界面
				intent.putExtra("msg", 1);
				startActivity(intent);
				//跳转界面后也要把这个界面关闭掉
				finish();
				
			}
			
			break;
		case R.id.measureCancelBtn:
			
			//退出时记得注销这个Activity的接收器
			unregisterReceiver(msgReceiver);
			finish();
			break;
			
		default:
			break;
		}
	}

	/**
	 * 用于清除还没确定定位的数据
	 */
	private void clearText()
	{
		if(!VALUE_ZUOSHANGTV)
		{
			zuoshangTv.setText("");
		}
		if(!VALUE_YOUSHANGTV)
		{
			youshangTv.setText("");
		}
		if(!VALUE_ZUOXIATV)
		{
			zuoxiaTv.setText("");
		}
		if(!VALUE_YOUXIATV)
		{
			youxiaTv.setText("");
		}
		
		switch (selectKey) {
		case KEY_ZUOSHANGTV:
			
			zuoshangOkBtn.setVisibility(View.VISIBLE);
			youshangOkBtn.setVisibility(View.INVISIBLE);
			zuoxiaOkBtn.setVisibility(View.INVISIBLE);
			youxiaOkBtn.setVisibility(View.INVISIBLE);
			
			break;
		case KEY_YOUSHANGTV:
				
			youshangOkBtn.setVisibility(View.VISIBLE);
			zuoshangOkBtn.setVisibility(View.INVISIBLE);
			zuoxiaOkBtn.setVisibility(View.INVISIBLE);
			youxiaOkBtn.setVisibility(View.INVISIBLE);
			
			break;
		case KEY_ZUOXIATV:
			
			zuoxiaOkBtn.setVisibility(View.VISIBLE);
			zuoshangOkBtn.setVisibility(View.INVISIBLE);
			youshangOkBtn.setVisibility(View.INVISIBLE);
			youxiaOkBtn.setVisibility(View.INVISIBLE);
			
			break;
		case KEY_YOUXIATV:
			
			youxiaOkBtn.setVisibility(View.VISIBLE);
			zuoshangOkBtn.setVisibility(View.INVISIBLE);
			youshangOkBtn.setVisibility(View.INVISIBLE);
			zuoxiaOkBtn.setVisibility(View.INVISIBLE);
			
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 判断是否所有方位都定位了
	 * @return
	 */
	private boolean isPrepared()
	{
		//只要有一个没有定位就返回false
		return VALUE_ZUOSHANGTV && VALUE_YOUSHANGTV && VALUE_ZUOXIATV && VALUE_YOUXIATV;
	}
	
}
