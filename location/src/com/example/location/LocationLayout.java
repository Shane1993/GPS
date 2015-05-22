package com.example.location;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.location.database.LocationDAO;
import com.example.location.model.LocationInfo;
import com.example.location.service.SendDataServer;

public class LocationLayout extends LinearLayout {

	public LocationLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LocationLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private ListView listView;
	private ArrayList<String> locationList;
	private ArrayAdapter<String> locationAdapter;
	
	private Button freshBtn,clearLocationBtn;
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		init();
		
		freshList();
		
		
		clearLocationBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alertDialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT).create();
				alertDialog.setTitle("����������ݣ�");
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "�ǵ�", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						LocationDAO locationDAO = new LocationDAO(getContext());
						List<LocationInfo> list = locationDAO.getScrollData(0, (int)locationDAO.getCount());
						for(LocationInfo info : list)
						{
							locationDAO.detele(info.getid());
						}
						
						/***************************************
						//�������֮����и�bug����
						 ***************************************/
						SendDataServer.last_LocationId = 0;
						
						locationList.clear();
						locationAdapter.notifyDataSetChanged();
					}
				});
				alertDialog.show();
			}
		});
		
		freshBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ˢ���б�
				freshList();
			}
		});
	}

	
	private void init() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.locationLayoutLv);
		locationList = new ArrayList<String>();
		locationAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, locationList);
		listView.setAdapter(locationAdapter);
		
		freshBtn = (Button) findViewById(R.id.freshLocationBtnId);
		clearLocationBtn = (Button) findViewById(R.id.clearLocationBtnId);
		
	}
	
	private void freshList()
	{
		
		locationList.clear();
		
		LocationDAO locationDAO = new LocationDAO(getContext());
		List<LocationInfo> list = locationDAO.getScrollData(0, (int)locationDAO.getCount());
		for(LocationInfo info : list)
		{
			locationList.add(info.getid() + "\n" + info.toString());
		}
		locationAdapter.notifyDataSetChanged();
		
	}
	
}
