package com.egoists.coco_nut.android.kanban.card;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;


public class Person{
	public String name;
	public Drawable photo;
	public boolean isme;
	public Person(String name, Resources res, boolean isme){
		this.name = name;
		this.photo =res.getDrawable(R.drawable.card_personphoto_default); 
		this.isme = isme;
	}
	public Person(String name, Bitmap photo, Resources res, boolean isme){
		this.name = name;
		this.photo = new BitmapDrawable(res,photo);
		this.isme = isme;
		//TODO: 서버에서 비트맵 받아오는법 처리
	}
}