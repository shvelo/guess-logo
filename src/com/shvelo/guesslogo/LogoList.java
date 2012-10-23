package com.shvelo.guesslogo;

import android.os.Bundle;

import android.app.Activity;
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_logo_list, menu);
        return true;
    }*/
}
