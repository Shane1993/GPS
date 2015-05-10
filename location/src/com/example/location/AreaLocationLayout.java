package com.example.location;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;

import com.example.location.database.AreaLocationDAO;
import com.example.location.model.AreaLocationInfo;
import com.example.location.service.SendDataServer;

public class AreaLocationLayout extends LinearLayout {

	public AreaLocationLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AreaLocationLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private ListView listView;
	private ArrayList<String> areaList;
	private ArrayAdapter<String> areaAdapter;
	
	private Button measureBtn,refreshAreaBtn;
	private AreaLocationDAO areaLocationDAO;
	//�������𳤰��Ͷ̰�
	private boolean shortClick = true;
	
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		System.out.println("This is AreaLocationLayout's onFinishInflate");
		//��ʼ��
		init();
		//ˢ���б�
		freshList();
		//���õ��Ч��
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(shortClick)
				{
					//����Ϣ��ȡ����
					String infomation = areaList.get(position);
					//����ֻҪ��ţ�Ϊ�˴��޸Ľ��棩������ֻ���´ӵ�0���ַ���ʼ����|��������ַ���
					int _id = Integer.parseInt(infomation.substring(0, infomation.indexOf("|")));
					
					
					Intent intent = new Intent(getContext(), MeasureActivity.class);
					intent.putExtra("msg", 1);
					//����id������ʾ������Ķ������Ϣ
					intent.putExtra("id", _id);
					getContext().startActivity(intent);
				}
				else
				{
					shortClick = true;
				}
			}
		});
		
//		//��ֹ���ݱ���ɾ������ȡ�����������
//		//���ó���Ч��
//		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					final int position, long id) {
//				// TODO Auto-generated method stub
//				AlertDialog alertDialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT).create();
//				alertDialog.setIcon(R.drawable.ic_launcher);
//				alertDialog.setTitle("ɾ����Ϣ��");
//				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "�ǵ�", new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
////						System.out.println("This is delete");
//						//����Ϣ��ȡ����
//						String infomation = areaList.get(position);
//						//����ֻҪ��ţ�Ϊ�˴��޸Ľ��棩������ֻ���´ӵ�0���ַ���ʼ����|��������ַ���
//						int _id = Integer.parseInt(infomation.substring(0, infomation.indexOf("|")));
//						areaLocationDAO.detele(_id);
//						//ÿ��ɾ�����ݺ�Ҫˢ���б�
//						freshList();
//						
//						//ÿ��ɾ�����ݹ����ϴ����ݷ����������ҲҪ���Ӧ�ı䣬�����©���½���������Ϣ
//						SendDataServer.last_AreaLocationId = 0;
//						
//					}});
//				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��", new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
////						System.out.println("This is cancel");
//					}});
//				//���Ҫ������ʾ�����Ի���
//				alertDialog.show();
//				//���ⴥ���̵���¼�
//				shortClick = false;
//				
//				return false;
//			}
//		});
		
		//���ò��԰������Ч��
		measureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),MeasureActivity.class);
				intent.putExtra("msg", 0);
				getContext().startActivity(intent);
				
			}
		});

		//����ˢ�°������Ч��
		refreshAreaBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				freshList();

			}
		});
	}

	private void init() {

		listView = (ListView) findViewById(R.id.areaLocationLv);
		areaList = new ArrayList<String>();
		areaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, areaList);
		
		listView.setAdapter(areaAdapter);
		
		measureBtn = (Button) findViewById(R.id.measureId);
		refreshAreaBtn = (Button) findViewById(R.id.refreshAreaId);
		
		areaLocationDAO = new AreaLocationDAO(getContext());
		
	}
	
	/**
	 * ˢ���б�
	 */
	private void freshList()
	{
		onRequestCloudCode();
	}
	
	/**
	 * ���ƶ˻�ȡ��������������
	 */
	private void onRequestCloudCode()
	{
		String cloudCodeName = "test";
		
		JSONObject params = new JSONObject();
		
		try
		{
			params.put("name", "getAllAreaName");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// �����ƶ˴������
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        // �첽�����ƶ˴���
        cloudCode.callEndpoint(getContext(), cloudCodeName, params,
                new CloudCodeListener() {

                    @Override
                    public void onSuccess(Object result) {
                        // TODO Auto-generated method stub
                    	
                        System.out.println("NetConnection's onSuccess");
                        //�����������λ����Ϣ���»�ȡ
                        List<AreaLocationInfo> list = areaLocationDAO.getScrollData(0, (int)areaLocationDAO.getCount());
						//��forѭ�������б�Ȼ���������ѡ��
						for(AreaLocationInfo info : list)
						{
							areaLocationDAO.detele(info.getid());
						}
						areaList.clear();
						areaAdapter.notifyDataSetChanged();
						
                        try {
							JSONObject jsonOject = new JSONObject(result.toString());
							JSONArray areaArray = jsonOject.getJSONArray("results");
							
							for(int i=0;i<areaArray.length();i++)
							{
								JSONObject areaJson = areaArray.getJSONObject(i);
								AreaLocationInfo areaLocationInfo = new AreaLocationInfo();
								areaLocationInfo.setName(areaJson.getString("name"));
								areaLocationInfo.setLatitude1(areaJson.getDouble("latitude1"));
								areaLocationInfo.setLatitude2(areaJson.getDouble("latitude2"));
								areaLocationInfo.setLatitude3(areaJson.getDouble("latitude3"));
								areaLocationInfo.setLatitude4(areaJson.getDouble("latitude4"));
								
								areaLocationInfo.setLongitude1(areaJson.getDouble("longitude1"));
								areaLocationInfo.setLongitude2(areaJson.getDouble("longitude2"));
								areaLocationInfo.setLongitude3(areaJson.getDouble("longitude3"));
								areaLocationInfo.setLongitude4(areaJson.getDouble("longitude4"));
								
								areaLocationInfo.setid(areaJson.getInt("id"));
								//��������ӽ����ݿ�
								areaLocationDAO.add(areaLocationInfo);
								
							}
							
							
							//��������Ϣ������Ļ��
							List<AreaLocationInfo> infoList = areaLocationDAO.getScrollData(0, (int)areaLocationDAO.getCount());
							//��forѭ�������б�
							for(AreaLocationInfo info : infoList)
							{
								areaList.add(info.getid() + "|" + info.getName());
							}
							
							areaAdapter.notifyDataSetChanged();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    	Toast.makeText(getContext(), "��ȡ����ʧ�� : " + s, Toast.LENGTH_SHORT).show();
                    }

                });
	}
	
	
}
