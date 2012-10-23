package com.shvelo.guesslogo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class MainActivity extends Activity {

	public int brandIndex;
	public List<Brand> brands;
	public Brand brand;
	public ImageView logoImage;
	public EditText guessField;
	public TextView successText;
	public Button nextButton;
	public Button restartButton;
	public TypedArray brand_names;
	public TypedArray brand_logos;
	public Random rand;
	public Resources res;
	public int[] guessed;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        res = getResources();
        logoImage = (ImageView)findViewById(R.id.logoImage);
        guessField =  (EditText) findViewById(R.id.guessField);
        successText = (TextView) findViewById(R.id.successText);
        nextButton = (Button)findViewById(R.id.nextButton);
        restartButton = (Button)findViewById(R.id.restartButton);
        brand_names = res.obtainTypedArray(R.array.brand_names);
        brand_logos = res.obtainTypedArray(R.array.brand_logos);
        brands = new ArrayList<Brand>();
        rand = new Random();
        
        XmlPullParser parser = res.getXml(R.xml.brands);
        try {
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "brands");
			while (parser.next() != XmlPullParser.END_TAG) {
		        if (parser.getEventType() != XmlPullParser.START_TAG) {
		            continue;
		        }
		        String name = parser.getName();
		        // Starts by looking for the entry tag
		        if (name.equals("entry")) {
		            entries.add(readEntry(parser));
		        } else {
		            skip(parser);
		        }
		    }
		} catch (XmlPullParserException e) {
		} catch (IOException e) {
		}
        
        
        for(int i = 0; i < brand_names.length(); i++) {
        	brands.add(new Brand(brand_names.getString(i), brand_logos.getDrawable(i)));
        }
        
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
        
        startGuessing();
    }
    
    public void startGuessing() {
    	
        brandIndex = rand.nextInt(brands.size());
        brand = new Brand(brands.get(brandIndex));
        
        logoImage.setImageDrawable(brand.logo);
        
        guessField.setText("");
        successText.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

    	guessField.setEnabled(true);
    	showKeyboard();
    }
    
    public void guessed() {
    	hideKeyboard();
    	guessField.setEnabled(false);
    	successText.setVisibility(View.VISIBLE);
    	
    	if(brands.size() > 1) {
    		brands.remove(brandIndex);
    		nextButton.setVisibility(View.VISIBLE);
    	} else {
    		restartButton.setVisibility(View.VISIBLE);
    	}
    }
    
    public void restart() {
    	Intent intent = getIntent();
    	finish();
    	startActivity(intent);
    }
    
    public void showKeyboard(){
    	//((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.SHOW_IMPLICIT );
    	((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(guessField, InputMethodManager.SHOW_IMPLICIT);
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
