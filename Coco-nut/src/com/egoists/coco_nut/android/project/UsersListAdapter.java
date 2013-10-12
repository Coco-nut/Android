package com.egoists.coco_nut.android.project;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.utils.ObjectUtils;

public class UsersListAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater;

    private ImageFetcher mImageFetcher;
    
    private ArrayList<BaasioUser> mUserList;

    public UsersListAdapter(Context context, ArrayList<BaasioUser> userList) {
        super();

        mContext = context;
        
        mUserList = userList;

        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public BaasioUser getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AndLog.i(position + "-view");
        UsersViewHolder view = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item_userlist, parent, false);

            view = new UsersViewHolder();

            view.mRoot = (ViewGroup)convertView.findViewById(R.id.layoutRoot);
            view.mProfile = (ImageView)convertView.findViewById(R.id.imageProfile);
            view.mName = (TextView)convertView.findViewById(R.id.textName);

            if (view != null) {
                convertView.setTag(view);
            }
        } else {
            view = (UsersViewHolder)convertView.getTag();
        }

        BaasioUser entity = mUserList.get(position);

        if (entity != null) {
            String imageUrl = entity.getPicture();
            if (imageUrl != null) {
                mImageFetcher.loadImage(imageUrl, view.mProfile, R.drawable.person_image_empty);
            } else {
                view.mProfile.setImageResource(R.drawable.person_image_empty);
            }

            if (!ObjectUtils.isEmpty(entity.getUsername())) {
                view.mName.setText(entity.getUsername());
            }

        }
        return convertView;
    }

}
