package com.example.menudunice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, "myDB", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table orders ("
				+ "_id integer primary key autoincrement,"
				+ "date timestamp default current_timestamp" + ");");

		db.execSQL("create table items ("
				+ "_id integer primary key autoincrement, title text,"
				+ "price integer, name text);");

		db.execSQL("create table orders_content (order_id integer , item_id integer,"
				+ "foreign key(item_id) REFERENCES items(Id), foreign key(order_id) REFERENCES orders(Id));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}
