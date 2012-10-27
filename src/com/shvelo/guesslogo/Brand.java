package com.shvelo.guesslogo;

import java.util.List;

import android.graphics.drawable.Drawable;

public class Brand {
	public String name;
	public Drawable logo;
	public List<String> variants;
	public int correct;
	
	public Brand(String newname, Drawable newlogo, List<String> variants, int correct){
		this.name = newname;
		this.logo = newlogo;
		this.variants = variants;
		this.correct = correct;
	}
	
	public Brand(Brand source){
		this.name = source.name;
		this.logo = source.logo;
		this.variants = source.variants;
		this.correct = source.correct;
	}
	
	public String getVariant(int i) {
		return this.variants.get(i - 1);
	}
}
