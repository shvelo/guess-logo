package com.shvelo.guesslogo;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class MainActivity extends Activity {

	public int brandIndex;
	public Brand brand;
	public ImageView logoImage;
	public EditText guessField;
	public TextView successText;
	public Button backButton;
	public Button nextButton;
	public Button restartButton;
	public Resources res;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        res = getResources();
        logoImage = (ImageView)findViewById(R.id.logoImage);
        guessField =  (EditText) findViewById(R.id.guessField);
        successText = (TextView) findViewById(R.id.successText);
        backButton = (Button)findViewById(R.id.backButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        restartButton = (Button)findViewById(R.id.restartButton);
        
        successText.setText(R.string.success);
        
        guessField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
              
            	String guess = s.toString();
            	if(guess.toLowerCase().contentEquals(brand.name.toLowerCase())) {
            		guessed();
            	}
              
            }
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
         });
        
        backButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BrandManager.showLogoList();
			}
		});
        
        nextButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				startGuessing();
			}
		});
        
        restartButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				restart();
			}
		});
        
        int intentBrand = getIntent().getIntExtra("brand", -1);
        if(intentBrand == -1) {
        	startGuessing();
        } else {
        	startGuessing(intentBrand);
        }
    }
    
    public void startGuessing(int index) {
    	
    	brandIndex = index;
        brand = BrandManager.get(index);
        
        logoImage.setImageDrawable(brand.logo);
        
        guessField.setText("");
        successText.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

        if(BrandManager.isGuessed(brandIndex)) {
        	guessed();
        } else {
        	guessField.setEnabled(true);
        	showKeyboard();
        }
    }
    
    public void startGuessing() {
    	
    	brandIndex = BrandManager.randomIndex();
        brand = new Brand(BrandManager.get(brandIndex));
        
        logoImage.setImageDrawable(brand.logo);
        
        guessField.setText("");
        successText.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        
        if(BrandManager.isGuessed(brandIndex)) {
        	guessed();
        } else {
        	guessField.setEnabled(true);
        	showKeyboard();
        }
    }
    
    public void guessed() {
    	hideKeyboard();
    	BrandManager.guessed(brandIndex);
    	guessField.setEnabled(false);
    	successText.setVisibility(View.VISIBLE);
    	
    	if(BrandManager.allGuessed()) {
    		successText.setText(R.string.all_logos_guessed);
    		restartButton.setVisibility(View.VISIBLE);
    	} else {
    		nextButton.setVisibility(View.VISIBLE);
    	}
    }
    
    public void restart() {
    	BrandManager.restart();
    }
    
    public void showKeyboard(){
    	((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_IMPLICIT_ONLY );
    }
    
    public void hideKeyboard(){
    	((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(guessField.getWindowToken(), 0);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/
}
