package com.shvelo.guesslogo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BrandManager {
	public static Context context;
	public static Activity activity;
	public static List<Brand> brands;
	public static SQLiteDatabase db;
	
	public static void init(Context con, Activity act) {
		context = con;
		activity = act;
        brands = new ArrayList<Brand>();
        
        DBHelper dbHelper = new DBHelper(context);
        try {
			dbHelper.create();
		} catch (IOException e) {
			e.printStackTrace();
		}
        dbHelper.open();
        
        db = dbHelper.get();
        
        Cursor query = db.query("brands", null, null, null, null, null, "name asc", null);
        
        for(int i = 0; i < query.getCount(); i++) {
        	query.moveToNext();
        	
        	int imageResource = context.getResources().getIdentifier(query.getString(query.getColumnIndex("logo")), null, context.getPackageName());
        	
        	List<String> variants = new ArrayList<String>();
        	variants.add(query.getString(query.getColumnIndex("variant1")));
        	variants.add(query.getString(query.getColumnIndex("variant2")));
        	variants.add(query.getString(query.getColumnIndex("variant3")));
        	variants.add(query.getString(query.getColumnIndex("variant4")));
        	
        	Brand b = new Brand(
        			query.getInt(query.getColumnIndex("_id")),
        			query.getString(query.getColumnIndex("name")),
        			context.getResources().getDrawable(imageResource),
        			variants,
        			query.getInt(query.getColumnIndex("correct")),
        			(query.getInt(query.getColumnIndex("guessed")) == 1)
        	);
        	brands.add(b);
        }
	}
	
	public static void saveUnguessed() {
		db.beginTransaction();
		for(int i = 0; i < brands.size(); i++) {
			int guessedInt = 0;
			if(brands.get(i).guessed) {
				guessedInt = 1;
			}
			db.execSQL("UPDATE brands SET guessed=? WHERE _id=?", new String[] {
					String.valueOf(guessedInt),
					String.valueOf(brands.get(i).id)
			});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public static Brand get(int id) {
		return brands.get(id);
	}
	
	public static void restart() {
		for(int i = 0; i < brands.size(); i++) {
			brands.get(i).guessed = false;
		}
		db.execSQL("UPDATE brands SET guessed=0");
		showLogoList();
	}
	
	public static void showGuessingScreen(int id) {

        Intent intent = new Intent();
        intent.setAction("com.shvelo.guesslogo.GUESS_BRAND");
        intent.putExtra("brand", id);
        
        activity.startActivity(intent);
	}
	
	public static void showLogoList() {
		Intent intent = activity.getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
	}
	
	public static Boolean validateGuess(int id, String name) {
		
    	if(name.toLowerCase().contentEquals(get(id).name.toLowerCase())) {
    		return true;
    	} else {
    		return false;
    	}
	}
	
	public static void guessed(int id) {
		brands.get(id).guessed = true;
		saveUnguessed();
	}
	
	public static int size() {
		return brands.size();
	}
	
	public static int sizeGuessed() {
		int size = 0;
		for(int i = 0; i < brands.size(); i++) {
			if(brands.get(i).guessed)
				size++;
		}
		return size;
	}
	
	public static Boolean allGuessed() {
		for(int i = 0; i < brands.size(); i++) {
			if(brands.get(i).guessed)
				return true;
		}
		return false;
	}
}
