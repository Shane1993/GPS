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
	//用来区别长按和短按
	private boolean shortClick = true;
	
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		System.out.println("This is AreaLocationLayout's onFinishInflate");
		//初始化
		init();
		//刷新列表
		freshList();
		//设置点击效果
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(shortClick)
				{
					//将信息提取出来
					String infomation = areaList.get(position);
					//我们只要编号（为了打开修改界面），所以只留下从第0个字符开始到‘|’的这段字符串
					int _id = Integer.parseInt(infomation.substring(0, infomation.indexOf("|")));
					
					
					Intent intent = new Intent(getContext(), MeasureActivity.class);
					intent.putExtra("msg", 1);
					//配置id便于显示被点击的对象的信息
					intent.putExtra("id", _id);
					getContext().startActivity(intent);
				}
				else
				{
					shortClick = true;
				}
			}
		});
		
//		//防止数据被乱删，所以取消了这个功能
//		//设置长按效果
//		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					final int position, long id) {
//				// TODO Auto-generated method stub
//				AlertDialog alertDialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT).create();
//				alertDialog.setIcon(R.drawable.ic_launcher);
//				alertDialog.setTitle("删除信息？");
//				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
////						System.out.println("This is delete");
//						//将信息提取出来
//						String infomation = areaList.get(position);
//						//我们只要编号（为了打开修改界面），所以只留下从第0个字符开始到‘|’的这段字符串
//						int _id = Integer.parseInt(infomation.substring(0, infomation.indexOf("|")));
//						areaLocationDAO.detele(_id);
//						//每次删除数据后都要刷新列表
//						freshList();
//						
//						//每次删除数据过后上传数据服务里的数据也要相对应改变，否则会漏掉新建的区域信息
//						SendDataServer.last_AreaLocationId = 0;
//						
//					}});
//				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
////						System.out.println("This is cancel");
//					}});
//				//最后不要忘了显示出来对话框
//				alertDialog.show();
//				//避免触发短点击事件
//				shortClick = false;
//				
//				return false;
//			}
//		});
		
		//设置测试按键点击效果
		measureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),MeasureActivity.class);
				intent.putExtra("msg", 0);
				getContext().startActivity(intent);
				
			}
		});

		//设置刷新按键点击效果
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
	 * 刷新列表
	 */
	private void freshList()
	{
		onRequestCloudCode();
	}
	
	/**
	 * 从云端获取测量的区域数据
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
		
		// 创建云端代码对象
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        // 异步调用云端代码
        cloudCode.callEndpoint(getContext(), cloudCodeName, params,
                new CloudCodeListener() {

                    @Override
                    public void onSuccess(Object result) {
                        // TODO Auto-generated method stub
                    	
                        System.out.println("NetConnection's onSuccess");
                        //清除所有区域位置信息重新获取
                        List<AreaLocationInfo> list = areaLocationDAO.getScrollData(0, (int)areaLocationDAO.getCount());
						//用for循环遍历列表，然后清除所有选项
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
								//将数据添加进数据库
								areaLocationDAO.add(areaLocationInfo);
								
							}
							
							
							//将区域信息列在屏幕上
							List<AreaLocationInfo> infoList = areaLocationDAO.getScrollData(0, (int)areaLocationDAO.getCount());
							//用for循环遍历列表
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

                    	Toast.makeText(getContext(), "获取数据失败 : " + s, Toast.LENGTH_SHORT).show();
                    }

                });
	}
	
	
}
