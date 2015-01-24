package com.mobile.yunyou.custom;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.mobile.yunyou.R;

public class SingleChoicePopWindow extends AbstractChoicePopWindow{
	
	private SingleChoicAdapter<String> mSingleChoicAdapter;

	
	
	public SingleChoicePopWindow(Context context,View parentView, List<String> list)
	{
		super(context, parentView, list);
		
		initData();
	}
	

	protected void initData() {
		// TODO Auto-generated method stub
		mSingleChoicAdapter = new SingleChoicAdapter<String>(mContext, mList, R.drawable.selector_checkbox3);
		
		mListView.setAdapter(mSingleChoicAdapter);
		mListView.setOnItemClickListener(mSingleChoicAdapter);   
		
		CustomUtils.setListViewHeightBasedOnChildren(mListView);
	
	}

	public void refreshData(List<String> data, int selItem)
	{
		mSingleChoicAdapter.refreshData(data);
		mSingleChoicAdapter.setSelectItem(selItem);
		CustomUtils.setListViewHeightBasedOnChildren(mListView);
	}
	
	
	
	public int getSelectItem()
	{
		return mSingleChoicAdapter.getSelectItem();
	}


}
