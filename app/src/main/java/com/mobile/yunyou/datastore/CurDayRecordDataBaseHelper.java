package com.mobile.yunyou.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CurDayRecordDataBaseHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "CurDayRecord.db";

	
	public CurDayRecordDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		
		db.execSQL(CurDayRecordDBManager.CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL("DROP TABLE IF EXISTS " + CurDayRecordDBManager.TABLE_NAME);

		
		onCreate(db);
	}

}
