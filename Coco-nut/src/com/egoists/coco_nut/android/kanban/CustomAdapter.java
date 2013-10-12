package com.egoists.coco_nut.android.kanban;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * TODO 이름 꼭 바꿀 것!
 * @author younggeun
 *
 */
public class CustomAdapter extends ArrayAdapter<CustomRow> {
    private Context mContext;
    private ArrayList<CustomRow> mCards;
    private int mRes;
    private Resources mResources;
    private LayoutInflater mLayInflater;

    public CustomAdapter(Context context, int resource, ArrayList<CustomRow> objects) {
        super(context, resource, objects);
        
        this.mContext = context;
        this.mRes = resource;
        this.mCards = objects;
        this.mResources = mContext.getResources();
        this.mLayInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cView = convertView;
        
        if(cView == null){
            LayoutInflater inflate = LayoutInflater.from(mContext);
            cView = inflate.inflate(mRes, null);
//          cView = inflate.inflate(mResource, parent, false);
        }
        
        return cView;
    }

}
