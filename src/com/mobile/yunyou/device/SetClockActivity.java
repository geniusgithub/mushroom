package com.mobile.yunyou.device;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.custom.CustomTimePopWindow;
import com.mobile.yunyou.custom.MultiChoicePopWindow;
import com.mobile.yunyou.custom.SingleEditPopWindow;
import com.mobile.yunyou.model.BaseType;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.DeviceSetType.ClockSet;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class SetClockActivity extends Activity implements OnClickListener{

	public final static String DATA_KEY = "data_key";

	
	private static final CommonLog log = LogFactory.createLog();
	
	private Context mContext;
	private Button mBtnBack;
	private Button mBtnSure;
	private View mRootView;

	private View mSetClockNameView;
	private View mSetClockTimeView;
	private View mSetWeekTimeView;
	private TextView mClockNameTextView;
	private TextView mClockTimeTextView;
	private TextView mWeekTimeTextView;
	
	private MultiChoicePopWindow mMultiChoicePopWindow;
	private String[] mWeekStringArrays = null;
	private List<String> mWeekStringList = new ArrayList<String>(); 
	private boolean mBoolean[] = null;
	
	private CustomTimePopWindow mCustomTimePopWindow;
	private SingleEditPopWindow mSingleEditPopWindow;
	
	private DeviceSetType.ClockSet mClockSet = new DeviceSetType.ClockSet();
	
	private BaseType.SimpleTime1 mClockTime = new BaseType.SimpleTime1();
	private BaseType.WeekTime1 mWeekTime = new BaseType.WeekTime1();

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_setclock_layout);
		
		Intent intent = getIntent();
		mClockSet = (ClockSet) intent.getParcelableExtra(DATA_KEY);
		if (mClockSet == null)
		{
			finish();
			return ;
		}
		
		initView();
		
		initData();
		
		
	}

	private void initView()
	{
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mBtnSure = (Button) findViewById(R.id.btn_ok);
		mBtnSure.setOnClickListener(this);
		
		mRootView = findViewById(R.id.rootView);
		
		mSetClockNameView = findViewById(R.id.ll_setClockName);
		mSetClockTimeView = findViewById(R.id.ll_setClockTime);
		mSetWeekTimeView =  findViewById(R.id.ll_setWeekTime);
		
		mSetClockNameView.setOnClickListener(this);
		mSetClockTimeView.setOnClickListener(this);
		mSetWeekTimeView.setOnClickListener(this);
		
		mClockNameTextView = (TextView) findViewById(R.id.tvClockName);
		mClockTimeTextView = (TextView) findViewById(R.id.tvClockTime);
		mWeekTimeTextView = (TextView) findViewById(R.id.tvWeekTime);
		
	}
	
	
	private void initData()
	{
		mContext = this;
		
		mWeekStringArrays = getResources().getStringArray(R.array.dev_week_name);
		for(int i = 0; i < mWeekStringArrays.length; i++)
		{
			mWeekStringList.add(mWeekStringArrays[i]);
		}
		mBoolean = new boolean[mWeekStringArrays.length];
		
		mMultiChoicePopWindow = new MultiChoicePopWindow(this, mRootView, mWeekStringList, mBoolean);
		mMultiChoicePopWindow.setTitle(getResources().getString(R.string.clock_text_poptitle));
		mMultiChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSetWeekTime();
			}
			
		
		});
		
		mCustomTimePopWindow = new CustomTimePopWindow(this, mRootView);
		mCustomTimePopWindow.setTitle(getResources().getString(R.string.clock_time_poptitle));
		mCustomTimePopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSetClockTime();
			}
		});
		
		mSingleEditPopWindow = new SingleEditPopWindow(this, mRootView);
		mSingleEditPopWindow.setTitle(R.string.clock_name_poptitle);
		mSingleEditPopWindow.setTextString(R.string.clock_name_poptitle);
		mSingleEditPopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onSetClockName();
			}
		});
		
		refreshClockData();
	}
	
	private void refreshClockData()
	{

		try {
			mClockTime.parseString(mClockSet.mTimeString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mWeekTime.parseString(mClockSet.mWeekString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setClockNameText(mClockSet.mName);
		setClockTimeText(mClockTime);
		setWeekTimeText(mWeekTime);
		
		
		List<Integer> list = mWeekTime.weekList; 
		int size = list.size();
		for(int i = 0; i < size; i++)
		{
			try {
				mBoolean[list.get(i) - 1] = true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		}
		
		
	}
	
	private void setClockNameText(String name)
	{
		mClockNameTextView.setText(name);
	}
	
	private void setClockTimeText(BaseType.SimpleTime1 clockTime)
	{
		
		mClockTimeTextView.setText(mClockTime.toDisplayString());
	}
	
	private void setWeekTimeText(BaseType.WeekTime1 weekTime)
	{
		mWeekTimeTextView.setText(weekTime.toDisplayString());
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_ok:
			onOK();
			break;
		case R.id.ll_setClockName:
			showClockNameWindow();
			break;
		case R.id.ll_setClockTime:
			showClockTimeWindow();
			break;
		case R.id.ll_setWeekTime:
			showWeekTimeWindow();
			break;
		}
	}
	
	private void onOK()
	{
		String name = mClockNameTextView.getText().toString();
		if (name.equals(""))
		{		
			Utils.showToast(this, R.string.toask_clockname_not_null);
			return;
		}
		mClockSet.mName = name;
		mClockSet.mTimeString = mClockTime.toString();
		mClockSet.mWeekString = mWeekTime.toString();
		
		Intent intent =new Intent(this,AlarmClockActivity.class);
	    intent.putExtra(DATA_KEY, mClockSet);
	    
		setResult(RESULT_OK, intent);
		finish();
	}
	
	
	private void showClockNameWindow()
	{
		mSingleEditPopWindow.setEditString(mClockNameTextView.getText().toString());
		mSingleEditPopWindow.show(true);
	}
	
	private void showClockTimeWindow()
	{
		mCustomTimePopWindow.setHour(mClockTime.hour);
		mCustomTimePopWindow.setMinute(mClockTime.minute);
		mCustomTimePopWindow.show(true);
	}
	
	private void showWeekTimeWindow()
	{
		mMultiChoicePopWindow.setSelectItem(mBoolean);
		mMultiChoicePopWindow.show(true);
	}
	
	
	private void onSetClockName()
	{
		String name = mSingleEditPopWindow.getEditString();
		if (name.equals(""))
		{
			Utils.showToast(this, R.string.toask_clockname_not_null);
			return ;
		}
		
		mClockNameTextView.setText(name);
	}
	
	private void onSetClockTime()
	{
		int hour = mCustomTimePopWindow.getHour();
		int mins = mCustomTimePopWindow.getMinute();
		
		mClockTime.hour = hour;
		mClockTime.minute = mins;
		
		setClockTimeText(mClockTime);
		
	}
	
	private void onSetWeekTime()
	{
		boolean flag[] = mMultiChoicePopWindow.getSelectItem();

		List<Integer> newList  = new ArrayList<Integer>();
		for(int i = 0; i < flag.length; i++)
		{
			if (flag[i])
			{
				newList.add(i + 1);
			}
			
		}
		
		mWeekTime.weekList = newList;
		
		setWeekTimeText(mWeekTime);
	}
}
