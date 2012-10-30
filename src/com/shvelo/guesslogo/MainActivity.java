package com.shvelo.guesslogo;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
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
	public Button nextButton;
	public Button restartButton;
	public TextView brandName;
	public View variants;
	public MenuItem menuGuessed;
	
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
        nextButton = (Button)findViewById(R.id.nextButton);
        restartButton = (Button)findViewById(R.id.restartButton);
        brandName = (TextView)findViewById(R.id.brandName);
        variants = findViewById(R.id.variants);
        
        if(BrandManager.allGuessed() || BrandManager.size() - BrandManager.sizeGuessed() == 1) {
        	nextButton.setVisibility(View.GONE);
        	restartButton.setVisibility(View.VISIBLE);
        }
        
        variant1.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 1) {
					guessed();
				} else {
					v.setEnabled(false);
				}
			}
		});
        
        variant2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 2) {
					guessed();
				} else {
					v.setEnabled(false);
				}
			}
		});
        
        variant3.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 3) {
					guessed();
				} else {
					v.setEnabled(false);
				}
			}
		});
        
        variant4.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(brand.correct == 4) {
					guessed();
				} else {
					v.setEnabled(false);
				}
			}
		});
        
        nextButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				next();
			}
		});
        
        restartButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				restart();
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
        
        if(brand.guessed) {
        	guessed();
        } else {
        	
        }
    }
    
    public void guessed() {
    	BrandManager.guessed(brandIndex);
    	brandName.setVisibility(View.VISIBLE);
    	variants.setVisibility(View.GONE);
    	
    	if(BrandManager.allGuessed()) {
        	nextButton.setVisibility(View.GONE);
        	restartButton.setVisibility(View.VISIBLE);
        }
    	
    	if(menuGuessed != null) {
    		int total = BrandManager.size();
            int totalGuessed = BrandManager.sizeGuessed();
            menuGuessed.setTitle("Guessed "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
    	}
    }
    
    public void restart() {
    	BrandManager.restart();
    }
    
    public void next() {
    	BrandManager.next(brandIndex);
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
        
        menuGuessed = menu.findItem(R.id.menuGuessed);
        
        int total = BrandManager.size();
        int totalGuessed = BrandManager.sizeGuessed();
        
        menuGuessed.setTitle("Guessed "+String.valueOf(totalGuessed) + "/" + String.valueOf(total));
        menuGuessed.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem item) {
				BrandManager.showLogoList();
				return false;
			}
        });
        
        
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