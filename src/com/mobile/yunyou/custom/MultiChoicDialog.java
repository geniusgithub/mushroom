package com.mobile.yunyou.custom;

import java.util.List;

import com.mobile.yunyou.R;

import android.content.Context;
import android.view.View;



public class MultiChoicDialog extends AbstractChoickDialog{

	private MultiChoicAdapter<String> mMultiChoicAdapter;

	public MultiChoicDialog(Context context, List<String> list, boolean[] flag) {
		super(context, list);
		
		initData(flag);
	}
	

	protected void initData(boolean flag[]) {
		// TODO Auto-generated method stub
		mMultiChoicAdapter = new MultiChoicAdapter<String>(mContext, mList, flag, R.drawable.selector_checkbox1);
		
		mListView.setAdapter(mMultiChoicAdapter);
		mListView.setOnItemClickListener(mMultiChoicAdapter);   
		
		CustomUtils.setListViewHeightBasedOnChildren(mListView);

	}


	public boolean[] getSelectItem()
	{
		return mMultiChoicAdapter.getSelectItem();
	}
	
}
