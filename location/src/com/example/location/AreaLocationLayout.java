package com.example.location;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

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
	
	private Button measureBtn,clearAreaBtn;
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
		
		//设置长按效果
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				AlertDialog alertDialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT).create();
				alertDialog.setIcon(R.drawable.ic_launcher);
				alertDialog.setTitle("删除信息？");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						System.out.println("This is delete");
						//将信息提取出来
						String infomation = areaList.get(position);
						//我们只要编号（为了打开修改界面），所以只留下从第0个字符开始到‘|’的这段字符串
						int _id = Integer.parseInt(infomation.substring(0, infomation.indexOf("|")));
						areaLocationDAO.detele(_id);
						//每次删除数据后都要刷新列表
						freshList();
						
						//每次删除数据过后上传数据服务里的数据也要相对应改变，否则会漏掉新建的区域信息
						SendDataServer.last_AreaLocationId = 0;
						
					}});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						System.out.println("This is cancel");
					}});
				//最后不要忘了显示出来对话框
				alertDialog.show();
				//避免触发短点击事件
				shortClick = false;
				
				return false;
			}
		});
		
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

		//设置清除按键点击效果
		clearAreaBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alertDialog = new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_LIGHT).create();
				alertDialog.setTitle("清空所有数据？");
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						List<AreaLocationInfo> infoList = areaLocationDAO.getScrollData(0, (int)areaLocationDAO.getCount());
						//用for循环遍历列表，然后清除所有选项
						for(AreaLocationInfo info : infoList)
						{
							areaLocationDAO.detele(info.getid());
						}
						areaList.clear();
						areaAdapter.notifyDataSetChanged();
					}
				});
				alertDialog.show();
				
			}
		});
	}

	private void init() {

		listView = (ListView) findViewById(R.id.areaLocationLv);
		areaList = new ArrayList<String>();
		areaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, areaList);
		
		listView.setAdapter(areaAdapter);
		
		measureBtn = (Button) findViewById(R.id.measureId);
		clearAreaBtn = (Button) findViewById(R.id.clearAreaId);
		
		areaLocationDAO = new AreaLocationDAO(getContext());
		
	}
	
	/**
	 * 刷新列表
	 */
	private void freshList()
	{
		areaList.clear();
		
		List<AreaLocationInfo> infoList = areaLocationDAO.getScrollData(0, (int)areaLocationDAO.getCount());
		//用for循环遍历列表
		for(AreaLocationInfo info : infoList)
		{
			areaList.add(info.getid() + "|" + info.getName());
			
		}
		
		areaAdapter.notifyDataSetChanged();
	}
	
	
}
