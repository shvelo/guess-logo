package com.shvelo.guesslogo;

import android.os.Bundle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class MainActivity extends Activity {

	public int brandIndex;
	public Brand brand;
	public ImageView logoImage;
	public Resources res;
	public Button variant1;
	public Button variant2;
	public Button variant3;
	public Button variant4;
	public TextView brandName;
	public View variants;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        res = getResources();
        logoImage = (ImageView)findViewById(R.id.logoImage);
        
        variant1 = (Button)findViewById(R.id.variant1);
        variant2 = (Button)findViewById(R.id.variant2);
        variant3 = (Button)findViewById(R.id.variant3);
        variant4 = (Button)findViewById(R.id.variant4);
        brandName = (TextView)findViewById(R.id.brandName);
        variants = findViewById(R.id.variants);
        
        variant1.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 1) {
					guessed();
					Log.v("guess","correct");
				}
			}
		});
        
        variant2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 2) {
					guessed();
					Log.v("guess","correct");
				}
			}
		});
        
        variant3.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 3) {
					guessed();
					Log.v("guess","correct");
				}
			}
		});
        
        variant4.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 4) {
					guessed();
					Log.v("guess","correct");
				}
			}
		});
        
        int intentBrand = getIntent().getIntExtra("brand", -1);
        if(intentBrand != -1) {
        	startGuessing(intentBrand);
        }
    }
    
    public void startGuessing(int index) {
    	
    	brandIndex = index;
        brand = BrandManager.get(index);
        
        logoImage.setImageDrawable(brand.logo);
        
        variant1.setText(brand.getVariant(1));
        variant2.setText(brand.getVariant(2));
        variant3.setText(brand.getVariant(3));
        variant4.setText(brand.getVariant(4));
        brandName.setText(brand.name);
        
        if(BrandManager.isGuessed(brandIndex)) {
        	guessed();
        } else {
        	
        }
    }
    
    public void guessed() {
    	BrandManager.guessed(brandIndex);
    	brandName.setVisibility(View.VISIBLE);
    	variants.setVisibility(View.GONE);
    }
    
    public void restart() {
    	BrandManager.restart();
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
                
        if(android.os.Build.VERSION.SDK_INT > 10) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        MenuItem menuGuessed = menu.findItem(R.id.menuGuessed);
        
        int total = BrandManager.size();
        int totalGuessed = total - BrandManager.unguessed.size();
        
        menuGuessed.setTitle("Guessed "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
        
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, LogoList.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}