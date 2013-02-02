package com.shvelo.guesslogo;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class LogoList extends SherlockActivity {

	public ImageAdapter adapter;
	public MenuItem menuGuessed;
	public MenuItem menuScore;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
            menuGuessed.setTitle("Guessed: "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
    	}
    	
    	updateScore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.global, menu);
        
        MenuItem menuRestart = menu.findItem(R.id.menuRestart);
        
        menuRestart.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem item) {
				BrandManager.restart();
				return false;
			}
        });
        
        menuGuessed = menu.findItem(R.id.menuGuessed);
        menuScore = menu.findItem(R.id.menuScore);
        
        updateScore();
        
        int total = BrandManager.size();
        int totalGuessed = BrandManager.sizeGuessed();
        
        menuGuessed.setTitle("Guessed "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
        
        return true;
    }
    
    public void updateScore() {
    	if(menuScore != null) {
    		menuScore.setTitle("Score: "+ BrandManager.getScore());
    	}
	}
}
