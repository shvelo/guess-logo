package com.shvelo.guesslogo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class BrandManager {
	public static Context context;
	public static Activity activity;
	public static List<Brand> brands;
	public static List<Integer> unguessed;
	public static Random rand;
	public static SharedPreferences settings;
	public static SQLiteDatabase db;
	
	public static void init(Context con, Activity act) {
		context = con;
		activity = act;
		Resources res = context.getResources();
		settings = activity.getPreferences(0) ;
		TypedArray brand_names = res.obtainTypedArray(R.array.brand_names);
		TypedArray brand_logos = res.obtainTypedArray(R.array.brand_logos);
        brands = new ArrayList<Brand>();
        unguessed = new ArrayList<Integer>();
        rand = new Random();
        
        DBHelper dbHelper = new DBHelper(context);
        try {
			dbHelper.create();
		} catch (IOException e) {
			e.printStackTrace();
		}
        dbHelper.open();
        
        db = dbHelper.get();
        
        Cursor query = db.query("brands", null, null, null, null, null, null, null);
        
        for(int i = 0; i < query.getCount(); i++) {
        	query.moveToNext();
        	
        	int imageResource = context.getResources().getIdentifier(query.getString(1), null, context.getPackageName());
        	
        	List<String> variants = new ArrayList<String>();
        	variants.add(query.getString(2));
        	variants.add(query.getString(3));
        	variants.add(query.getString(4));
        	variants.add(query.getString(5));
        	
        	Brand b = new Brand(
        			query.getString(0),
        			context.getResources().getDrawable(imageResource),
        			variants,
        			query.getInt(6)
        	);
        	brands.add(b);
        }
        
        String savedValue = settings.getString("unguessed", null);
        
        if(savedValue == null) {
        	for(int i = 0; i < brand_names.length(); i++) {
            	unguessed.add(i);
            }
        	
        	saveUnguessed();
        } else if( savedValue.length() > 0){
        	Log.i("saved string","Value: "+savedValue);
        	String[] savedList = savedValue.split("\\|");
        	
        	for(int i = 0; i < savedList.length; i++) {
        		unguessed.add(Integer.valueOf(savedList[i]));
        	}
        }
	}
	
	public static void saveUnguessed() {
		String serializedString = "";
    	for(int i = 0; i < unguessed.size(); i++) {
    		if(i == unguessed.size() - 1) {
    			serializedString += unguessed.get(i).toString();
    		} else {
    			serializedString += unguessed.get(i).toString() + "|";
    		}
    	}
    	
    	Editor settingsEditor = settings.edit();
    	settingsEditor.putString("unguessed", serializedString);
    	settingsEditor.commit();
	}
	
	public static Brand get(int id) {
		return brands.get(id);
	}
	
	public static void restart() {
		Editor settingsEditor = settings.edit();
    	settingsEditor.remove("unguessed");
    	settingsEditor.commit();
    	
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
		unguessed.remove(Integer.valueOf(id));
		saveUnguessed();
	}
	
	public static int size() {
		return brands.size();
	}
	
	public static Boolean allGuessed() {
		if(unguessed.size() > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static Boolean isGuessed(int id) {
		for(int i = 0; i < unguessed.size(); i++) {
			if(unguessed.get(i).equals(Integer.valueOf(id))) {
				return false;
			}
		}
		return true;
	}
}
