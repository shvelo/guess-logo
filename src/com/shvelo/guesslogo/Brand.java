package com.shvelo.guesslogo;

import android.graphics.drawable.Drawable;

public class Brand {
	public String name;
	public Drawable logo;
	
	public Brand(String newname, Drawable newlogo){
		this.name = newname;
		this.logo = newlogo;
	}
	
	public Brand(Brand source){
		this.name = source.name;
		this.logo = source.logo;
	}
}
