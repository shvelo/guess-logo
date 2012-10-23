package com.shvelo.guesslogo;

import android.os.Bundle;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.GridView;

public class LogoList extends Activity {

	ImageAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_list);
        
        BrandManager.init(getApplicationContext(), this);
        
        adapter = new ImageAdapter(getApplicationContext());
        
        GridView grid = (GridView)findViewById(R.id.logo_list);
        grid.setAdapter(adapter);
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
        
        MenuItem menuGuessed = menu.findItem(R.id.menuGuessed);
        
        int total = BrandManager.size();
        int totalGuessed = total - BrandManager.unguessed.size();
        
        menuGuessed.setTitle("Guessed "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
        
        return true;
    }
}
