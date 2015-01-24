package com.mobile.yunyou.map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.mobile.yunyou.R;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class MapMenuPopWindow extends PopupWindow{

	
	private static final CommonLog log = LogFactory.createLog();
	
	private Context mContext;
	
	private Button 			mGoDeviceBtn;
	private Button 			mMonitorBtn;
	private Button 			mCalDistanceBtn;
	private Button 			mShutDownBtn;
	private Button 			mHistoryBtn;
	private Button 			mPenBtn;
	
	public MapMenuPopWindow(Context context)
	{
		super(context);
		
		mContext = context;
		
		init();
	}
	

	public void setClickiListener(View.OnClickListener listener)
	{
		mGoDeviceBtn.setOnClickListener(listener);
		mMonitorBtn.setOnClickListener(listener);
		mCalDistanceBtn.setOnClickListener(listener);
		mShutDownBtn.setOnClickListener(listener);
		mHistoryBtn.setOnClickListener(listener);
		mPenBtn.setOnClickListener(listener);
	}
	
	private void init()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.map_menu_layout, null);
		setContentView(view);		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		
		setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
	

		mGoDeviceBtn = (Button) view.findViewById(R.id.btn_menu_deviceset);
		mMonitorBtn = (Button) view.findViewById(R.id.btn_menu_listener);
		mCalDistanceBtn = (Button) view.findViewById(R.id.btn_menu_distance);
		mShutDownBtn = (Button) view.findViewById(R.id.btn_menu_shutdown);
		mHistoryBtn = (Button) view.findViewById(R.id.btn_menu_history);
		mPenBtn = (Button) view.findViewById(R.id.btn_menu_pen);
	}
	
	
	public void showWindow(View parent)
	{
		showAtLocation(parent, Gravity.BOTTOM | Gravity.LEFT, 100, 100);
	}
	
	
}
