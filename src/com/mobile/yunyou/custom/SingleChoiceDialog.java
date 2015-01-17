package com.mobile.yunyou.custom;

import java.util.List;

import com.mobile.yunyou.R;


import android.content.Context;

public class SingleChoiceDialog extends AbstractChoickDialog{

	private SingleChoicAdapter<String> mSingleChoicAdapter;

	public SingleChoiceDialog(Context context, List<String> list) {
		super(context, list);
		
		initData();
	}

	protected void initData() {
		// TODO Auto-generated method stub
		mSingleChoicAdapter = new SingleChoicAdapter<String>(mContext, mList, R.drawable.selector_checkbox3);
		
		mListView.setAdapter(mSingleChoicAdapter);
		mListView.setOnItemClickListener(mSingleChoicAdapter);   
		
		CustomUtils.setListViewHeightBasedOnChildren(mListView);
	
	}


	public int getSelectItem()
	{
		return mSingleChoicAdapter.getSelectItem();
	}


}
