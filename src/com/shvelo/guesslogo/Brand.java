package com.shvelo.guesslogo;

import java.util.List;

import android.graphics.drawable.Drawable;

public class Brand {
	public String name;
	public Drawable logo;
	public List<String> variants;
	public int correct;
	public int id;
	public boolean guessed;
	
	public Brand(int id, String newname, Drawable newlogo, List<String> variants, int correct, boolean guessed){
		this.id = id;
		this.name = newname;
		this.logo = newlogo;
		this.variants = variants;
		this.correct = correct;
		this.guessed = guessed;
	}
	
	public Brand(Brand source){
		this.id = source.id;
		this.name = source.name;
		this.logo = source.logo;
		this.variants = source.variants;
		this.correct = source.correct;
		this.guessed = source.guessed;
	}
	
	public String getVariant(int i) {
		return this.variants.get(i - 1);
	}
}
