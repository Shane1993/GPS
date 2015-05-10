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

	// ��������GPS������������
	private LocationInfo locationInfo;
	// ������¼ÿ������������
	private LocationInfo locationInfo1, locationInfo2, locationInfo3,
			locationInfo4;
	private AreaLocationInfo areaLocationInfo = new AreaLocationInfo();

	// ���ó������������ж��ǵ�ǰ�ĸ�������Ҫ��λ��ʾ
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

	// �������������˶��ܹ��ɼ�������Ϣ
	public static final String password = "123456";

	private int msg; // ���ñ��������ж��Ǵ��ĸ���ڽ�����������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure);

		Init();

		Intent intent = getIntent();
		msg = intent.getIntExtra("msg", 0);

		switch (msg) {
		// 0����������λ��
		case 0:
			// ��ʼ��
			locationInfo1 = new LocationInfo();
			locationInfo2 = new LocationInfo();
			locationInfo3 = new LocationInfo();
			locationInfo4 = new LocationInfo();

			break;
		// �����1��˵����ͨ�����ѡ������������ģ�����Ҫ�����ݿ��ж�ȡ��Ӧ��������ʾ����
		case 1:
			
			// �����ݿ������ȡ��ص����ݣ��ȴ�intent�л�ȡid��Ϣ
			int id = intent.getIntExtra("id", 0);
			AreaLocationDAO areaLocationDAO = new AreaLocationDAO(
					MeasureActivity.this);
			areaLocationInfo = areaLocationDAO.find(id);
			
			// ��ʼ��ʾ��Ӧ��Ϣ
			zuoshangTv.setText("���ȣ�" + areaLocationInfo.getLongitude1()
					+ "\nγ�ȣ�" + areaLocationInfo.getLatitude1());
			youshangTv.setText("���ȣ�" + areaLocationInfo.getLongitude2()
					+ "\nγ�ȣ�" + areaLocationInfo.getLatitude2());
			zuoxiaTv.setText("���ȣ�" + areaLocationInfo.getLongitude3() + "\nγ�ȣ�"
					+ areaLocationInfo.getLatitude3());
			youxiaTv.setText("���ȣ�" + areaLocationInfo.getLongitude4() + "\nγ�ȣ�"
					+ areaLocationInfo.getLatitude4());
			locationNameEt.setText(areaLocationInfo.getName());
			
			
			// �޸�������Ϣ
			zuoshangMSBtn.setVisibility(View.INVISIBLE);
			youshangMSBtn.setVisibility(View.INVISIBLE);
			zuoxiaMSBtn.setVisibility(View.INVISIBLE);
			youxiaMSBtn.setVisibility(View.INVISIBLE);
			measureOkBtn.setVisibility(View.INVISIBLE);

//			// �޸ı���˵�����Ѿ���ֵ��
//			VALUE_ZUOSHANGTV = true;
//			VALUE_YOUSHANGTV = true;
//			VALUE_ZUOXIATV = true;
//			VALUE_YOUXIATV = true;
//			
//			// ��ʼ��
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
				zuoshangTv.setText("���ȣ�" + locationInfo.getLongitude()
						+ "\nγ�ȣ�" + locationInfo.getLatitude());
				break;
			case KEY_YOUSHANGTV:
				locationInfo2.setLongitude(locationInfo.getLongitude());
				locationInfo2.setLatitude(locationInfo.getLatitude());
				youshangTv.setText("���ȣ�" + locationInfo.getLongitude()
						+ "\nγ�ȣ�" + locationInfo.getLatitude());
				break;
			case KEY_ZUOXIATV:
				locationInfo3.setLongitude(locationInfo.getLongitude());
				locationInfo3.setLatitude(locationInfo.getLatitude());
				zuoxiaTv.setText("���ȣ�" + locationInfo.getLongitude() + "\nγ�ȣ�"
						+ locationInfo.getLatitude());
				break;
			case KEY_YOUXIATV:
				locationInfo4.setLongitude(locationInfo.getLongitude());
				locationInfo4.setLatitude(locationInfo.getLatitude());
				youxiaTv.setText("���ȣ�" + locationInfo.getLongitude() + "\nγ�ȣ�"
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
			// �˳�ʱ�ǵ�ע�����Activity�Ľ�����
			unregisterReceiver(msgReceiver);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��ʼ������
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

			// �ж�֮ǰ�Ƿ��Ѿ���λ��������Ѿ���λ����˵�����������¶�λ������Ҫ�����־λ
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
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				zuoshangMSBtn.setText("���¶�λ");
				// �޸ı���˵����λ�ɹ�
				VALUE_ZUOSHANGTV = true;
				// ����ȷ������
				zuoshangOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youshangOkBtn:

			if (youshangTv.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				youshangMSBtn.setText("���¶�λ");
				// �޸ı���˵����λ�ɹ�
				VALUE_YOUSHANGTV = true;
				youshangOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.zuoxiaOkBtn:

			if (zuoxiaTv.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				zuoxiaMSBtn.setText("���¶�λ");
				// �޸ı���˵����λ�ɹ�
				VALUE_ZUOXIATV = true;
				zuoxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youxiaOkBtn:

			if (youxiaTv.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ",
						Toast.LENGTH_SHORT).show();
			} else {
				selectKey = KEY_NO_SELECT;

				youxiaMSBtn.setText("���¶�λ");
				// �޸ı���˵����λ�ɹ�
				VALUE_YOUXIATV = true;
				youxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.measureOkBtn:

			if (locationNameEt.getText().toString().isEmpty()) {
				Toast.makeText(MeasureActivity.this, "��������Ӧ������",
						Toast.LENGTH_SHORT).show();
			} else if (!isPrepared()) {
				Toast.makeText(MeasureActivity.this, "����ȫ����λ",
						Toast.LENGTH_SHORT).show();
			} else {
				// �ж�֮ǰ�Ƿ����������
				if (getCachePassword() == null) {

					// ��������ܸ�
					final EditText editText = new EditText(MeasureActivity.this);
					editText.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					final AlertDialog ad = new AlertDialog.Builder(
							MeasureActivity.this).create();
					ad.setView(editText);
					ad.setTitle("����");
					ad.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									ad.dismiss();
								}
							});
					ad.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (editText.getText().toString()
											.equals(password)) {
										// ��������
										cachePassword(password);

										modifyData();

										// �˳�ʱ�ǵ�ע�����Activity�Ľ�����
										unregisterReceiver(msgReceiver);

										// Intent intent = new
										// Intent(MeasureActivity.this,MainActivity.class);
										// //����Я�����ݣ�Ϊ������ȷ��ʱ��������Mainactivity��Tab�еĵڶ�������
										// intent.putExtra("msg", 1);
										// startActivity(intent);
										// ��ת�����ҲҪ���������رյ�
										finish();
									} else {
										Toast.makeText(MeasureActivity.this,
												"�������", Toast.LENGTH_SHORT)
												.show();
									}
									ad.dismiss();
								}
							});

					ad.show();

				} else {
					modifyData();

					// �˳�ʱ�ǵ�ע�����Activity�Ľ�����
					unregisterReceiver(msgReceiver);

					// Intent intent = new
					// Intent(MeasureActivity.this,MainActivity.class);
					// //����Я�����ݣ�Ϊ������ȷ��ʱ��������Mainactivity��Tab�еĵڶ�������
					// intent.putExtra("msg", 1);
					// startActivity(intent);
					// ��ת�����ҲҪ���������رյ�
					finish();
				}
			}

			break;
		case R.id.measureCancelBtn:

			// �˳�ʱ�ǵ�ע�����Activity�Ľ�����
			unregisterReceiver(msgReceiver);
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * ���������ûȷ����λ������
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
	 * �ж��Ƿ����з�λ����λ��
	 * 
	 * @return
	 */
	private boolean isPrepared() {
		// ֻҪ��һ��û�ж�λ�ͷ���false
		return VALUE_ZUOSHANGTV && VALUE_YOUSHANGTV && VALUE_ZUOXIATV
				&& VALUE_YOUXIATV;
	}

	/**
	 * ������������
	 */
	private void cachePassword(String password) {
		Editor editor = getSharedPreferences("GPSLocation",
				Context.MODE_PRIVATE).edit();
		editor.putString("password", password);
		editor.apply();
	}

	/**
	 * ������������
	 */
	private String getCachePassword() {
		return getSharedPreferences("GPSLocation", Context.MODE_PRIVATE)
				.getString("password", null);
	}

	private void modifyData() {
		AreaLocationDAO areaLocationDAO = new AreaLocationDAO(
				MeasureActivity.this);
		// ʹ�����µ�����
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
		// �����0��˵�������Ǵ�������
		case 0:
			areaLocationInfo.setid(areaLocationDAO.getMaxId() + 1);
			//�������ϴ���������
			areaLocationInfo.save(MeasureActivity.this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					System.out.println("�ϴ��ɹ�");
					Toast.makeText(MeasureActivity.this, "�ϴ��ɹ�",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("�ϴ�ʧ�ܣ�" + arg1);
					Toast.makeText(MeasureActivity.this, arg1,
							Toast.LENGTH_SHORT).show();
				}
			});
			break;
		// �����1��˵���������޸����ݣ�ȥ�����ù���
		case 1:
			
//			//�����ص����ݿ����
//			areaLocationDAO.update(areaLocationInfo);
//			//�������������ݸ���
//			areaLocationInfo.update(MeasureActivity.this,new UpdateListener() {
//				
//				@Override
//				public void onSuccess() {
//					System.out.println("���³ɹ�");
//					Toast.makeText(MeasureActivity.this, "���³ɹ�",
//							Toast.LENGTH_SHORT).show();
//				}
//				
//				@Override
//				public void onFailure(int arg0, String arg1) {
//					// TODO Auto-generated method stub
//					System.out.println("�ϴ�ʧ�ܣ�" + arg1);
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
