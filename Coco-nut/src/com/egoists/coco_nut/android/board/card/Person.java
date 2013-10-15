package com.egoists.coco_nut.android.board.card;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.egoists.coco_nut.android.R;


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

    public ImageView getImageView(Context con) {
        ImageView imgIcon = new ImageView(con);
        
        // width = height = 40dp
        final float scale = con.getResources().getDisplayMetrics().density;
        int pixels = (int) (40 * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pixels, pixels);
        // margin = 8dp
        pixels = pixels/5;
        layoutParams.setMargins(pixels, pixels, pixels, pixels);
        imgIcon.setLayoutParams(layoutParams);
        imgIcon.setImageDrawable(photo);
        return imgIcon;
    }
}