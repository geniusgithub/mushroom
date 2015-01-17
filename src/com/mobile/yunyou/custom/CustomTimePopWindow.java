package com.mobile.yunyou.custom;

import java.util.Calendar;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.widget.NumericWheelAdapter;
import com.mobile.yunyou.widget.WheelView;

public class CustomTimePopWindow implements OnClickListener{

	protected Context mContext;
	protected View mParentView;


	protected TextView mTVTitle;
	protected Button mButtonOK;
	protected Button mButtonCancel;
	protected  WheelView hoursWheelView;
	protected  WheelView minsWheelView ;
	
	
	protected PopupWindow mPopupWindow;                                                                                                                                                  

	protected Object tag;
	
	private OnClickListener mOkListener;

	public CustomTimePopWindow(Context context,View parentView)
	{
		mContext = context;
		mParentView = parentView;
	
		initView(mContext);
	}
	
	protected void initView(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.custom_time_dialog_layout, null);
	
		mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
		mButtonOK = (Button) view.findViewById(R.id.btnOK);
		mButtonOK.setOnClickListener(this);
		mButtonCancel = (Button) view.findViewById(R.id.btnCancel);
		mButtonCancel.setOnClickListener(this);
		
		hoursWheelView =  (WheelView) view.findViewById(R.id.hour);
		minsWheelView =  (WheelView) view.findViewById(R.id.mins);
		
		
		mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	mPopupWindow.setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		mPopupWindow.setBackgroundDrawable(dw);
		 	
		initWheelView(view);
	}
	
	
	private void initWheelView(View view)
	{

		hoursWheelView = (WheelView) view.findViewById(R.id.hour);
		hoursWheelView.setAdapter(new NumericWheelAdapter(0, 23));
		hoursWheelView.setCyclic(true);
		hoursWheelView.TEXT_SIZE = 30;
		
		minsWheelView = (WheelView) view.findViewById(R.id.mins);
		minsWheelView.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		minsWheelView.setCyclic(true);
		minsWheelView.TEXT_SIZE = 30;
		
		
		// set current time
		Calendar c = Calendar.getInstance();
		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curMinutes = c.get(Calendar.MINUTE);
	
		hoursWheelView.setCurrentItem(curHours);
		minsWheelView.setCurrentItem(curMinutes);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnOK:
			onButtonOK(v);
			break;
		case R.id.btnCancel:
			onButtonCancel(v);
			break;
		}
	}
		
	public void setTag(Object object)
	{
		tag = object;
	}
	
	public Object getTag()
	{
		return tag;
	}
	
	public void setHour(int hour)
	{
		hoursWheelView.setCurrentItem(hour);
	}
	
	public void setMinute(int minute)
	{
		minsWheelView.setCurrentItem(minute);
	}
	
	public int getHour()
	{
		return hoursWheelView.getCurrentItem();
	}
	
	public int getMinute()
	{
		return minsWheelView.getCurrentItem();
	}
	
	public void setOnOKButtonListener(View.OnClickListener onClickListener) {
		mOkListener = onClickListener;
	}
	
	public void setTitle(String title)
	{
		mTVTitle.setText(title);
	}
	


	public void show(boolean bShow)
	{

		if (bShow)
		{		
			mPopupWindow.showAtLocation(mParentView, Gravity.TOP, 0, 0);
		}else{
			mPopupWindow.dismiss();
		}
	}

	protected void onButtonOK(View v)
	{
		show(false);
		
		if (mOkListener != null)
		{
			mOkListener.onClick(v);
		}
	}
	
	protected void onButtonCancel(View v)
	{
		show(false);

	}
	
	
}
