package com.example.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.location.database.AreaLocationDAO;
import com.example.location.model.AreaLocationInfo;
import com.example.location.model.LocationInfo;
import com.example.location.service.SendDataServer;

public class MeasureActivity extends Activity implements OnClickListener {

	private Button zuoshangMSBtn, youshangMSBtn, zuoxiaMSBtn, youxiaMSBtn,
			zuoshangOkBtn, youshangOkBtn, zuoxiaOkBtn, youxiaOkBtn,
			measureOkBtn, measureCancelBtn;

	private TextView zuoshangTv, youshangTv, zuoxiaTv, youxiaTv;
	private EditText locationNameEt;

	private MsgReceiver msgReceiver;

	// 用来接收GPS传回来的数据
	private LocationInfo locationInfo;
	// 用来记录每个角落的坐标点
	private LocationInfo locationInfo1, locationInfo2, locationInfo3,
			locationInfo4;
	private AreaLocationInfo areaLocationInfo = new AreaLocationInfo();

	// 设置常量用来便于判断是当前哪个角落需要定位显示
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

	// 用来避免所有人都能够采集区域信息
	public static final String password = "123456";

	private int msg; // 设置变量用来判断是从哪个入口进入这个界面的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure);

		Init();

		Intent intent = getIntent();
		msg = intent.getIntExtra("msg", 0);

		switch (msg) {
		// 0代表创建区域位置
		case 0:
			// 初始化
			locationInfo1 = new LocationInfo();
			locationInfo2 = new LocationInfo();
			locationInfo3 = new LocationInfo();
			locationInfo4 = new LocationInfo();

			break;
		// 如果是1则说明是通过点击选项进入这个界面的，所以要从数据库中读取相应的数据显示出来
		case 1:
			
			// 从数据库里面读取相关的数据，先从intent中获取id信息
			int id = intent.getIntExtra("id", 0);
			AreaLocationDAO areaLocationDAO = new AreaLocationDAO(
					MeasureActivity.this);
			areaLocationInfo = areaLocationDAO.find(id);
			
			// 开始显示相应信息
			zuoshangTv.setText("经度：" + areaLocationInfo.getLongitude1()
					+ "\n纬度：" + areaLocationInfo.getLatitude1());
			youshangTv.setText("经度：" + areaLocationInfo.getLongitude2()
					+ "\n纬度：" + areaLocationInfo.getLatitude2());
			zuoxiaTv.setText("经度：" + areaLocationInfo.getLongitude3() + "\n纬度："
					+ areaLocationInfo.getLatitude3());
			youxiaTv.setText("经度：" + areaLocationInfo.getLongitude4() + "\n纬度："
					+ areaLocationInfo.getLatitude4());
			locationNameEt.setText(areaLocationInfo.getName());
			
			
			// 修改文字信息
			zuoshangMSBtn.setVisibility(View.INVISIBLE);
			youshangMSBtn.setVisibility(View.INVISIBLE);
			zuoxiaMSBtn.setVisibility(View.INVISIBLE);
			youxiaMSBtn.setVisibility(View.INVISIBLE);
			measureOkBtn.setVisibility(View.INVISIBLE);

//			// 修改变量说明都已经有值了
//			VALUE_ZUOSHANGTV = true;
//			VALUE_YOUSHANGTV = true;
//			VALUE_ZUOXIATV = true;
//			VALUE_YOUXIATV = true;
//			
//			// 初始化
//			locationInfo1 = new LocationInfo();
//			locationInfo1.setLongitude(areaLocationInfo.getLongitude1());
//			locationInfo1.setLatitude(areaLocationInfo.getLatitude1());
//			locationInfo2 = new LocationInfo();
//			locationInfo2.setLongitude(areaLocationInfo.getLongitude2());
//			locationInfo2.setLatitude(areaLocationInfo.getLatitude2());
//			locationInfo3 = new LocationInfo();
//			locationInfo3.setLongitude(areaLocationInfo.getLongitude3());
//			locationInfo3.setLatitude(areaLocationInfo.getLatitude3());
//			locationInfo4 = new LocationInfo();
//			locationInfo4.setLongitude(areaLocationInfo.getLongitude4());
//			locationInfo4.setLatitude(areaLocationInfo.getLatitude4());

		}

		msgReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.location.RECEIVER");
		registerReceiver(msgReceiver, intentFilter);

	}

	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			locationInfo = (LocationInfo) intent
					.getSerializableExtra("locationInfo");

			switch (selectKey) {
			case KEY_ZUOSHANGTV:
				locationInfo1.setLongitude(locationInfo.getLongitude());
				locationInfo1.setLatitude(locationInfo.getLatitude());
				zuoshangTv.setText("经度：" + locationInfo.getLongitude()
						+ "\n纬度：" + locationInfo.getLatitude());
				break;
			case KEY_YOUSHANGTV:
				locationInfo2.setLongitude(locationInfo.getLongitude());
				locationInfo2.setLatitude(locationInfo.getLatitude());
				youshangTv.setText("经度：" + locationInfo.getLongitude()
						+ "\n纬度：" + locationInfo.getLatitude());
				break;
			case KEY_ZUOXIATV:
				locationInfo3.setLongitude(locationInfo.getLongitude());
				locationInfo3.setLatitude(locationInfo.getLatitude());
				zuoxiaTv.setText("经度：" + locationInfo.getLongitude() + "\n纬度："
						+ locationInfo.getLatitude());
				break;
			case KEY_YOUXIATV:
				locationInfo4.setLongitude(locationInfo.getLongitude());
				locationInfo4.setLatitude(locationInfo.getLatitude());
				youxiaTv.setText("经度：" + locationInfo.getLongitude() + "\n纬度："
						+ locationInfo.getLatitude());
				break;
			default:
				break;
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("This is BACK");
			// 退出时记得注销这个Activity的接收器
			unregisterReceiver(msgReceiver);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化函数
	 */
	private void Init() {
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

			// 判断之前是否已经定位过，如果已经定位过则说明现在是重新定位，所以要清除标志位
			if (VALUE_ZUOSHANGTV) {
				VALUE_ZUOSHANGTV = false;
			}

			break;
		case R.id.youshangMSBtn:

			selectKey = KEY_YOUSHANGTV;

			clearText();

			if (VALUE_YOUSHANGTV) {
				VALUE_YOUSHANGTV = false;
			}

			break;
		case R.id.zuoxiaMSBtn:

			selectKey = KEY_ZUOXIATV;

			clearText();

			if (VALUE_ZUOXIATV) {
				VALUE_ZUOXIATV = false;
			}

			break;
		case R.id.youxiaMSBtn:

			selectKey = KEY_YOUXIATV;

			clearText();

			if (VALUE_YOUXIATV) {
				VALUE_YOUXIATV = false;
			}

			break;
		case R.id.zuoshangOkBtn:

			if (zuoshangTv.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "请先进行定位",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				zuoshangMSBtn.setText("重新定位");
				// 修改变量说明定位成功
				VALUE_ZUOSHANGTV = true;
				// 隐藏确定按键
				zuoshangOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youshangOkBtn:

			if (youshangTv.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "请先进行定位",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				youshangMSBtn.setText("重新定位");
				// 修改变量说明定位成功
				VALUE_YOUSHANGTV = true;
				youshangOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.zuoxiaOkBtn:

			if (zuoxiaTv.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "请先进行定位",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				zuoxiaMSBtn.setText("重新定位");
				// 修改变量说明定位成功
				VALUE_ZUOXIATV = true;
				zuoxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youxiaOkBtn:

			if (youxiaTv.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "请先进行定位",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				youxiaMSBtn.setText("重新定位");
				// 修改变量说明定位成功
				VALUE_YOUXIATV = true;
				youxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.measureOkBtn:

			if (locationNameEt.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "请输入相应的名称",
						Toast.LENGTH_SHORT).show();
			} else if (!isPrepared()) {
				Toast.makeText(MeasureActivity.this, "请先全部定位",
						Toast.LENGTH_SHORT).show();
			} else {
				// 判断之前是否输入过密码
				if (getCachePassword() == null) {

					// 有密码才能改
					final EditText editText = new EditText(MeasureActivity.this);
					editText.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					final AlertDialog ad = new AlertDialog.Builder(
							MeasureActivity.this).create();
					ad.setView(editText);
					ad.setTitle("密码");
					ad.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									ad.dismiss();
								}
							});
					ad.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (editText.getText().toString()
											.equals(password)) {
										// 缓存密码
										cachePassword(password);

										modifyData();

										// 退出时记得注销这个Activity的接收器
										unregisterReceiver(msgReceiver);

										// Intent intent = new
										// Intent(MeasureActivity.this,MainActivity.class);
										// //设置携带数据，为了能在确定时启动的是Mainactivity中Tab中的第二个界面
										// intent.putExtra("msg", 1);
										// startActivity(intent);
										// 跳转界面后也要把这个界面关闭掉
										finish();
									} else {
										Toast.makeText(MeasureActivity.this,
												"密码错误", Toast.LENGTH_SHORT)
												.show();
									}
									ad.dismiss();
								}
							});

					ad.show();

				} else {
					modifyData();

					// 退出时记得注销这个Activity的接收器
					unregisterReceiver(msgReceiver);

					// Intent intent = new
					// Intent(MeasureActivity.this,MainActivity.class);
					// //设置携带数据，为了能在确定时启动的是Mainactivity中Tab中的第二个界面
					// intent.putExtra("msg", 1);
					// startActivity(intent);
					// 跳转界面后也要把这个界面关闭掉
					finish();
				}
			}

			break;
		case R.id.measureCancelBtn:

			// 退出时记得注销这个Activity的接收器
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
	private void clearText() {
		if (!VALUE_ZUOSHANGTV) {
			zuoshangTv.setText("");
		}
		if (!VALUE_YOUSHANGTV) {
			youshangTv.setText("");
		}
		if (!VALUE_ZUOXIATV) {
			zuoxiaTv.setText("");
		}
		if (!VALUE_YOUXIATV) {
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
	 * 
	 * @return
	 */
	private boolean isPrepared() {
		// 只要有一个没有定位就返回false
		return VALUE_ZUOSHANGTV && VALUE_YOUSHANGTV && VALUE_ZUOXIATV
				&& VALUE_YOUXIATV;
	}

	/**
	 * 用来缓存密码
	 */
	private void cachePassword(String password) {
		Editor editor = getSharedPreferences("GPSLocation",
				Context.MODE_PRIVATE).edit();
		editor.putString("password", password);
		editor.apply();
	}

	/**
	 * 用来缓存密码
	 */
	private String getCachePassword() {
		return getSharedPreferences("GPSLocation", Context.MODE_PRIVATE)
				.getString("password", null);
	}

	private void modifyData() {
		AreaLocationDAO areaLocationDAO = new AreaLocationDAO(
				MeasureActivity.this);
		// 使用最新的数据
		areaLocationInfo.setName(locationNameEt.getText().toString());
		areaLocationInfo.setLongitude1(locationInfo1.getLongitude());
		areaLocationInfo.setLatitude1(locationInfo1.getLatitude());
		areaLocationInfo.setLongitude2(locationInfo2.getLongitude());
		areaLocationInfo.setLatitude2(locationInfo2.getLatitude());
		areaLocationInfo.setLongitude3(locationInfo3.getLongitude());
		areaLocationInfo.setLatitude3(locationInfo3.getLatitude());
		areaLocationInfo.setLongitude4(locationInfo4.getLongitude());
		areaLocationInfo.setLatitude4(locationInfo4.getLatitude());

		switch (msg) {
		// 如果是0则说明现在是创建数据
		case 0:
			areaLocationInfo.setid(areaLocationDAO.getMaxId() + 1);
			//将数据上传至服务器
			areaLocationInfo.save(MeasureActivity.this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					System.out.println("上传成功");
					Toast.makeText(MeasureActivity.this, "上传成功",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("上传失败：" + arg1);
					Toast.makeText(MeasureActivity.this, arg1,
							Toast.LENGTH_SHORT).show();
				}
			});
			break;
		// 如果是1则说明现在是修改数据，去除掉该功能
		case 1:
			
//			//将本地的数据库更新
//			areaLocationDAO.update(areaLocationInfo);
//			//将服务器的数据更新
//			areaLocationInfo.update(MeasureActivity.this,new UpdateListener() {
//				
//				@Override
//				public void onSuccess() {
//					System.out.println("更新成功");
//					Toast.makeText(MeasureActivity.this, "更新成功",
//							Toast.LENGTH_SHORT).show();
//				}
//				
//				@Override
//				public void onFailure(int arg0, String arg1) {
//					// TODO Auto-generated method stub
//					System.out.println("上传失败：" + arg1);
//					Toast.makeText(MeasureActivity.this, arg1,
//							Toast.LENGTH_SHORT).show();
//				}
//			});
//			break;
//		default:
//			break;
		}
	}
}
