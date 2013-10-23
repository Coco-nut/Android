package com.egoists.coco_nut.android.group.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.CardCreationActivity;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.group.GroupInvitationActivity;
import com.egoists.coco_nut.android.util.AndLog;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.utils.ObjectUtils;

public class UsersListAdapter extends BaseAdapter {
    private Activity mActivity;
    
    private Context mContext;
    private LayoutInflater mInflater;
    private ImageFetcher mImageFetcher;
    private ArrayList<BaasioUser> mUserList;

    public UsersListAdapter(Context context, ArrayList<BaasioUser> userList) {
        super();
        mContext = context;
        mUserList = userList;
        mImageFetcher = new ImageFetcher(mContext);
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public UsersListAdapter(GroupInvitationActivity activity, Context context, ArrayList<BaasioUser> userList) {
        this(context, userList);
        mActivity = activity;
    }
    
    public UsersListAdapter(CardCreationActivity activity, Context context, ArrayList<BaasioUser> userList) {
        this(context, userList);
    }

    @Override
    public int getCount() {
        AndLog.d("getCount");
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
    
    public void update(List<BaasioUser> userList) {
        AndLog.d("UserList is updated");
        mUserList.clear();
        mUserList.addAll(userList);
        this.notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AndLog.d(position + "-view");
        UsersViewHolder view = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item_userlist, parent, false);

            view = new UsersViewHolder();

            view.mRoot = (ViewGroup)convertView.findViewById(R.id.layoutRoot);
            view.mProfile = (ImageView)convertView.findViewById(R.id.imageProfile);
            view.mName = (TextView)convertView.findViewById(R.id.textUserName);
            view.mBody = (TextView)convertView.findViewById(R.id.textUserPhone);
            view.mJoin = (CheckBox)convertView.findViewById(R.id.checkUserJoin);
            
            if (view != null) {
                convertView.setTag(view);
            }
        } else {
            view = (UsersViewHolder)convertView.getTag();
        }

        final BaasioUser entity = mUserList.get(position);

        if (entity != null) {
            String imageUrl = entity.getPicture();
            if (imageUrl != null) {
                mImageFetcher.loadImage(imageUrl, view.mProfile, R.drawable.card_personphoto_default);
            } else {
                view.mProfile.setImageResource(R.drawable.card_personphoto_default);
            }

            if (!ObjectUtils.isEmpty(entity.getName())) {
                view.mName.setText(entity.getName());
            }
            
            if (!ObjectUtils.isEmpty(entity.getUsername())) {
                view.mBody.setText(entity.getUsername());
            }
            
            view.mJoin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // 사용자 그룹에 추가
                        ((GroupInvitationActivity)mActivity).addUserToGroup(entity.getUuid());
                    } else {
                        // 사용자 삭제
                        ((GroupInvitationActivity)mActivity).removeUserToGroup(entity.getUuid());
                    }
                }
                
            });
        }
        
//        if (view.mRoot != null) {
//            view.mRoot.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 리스트에서 본인 삭제
//                    mUserList.remove(position);
//                    UsersListAdapter.this.notifyDataSetChanged();
//                    
//                    // 사용자 그룹에 추가
//                    ((GroupInvitationActivity)mActivity).addUserToGroup(entity.getUuid());
//                }
//            });
//        }
        return convertView;
    }
}
