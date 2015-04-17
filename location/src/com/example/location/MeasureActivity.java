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
	
	//��������GPS������������
	private LocationInfo locationInfo;
	//������¼ÿ������������
	private LocationInfo locationInfo1,locationInfo2,locationInfo3,locationInfo4;
	private AreaLocationInfo areaLocationInfo = new AreaLocationInfo();
	
	//���ó������������ж��ǵ�ǰ�ĸ�������Ҫ��λ��ʾ
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
	
	private int msg;	//���ñ��������ж��Ǵ��ĸ���ڽ�����������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure);
		
		Init();
		
		Intent intent = getIntent();
		msg = intent.getIntExtra("msg", 0);
		
		switch (msg) {
		//0����������λ��
		case 0:
			//��ʼ��
			locationInfo1 = new LocationInfo();
			locationInfo2 = new LocationInfo();
			locationInfo3 = new LocationInfo();
			locationInfo4 = new LocationInfo();
			
			break;
		//�����1��˵����ͨ�����ѡ������������ģ�����Ҫ�����ݿ��ж�ȡ��Ӧ��������ʾ����
		case 1:
			//�޸�������Ϣ
			zuoshangMSBtn.setText("���¶�λ");
			youshangMSBtn.setText("���¶�λ");
			zuoxiaMSBtn.setText("���¶�λ");
			youxiaMSBtn.setText("���¶�λ");
			measureOkBtn.setText("�޸�");
			
			//�޸ı���˵�����Ѿ���ֵ��
			VALUE_ZUOSHANGTV = true;
			VALUE_YOUSHANGTV = true;
			VALUE_ZUOXIATV = true;
			VALUE_YOUXIATV = true;
			
			//�����ݿ������ȡ��ص����ݣ��ȴ�intent�л�ȡid��Ϣ
			int id = intent.getIntExtra("id", 0);
			AreaLocationDAO areaLocationDAO = new AreaLocationDAO(MeasureActivity.this);
			areaLocationInfo = areaLocationDAO.find(id);
			//��ʼ��ʾ��Ӧ��Ϣ
			zuoshangTv.setText("���ȣ�" + areaLocationInfo.getLongitude1() + "\nγ�ȣ�" + areaLocationInfo.getLatitude1());
			youshangTv.setText("���ȣ�" + areaLocationInfo.getLongitude2() + "\nγ�ȣ�" + areaLocationInfo.getLatitude2());			
			zuoxiaTv.setText("���ȣ�" + areaLocationInfo.getLongitude3() + "\nγ�ȣ�" + areaLocationInfo.getLatitude3());
			youxiaTv.setText("���ȣ�" + areaLocationInfo.getLongitude4() + "\nγ�ȣ�" + areaLocationInfo.getLatitude4());
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
				locationInfo1.setLongitude(locationInfo.getLongitude());
				locationInfo1.setLatitude(locationInfo.getLatitude());
				zuoshangTv.setText("���ȣ�" + locationInfo.getLongitude() + "\nγ�ȣ�" + locationInfo.getLatitude());
				break;
			case KEY_YOUSHANGTV:
				locationInfo2.setLongitude(locationInfo.getLongitude());
				locationInfo2.setLatitude(locationInfo.getLatitude());
				youshangTv.setText("���ȣ�" + locationInfo.getLongitude() + "\nγ�ȣ�" + locationInfo.getLatitude());			
				break;
			case KEY_ZUOXIATV:
				locationInfo3.setLongitude(locationInfo.getLongitude());
				locationInfo3.setLatitude(locationInfo.getLatitude());
				zuoxiaTv.setText("���ȣ�" + locationInfo.getLongitude() + "\nγ�ȣ�" + locationInfo.getLatitude());
				break;
			case KEY_YOUXIATV:
				locationInfo4.setLongitude(locationInfo.getLongitude());
				locationInfo4.setLatitude(locationInfo.getLatitude());
				youxiaTv.setText("���ȣ�" + locationInfo.getLongitude() + "\nγ�ȣ�" + locationInfo.getLatitude());
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
			//�˳�ʱ�ǵ�ע�����Activity�Ľ�����
			unregisterReceiver(msgReceiver);
			finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}

	

	/**
	 * ��ʼ������
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
			
			//�ж�֮ǰ�Ƿ��Ѿ���λ��������Ѿ���λ����˵�����������¶�λ������Ҫ�����־λ
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
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				zuoshangMSBtn.setText("���¶�λ");
				//�޸ı���˵����λ�ɹ�
				VALUE_ZUOSHANGTV = true;
				//����ȷ������
				zuoshangOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youshangOkBtn:
			
			if(youshangTv.getText().toString().isEmpty())
			{
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				youshangMSBtn.setText("���¶�λ");
				//�޸ı���˵����λ�ɹ�
				VALUE_YOUSHANGTV = true;
				youshangOkBtn.setVisibility(View.INVISIBLE);
			}	
			break;
		case R.id.zuoxiaOkBtn:
			
			if(zuoxiaTv.getText().toString().isEmpty())
			{
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				zuoxiaMSBtn.setText("���¶�λ");
				//�޸ı���˵����λ�ɹ�
				VALUE_ZUOXIATV = true;
				zuoxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.youxiaOkBtn:
			
			if(youxiaTv.getText().toString().isEmpty())
			{
				Toast.makeText(MeasureActivity.this, "���Ƚ��ж�λ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				selectKey = KEY_NO_SELECT;
				
				youxiaMSBtn.setText("���¶�λ");
				//�޸ı���˵����λ�ɹ�
				VALUE_YOUXIATV = true;
				youxiaOkBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.measureOkBtn:
			
			if(locationNameEt.getText().toString().isEmpty() )
			{
				Toast.makeText(MeasureActivity.this, "��������Ӧ������", Toast.LENGTH_SHORT).show();
			}
			else if( !isPrepared() )
			{
				Toast.makeText(MeasureActivity.this, "����ȫ����λ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				AreaLocationDAO areaLocationDAO = new AreaLocationDAO(MeasureActivity.this);
				//ʹ�����µ�����
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
				//�����0��˵�������Ǵ�������
				case 0:
					areaLocationInfo.setid(areaLocationDAO.getMaxId() + 1);
					//��������ӽ����ݿ�
					areaLocationDAO.add(areaLocationInfo);
					Toast.makeText(MeasureActivity.this, "������ӳɹ�", Toast.LENGTH_SHORT).show();
					break;
				//�����1��˵���������޸�����
				case 1:
					//�������ݿ��е�����
					areaLocationDAO.update(areaLocationInfo);
					Toast.makeText(MeasureActivity.this, "�����޸ĳɹ�", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				
				//�˳�ʱ�ǵ�ע�����Activity�Ľ�����
				unregisterReceiver(msgReceiver);
				
//				Intent intent = new Intent(MeasureActivity.this,MainActivity.class);
//				//����Я�����ݣ�Ϊ������ȷ��ʱ��������Mainactivity��Tab�еĵڶ�������
//				intent.putExtra("msg", 1);
//				startActivity(intent);
				//��ת�����ҲҪ���������رյ�
				finish();
				
			}
			
			break;
		case R.id.measureCancelBtn:
			
			//�˳�ʱ�ǵ�ע�����Activity�Ľ�����
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
	 * �ж��Ƿ����з�λ����λ��
	 * @return
	 */
	private boolean isPrepared()
	{
		//ֻҪ��һ��û�ж�λ�ͷ���false
		return VALUE_ZUOSHANGTV && VALUE_YOUSHANGTV && VALUE_ZUOXIATV && VALUE_YOUXIATV;
	}
	
}
