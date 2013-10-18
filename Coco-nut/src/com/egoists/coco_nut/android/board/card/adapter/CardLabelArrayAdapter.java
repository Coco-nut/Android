package com.egoists.coco_nut.android.board.card.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CardLabelArrayAdapter extends ArrayAdapter<String> {
    //생성자를 정의해준다
    public CardLabelArrayAdapter(Context context, int resource, String[] objects){
        super(context, resource, objects);
    }
     
    //getView를 overriding 해준다
    public View getView(int position, View convertView, ViewGroup parent){
        View v = super.getView(position, convertView, parent);
        if(position == getCount()){
            ((TextView) v.findViewById(android.R.id.text1)).setText("");
            ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
        }
        return v;
    }
     
    //getCount를 overriding 해준다
    public int getCount() {
        return super.getCount() - 1;
    }
}
