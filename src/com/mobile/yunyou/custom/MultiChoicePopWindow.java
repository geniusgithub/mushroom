package com.mobile.yunyou.custom;

import java.util.List;

import com.mobile.yunyou.R;

import android.content.Context;
import android.view.View;



public class MultiChoicePopWindow extends AbstractChoicePopWindow{

	
	private MultiChoicAdapter<String> mMultiChoicAdapter;
	
	public MultiChoicePopWindow(Context context,View parentView, List<String> list, boolean flag[])
	{
		super(context, parentView, list);
		
		initData(flag);
	}
	

	protected void initData(boolean flag[]) {
		// TODO Auto-generated method stub
		mMultiChoicAdapter = new MultiChoicAdapter<String>(mContext, mList, flag, R.drawable.selector_checkbox1);
		
		mListView.setAdapter(mMultiChoicAdapter);
		mListView.setOnItemClickListener(mMultiChoicAdapter);   
		
		CustomUtils.setListViewHeightBasedOnChildren(mListView);

	}


	public void setSelectItem(boolean [] flag)
	{
		mMultiChoicAdapter.setSelectItem(flag);
		mMultiChoicAdapter.notifyDataSetChanged();
	}

	public boolean[] getSelectItem()
	{
		return mMultiChoicAdapter.getSelectItem();
	}
	
}
