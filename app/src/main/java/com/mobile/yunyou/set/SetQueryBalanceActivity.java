package com.mobile.yunyou.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.util.Utils;

public class SetQueryBalanceActivity extends Activity implements OnClickListener{

	private Button mBtnBack;
	
	private TextView mTvAccount;
	private TextView mTvBalance;
	private TextView mTvVipDate;
	private TextView mTvVipState;
	
	private Button mBtnRecharNow;
	private Button mBtnRecharRecord;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_query_balance_layout);

        initView();
    }
	
	private void initView()
	{
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mTvAccount = (TextView) findViewById(R.id.tv_account);
		mTvBalance = (TextView) findViewById(R.id.tv_balance);
		mTvVipDate = (TextView) findViewById(R.id.tv_vip_date);
		mTvVipState = (TextView) findViewById(R.id.tv_vip_state);

		String text = Utils.ToDBC(mTvVipDate.getText().toString());
		mTvVipDate.setText(text);
		
		mBtnRecharNow = (Button) findViewById(R.id.btn_rechargenow);
		mBtnRecharNow.setOnClickListener(this);
		
		mBtnRecharRecord = (Button) findViewById(R.id.btn_rechargerecord);
		mBtnRecharRecord.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_rechargenow:
				goRechargeNowActivity();
				break;
			case R.id.btn_rechargerecord:
				goRechargeRecordActivity();
				break;
		}
	}
	
	private void goRechargeNowActivity()
	{
		Intent intent = new Intent();
    	intent.setClass(this, SetRechargeNowActivity.class);
    	startActivity(intent);
	}
	
	private void goRechargeRecordActivity()
	{
		Intent intent = new Intent();
    	intent.setClass(this, SetRechargeRecordActivity.class);
    	startActivity(intent);
	}
}
