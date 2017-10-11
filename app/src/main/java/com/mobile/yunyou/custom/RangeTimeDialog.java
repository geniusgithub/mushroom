package com.mobile.yunyou.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.BaseType;
import com.mobile.yunyou.util.Utils;

public class RangeTimeDialog extends Dialog implements android.view.View.OnClickListener{

	protected Context mContext;
	protected Button mBtnOK;
	protected Button mBtnCancel;
	protected View.OnClickListener mOkListener;
	
	
	
	protected EditText mYearEditText;
	protected EditText mMonthEditText;
	protected EditText mDayEditText;
	protected EditText mStartHourEditText;
	protected EditText mStartMinuteEditText;
	protected EditText mEndHourEditText;
	protected EditText mEndMinuteEditText;
	
	
	
	
	public RangeTimeDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		initView(mContext);	
	}
	
	
	protected void initView(Context context)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dev_history_dialog_layout);
	
	    Window dialogWindow = getWindow();       
	    WindowManager.LayoutParams lp = dialogWindow.getAttributes();         
		ColorDrawable dw = new ColorDrawable(0x00000000);
	    dialogWindow.setBackgroundDrawable(dw);
	
	    WindowManager m = dialogWindow.getWindowManager();
	    Display d = m.getDefaultDisplay();
	    lp.width = Utils.getScreenWidth(mContext) - 10;
//	    lp.height = Utils.getScreenHeight(context) - 200;
  
	    dialogWindow.setAttributes(lp);
	    
	    mBtnOK = (Button) findViewById(R.id.btnOK);
	    mBtnCancel = (Button) findViewById(R.id.btnCancel);
	    mBtnOK.setOnClickListener(this);
	    mBtnCancel.setOnClickListener(this);
	    
	    
	    
	    mYearEditText = (EditText) findViewById(R.id.etEditYear);
	    mMonthEditText = (EditText) findViewById(R.id.etEditMonth);
	    mDayEditText = (EditText) findViewById(R.id.etEditDay);
	    mStartHourEditText = (EditText) findViewById(R.id.etEditStartHour);
	    mStartMinuteEditText = (EditText) findViewById(R.id.etEditStartMinute); 
	    mEndHourEditText = (EditText) findViewById(R.id.etEditEndHour);
	    mEndMinuteEditText = (EditText) findViewById(R.id.etEditEndMinute);
	    
	}

	public void clear()
	{
		mYearEditText.setText("");
		mMonthEditText.setText("");
		mDayEditText.setText("");
		mStartHourEditText.setText("");
		mStartMinuteEditText.setText("");
		mEndHourEditText.setText("");
		mEndMinuteEditText.setText("");
		
		
//		mYearEditText.setText("2012");
//		mMonthEditText.setText("12");
//		mDayEditText.setText("6");
//		mStartHourEditText.setText("1");
//		mStartMinuteEditText.setText("40");
//		mEndHourEditText.setText("23");
//		mEndMinuteEditText.setText("0");

	}
	
	public BaseType.RangeTime getRangeTime()
	{
		BaseType.RangeTime rangeTime = new BaseType.RangeTime();
		
		String year = mYearEditText.getText().toString();
		String month = mMonthEditText.getText().toString();
		String day = mDayEditText.getText().toString();
		String startHour = mStartHourEditText.getText().toString();
		String startMinute = mStartMinuteEditText.getText().toString();
		String endHour = mEndHourEditText.getText().toString();
		String endMinute = mEndMinuteEditText.getText().toString();
	
		try {
			rangeTime.year = Integer.parseInt(year);
			rangeTime.month = Integer.parseInt(month);
			rangeTime.day = Integer.parseInt(day);
			rangeTime.startHour = Integer.parseInt(startHour);
			rangeTime.startMinute = Integer.parseInt(startMinute);
			rangeTime.endHour = Integer.parseInt(endHour);
			rangeTime.endMinute = Integer.parseInt(endMinute);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		return rangeTime;
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
		
	
	public void setOnOKButtonListener(View.OnClickListener onClickListener) {
		mOkListener = onClickListener;
	}	

	protected void onButtonOK(View v)
	{
		if (mOkListener != null)
		{
			mOkListener.onClick(v);
		}
	}
	
	protected void onButtonCancel(View v)
	{
		dismiss();
	}
}
