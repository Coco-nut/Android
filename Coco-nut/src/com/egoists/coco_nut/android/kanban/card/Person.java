package com.egoists.coco_nut.android.kanban.card;

import java.io.Serializable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;


public class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4588594544993352199L;
	public String name;
	public boolean isdefault = false;
	public String bitmapPath;
	public boolean isme;
	public Person(String name, boolean isme){
		this.name = name;
		this.isdefault = true; 
		this.isme = isme;
	}
	public Person(String name, String bitmapPath, boolean isme){
		this.name = name;
		this.bitmapPath = bitmapPath;
		this.isme = isme;
		//TODO: 서버에서 비트맵 받아오는법 처리
	}
	public Drawable getPhoto(Resources res)
	{
		if (isdefault)
			return res.getDrawable(R.drawable.card_personphoto_default);
		else
			//TODO : get drawable from path
			return null;
	}
}