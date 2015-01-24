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
import com.mobile.yunyou.model.BaseType;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.util.Utils;

public class SetGuardTimeActivity extends Activity implements OnClickListener{

	
	public final static String TITLE_KEY = "title_key";
	
	public final static String DATA_KEY = "data_key";
	
    private Context mContext;
	private View mRootView;
	private View mWeekTimeLayoutView;
	private View mStarttimeView;
	private View mEndtimeView;
	
	private TextView mStartTimeTextView;
	private TextView mEndTimeTextView;
	private TextView mWeekTimeTextView;
	
	private Button mBtnBack;
	private Button mBtnOk;
	private TextView mSimTitle;
	private String titleString = "";
	
	private MultiChoicePopWindow mMultiChoicePopWindow;
	private String[] mWeekArrays = null;
	private List<String> mWeekList = new ArrayList<String>(); 
	private boolean mBoolean[] = null;
	
	private CustomTimePopWindow mCustomTimePopWindow;
	
	
	private DeviceSetType.GpsStillTime mGpsStillTime = new DeviceSetType.GpsStillTime();
	
	private BaseType.SimpleTime1 mStartTime = new BaseType.SimpleTime1();
	private BaseType.SimpleTime1 mEndTime = new BaseType.SimpleTime1();
	private BaseType.WeekTime1 mWeekTime = new BaseType.WeekTime1();

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_setguardtime_layout);
		
		Intent intent = getIntent();
		titleString  = intent.getStringExtra(TITLE_KEY);
		
		mGpsStillTime = (DeviceSetType.GpsStillTime) intent.getParcelableExtra(DATA_KEY);
		if (mGpsStillTime == null)
		{
			finish();
			return ;
		}
		
		initView();
		
		initData();
		
	}
	
	

	private void initView()
	{
		mRootView  = findViewById(R.id.rootView);
		mStarttimeView = findViewById(R.id.ll_starttime);
		mStarttimeView.setOnClickListener(this);
		mEndtimeView = findViewById(R.id.ll_endtime);
		mEndtimeView.setOnClickListener(this);
		mWeekTimeLayoutView = findViewById(R.id.weektimeLayout);
		mWeekTimeLayoutView.setOnClickListener(this);
		
		
		mStartTimeTextView = (TextView) findViewById(R.id.tvstartTime);
		mEndTimeTextView = (TextView) findViewById(R.id.tvendTime);
		mWeekTimeTextView = (TextView) findViewById(R.id.tvweekTime);
		
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnOk = (Button) findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);
		
		mSimTitle = (TextView) findViewById(R.id.title);
		if (titleString != null)
		{
			mSimTitle.setText(titleString);
		}
		
		mCustomTimePopWindow = new CustomTimePopWindow(this, mRootView);
		mCustomTimePopWindow.setTitle(getResources().getString(R.string.clock_time_poptitle));
		mCustomTimePopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tag = (Integer) mCustomTimePopWindow.getTag();
				onTimeResult(tag);
			}
		});
	}
	
	private void initData()
	{
		mContext = this;
		
		mWeekArrays = getResources().getStringArray(R.array.dev_week_name);
		for(int i = 0; i < mWeekArrays.length; i++)
		{
			mWeekList.add(mWeekArrays[i]);
		}
		mBoolean = new boolean[mWeekArrays.length];
		
		mMultiChoicePopWindow = new MultiChoicePopWindow(this, mRootView, mWeekList, mBoolean);
		mMultiChoicePopWindow.setTitle(getResources().getString(R.string.clock_text_poptitle));
		mMultiChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onWeekTimeResult();
			}
			
		
		});
		
		refreshTimeData();
	}
	
	
	private void refreshTimeData()
	{

		try {
			mStartTime.parseString(mGpsStillTime.mStartTimeString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			mEndTime.parseString(mGpsStillTime.mEndTimeString);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mWeekTime.parseString(mGpsStillTime.mWeekString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setStartTimeText(mStartTime);
		setEndTimeText(mEndTime);
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
	
	private void setStartTimeText(BaseType.SimpleTime1 clockTime)
	{
		
		mStartTimeTextView.setText(clockTime.toDisplayString());
	}
	
	private void setEndTimeText(BaseType.SimpleTime1 clockTime)
	{
		
		mEndTimeTextView.setText(clockTime.toDisplayString());
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
		case R.id.ll_starttime:
			showStartTimeWindow();
			break;
		case R.id.ll_endtime:
			showEndTimeWindow();
			break;
		case R.id.weektimeLayout:
			showWeekTimeWindow();
			break;
		}
	}
	

	
	private void onOK()
	{

		if (mStartTime.compare(mEndTime) >= 0)
		{
			Utils.showToast(this, R.string.toask_error_time);
			return ;
		}
		
		mGpsStillTime.mStartTimeString = mStartTime.toString();
		mGpsStillTime.mEndTimeString = mEndTime.toString();
		mGpsStillTime.mWeekString = mWeekTime.toString();
		
		Intent intent =new Intent(this, GuardTimeActivity.class);
	    intent.putExtra(DATA_KEY, mGpsStillTime);
	    
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void showStartTimeWindow()
	{
		mCustomTimePopWindow.setTag(0);
		mCustomTimePopWindow.setHour(mStartTime.hour);
		mCustomTimePopWindow.setMinute(mStartTime.minute);
		mCustomTimePopWindow.show(true);
	}
	
	private void showEndTimeWindow()
	{
		mCustomTimePopWindow.setTag(1);
		mCustomTimePopWindow.setHour(mEndTime.hour);
		mCustomTimePopWindow.setMinute(mEndTime.minute);
		mCustomTimePopWindow.show(true);
	}
	
	private void showWeekTimeWindow()
	{
		mMultiChoicePopWindow.show(true);
	}
	
	private void onTimeResult(int tag)
	{
		int hour = mCustomTimePopWindow.getHour();
		int mins = mCustomTimePopWindow.getMinute();
		
		if (tag == 0)
		{
			mStartTime.hour = hour;
			mStartTime.minute = mins;
			setStartTimeText(mStartTime);
		}else if (tag == 1)
		{
			mEndTime.hour = hour;
			mEndTime.minute = mins;
			setEndTimeText(mEndTime);
		}
		
		
	}
	
	private void onWeekTimeResult()
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
