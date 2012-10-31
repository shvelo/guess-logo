package com.shvelo.guesslogo;

import android.os.Bundle;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

public class LogoList extends Activity {

	public ImageAdapter adapter;
	public MenuItem menuGuessed;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if(android.os.Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_logo_list);
        
        BrandManager.init(getApplicationContext(), this);
        
        adapter = new ImageAdapter(getApplicationContext());
        
        GridView grid = (GridView)findViewById(R.id.logo_list);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,	long arg3) {
				BrandManager.showGuessingScreen(arg2);
			}
        	
        });
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	adapter.notifyDataSetChanged();
    	
    	if(menuGuessed != null) {
    		int total = BrandManager.size();
            int totalGuessed = BrandManager.sizeGuessed();
            menuGuessed.setTitle("Guessed "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        
        MenuItem menuRestart = menu.findItem(R.id.menuRestart);
        
        menuRestart.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem item) {
				BrandManager.restart();
				return false;
			}
        });
        
        menuGuessed = menu.findItem(R.id.menuGuessed);
        
        int total = BrandManager.size();
        int totalGuessed = BrandManager.sizeGuessed();
        
        menuGuessed.setTitle("Guessed "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
        
        return true;
    }
}
