package com.mobile.yunyou;

import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.custom.DeviceAreaDialog;
import com.mobile.yunyou.custom.RangeTimeDialog;
import com.mobile.yunyou.model.DeviceSetType;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TestDeviceAreaActivity extends Activity implements OnItemClickListener, OnClickListener{

//	private ListView mListView;
//	private TestDeviceAreaAdapter mAdapter;
	private List<DeviceSetType.DeviceAreaResult> mdata;
	
	private DeviceAreaDialog mDeviceAreaDialog;
	
	private RangeTimeDialog mRangeTimeDialog;
	
	private Button mButton1;
	private Button mButton2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.test_dialog_activity);
		
		initView();
	}

	private final int COUNT = 20;
	private void initView()
	{
//		mListView = (ListView) findViewById(R.id.listView);
//		mListView.setOnItemClickListener(this);
//		
		mButton1 = (Button) findViewById(R.id.button1);
		mButton1.setOnClickListener(this);
		

		mButton2 = (Button) findViewById(R.id.button2);
		mButton2.setOnClickListener(this);
		
		
//		mdata = new ArrayList<DeviceSetType.DeviceAreaResult>();
//		for(int i = 0; i < COUNT; i++)
//		{
//			DeviceSetType.DeviceAreaResult object = new DeviceSetType.DeviceAreaResult();
//			object.mName = "genius->" + i;
//			mdata.add(object);
//		}
//		
//		mAdapter = new TestDeviceAreaAdapter(this, mdata);
//		mListView.setAdapter(mAdapter);
//		mListView.setVisibility(View.GONE);
		
//		mDeviceAreaDialog = new DeviceAreaDialog(this, mdata);
		
		mRangeTimeDialog = new RangeTimeDialog(this);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "pos = " + pos, Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		case R.id.button1:
			mDeviceAreaDialog.show();
			break;
		case R.id.button2:
			mRangeTimeDialog.show();
			break;
		}
		
	}
	
}
