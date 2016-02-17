package com.example.practical15;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

	//LOG TAG
	private final String TAG = "DatabaseHelper";

	// Database Version
	private final static int DATABASE_VERSION = 1;

	// Database Name
	private final static String DATABASE_NAME = "MYDATABASE_DB";

	Context context;

	DBHelper(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){

		db.execSQL(UserTable.CREATE_TABLE);

	}

	public Cursor select(String query)
	{
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.rawQuery(query, null);

		//db.close();

		return cursor;		
	}

	public long insert(String tableName,ContentValues cv)
	{
		long result;

		SQLiteDatabase db=this.getWritableDatabase();

		result=db.insert(tableName, null, cv);

		db.close();

		return result;

	}

	public int update(String tableName,ContentValues cv,String whereClause,String[] whereArgs)
	{
		int result;

		SQLiteDatabase db=this.getWritableDatabase();

		result=db.update(tableName, cv, whereClause, whereArgs);

		db.close();

		return result;
	}

	public int delete(String tableName,String whereClause,String[] whereArgs)
	{
		int result;

		SQLiteDatabase db=this.getWritableDatabase();

		result=db.delete(tableName, whereClause, whereArgs);

		return result;

	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);

		// create new tables
		onCreate(db);

	}


	public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }



	public class UserTable {


		// Table Names
		public final static String TABLE_NAME = "user";

		public final static String KEY_ID = "id";
		public final static String KEY_NAME = "name";
		public final static String KEY_AGE = "age";
		public final static String KEY_USER_ID = "user_id";
		public final static String KEY_PASS = "pass";


		private final static String CREATE_TABLE = "CREATE TABLE "
				+ TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
				+ " TEXT," + KEY_AGE + " INTEGER," + KEY_USER_ID
				+ " INTEGER," + KEY_PASS + " TEXT)";


	}








}
