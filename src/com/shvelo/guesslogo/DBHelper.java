package com.shvelo.guesslogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private SQLiteDatabase database;

	private final Context context;

	private static final String DATABASE_NAME = "brands.db3";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_CREATE = "CREATE TABLE brands("+
			"_id INTEGER PRIMARY KEY,"+
			"name TEXT,"+
			"logo TEXT,"+
			"variant1 TEXT,"+
			"variant2 TEXT,"+
			"variant3 TEXT,"+
			"variant4 TEXT,"+
			"correct INTEGER,"+
			"guessed INTEGER"+
	")";

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DBHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		
		database = getReadableDatabase();
	}
	
	/**
	 * Close the database
	 */
	@Override
	public synchronized void close() {
		if (database != null)
			database.close();
		
		super.close();
	}
	
	/**
	 * Get the database
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase get() {
		return database;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		database = db;
		database.execSQL(TABLE_CREATE);
		loadDB();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		List<Integer> guessed = new ArrayList<Integer>();
		
		Cursor query = db.query("brands", null, null, null, null, null, "name asc", null);
		for(int i = 0; i < query.getCount(); i++) {
        	query.moveToNext();
        	if(query.getInt(query.getColumnIndex("guessed")) == 1)
        		guessed.add(query.getInt(query.getColumnIndex("_id")));
		}
		
		db.execSQL("DROP TABLE brands");
		db.execSQL(TABLE_CREATE);
		loadDB();
		
		db.beginTransaction();		
		for(int i = 0; i < guessed.size(); i++) {
			db.execSQL("UPDATE brands SET guessed=1 WHERE _id=?", new String[] {
					guessed.get(i).toString()
			});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	private void loadDB() {
		new Thread(new Runnable() {
			public void run() {
				try {
					loadLogos();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

	private void loadLogos() throws IOException {
		InputStream inputStream = context.getAssets().open("brands.sql");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line;
		
		database.beginTransaction();		
		try {
			while ((line = reader.readLine()) != null) {
				database.execSQL(line);
			}
			database.setTransactionSuccessful();
		} finally {
			reader.close();
			database.endTransaction();
		}
	}
	
}