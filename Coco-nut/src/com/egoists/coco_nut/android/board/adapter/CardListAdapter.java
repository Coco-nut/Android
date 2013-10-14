package com.egoists.coco_nut.android.board.adapter;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.CardCreationActivity;
import com.egoists.coco_nut.android.util.AndLog;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.utils.ObjectUtils;

public class CardListAdapter extends BaseAdapter {
//    private Activity mActivity;
    
    private Context mContext;
    private LayoutInflater mInflater;
//    private ImageFetcher mImageFetcher;
    private ArrayList<BaasioEntity> mCardList;
    
    public CardListAdapter(Context context, ArrayList<BaasioEntity> cardList) {
        super();
        mContext = context;
        mCardList = cardList;
//        mImageFetcher = new ImageFetcher(mContext);
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void update(List<BaasioEntity> cardList) {
        AndLog.d("UserList is updated");
        mCardList.clear();
        mCardList.addAll(cardList);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardViewHolder view = null;
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_card, parent, false);
            
            view = new CardViewHolder();

            view.mRoot = (ViewGroup)convertView.findViewById(R.id.layoutCardRoot);
            view.mTitle = (TextView)convertView.findViewById(R.id.txtCardListTitle);
            view.mSubTitle = (TextView)convertView.findViewById(R.id.txtCardListSubTitle);
            view.mRatingBar = (RatingBar)convertView.findViewById(R.id.ratingListCard);

            if (view != null) {
                convertView.setTag(view);
            }
        } else {
            view = (CardViewHolder)convertView.getTag();
        }
        
        // 검색 결과 출력
        final BaasioEntity entity = mCardList.get(position);

        if (entity != null) {
            JsonNode nodeTitle = entity.getProperty(CardCreationActivity.ENTITY_NAME_TITLE);
            if (!ObjectUtils.isEmpty(nodeTitle)) {
                view.mTitle.setText(nodeTitle.toString().replaceAll("\"", ""));
            }
            
            JsonNode nodeSubTitle = entity.getProperty(CardCreationActivity.ENTITY_NAME_SUBTITLE);
            if (!ObjectUtils.isEmpty(nodeSubTitle)) {
                view.mSubTitle.setText(nodeSubTitle.toString().replaceAll("\"", ""));
            }
            
            JsonNode nodeRating = entity.getProperty(CardCreationActivity.ENTITY_NAME_RATING);
            if (!ObjectUtils.isEmpty(nodeRating)) {
                view.mRatingBar.setRating(Float.parseFloat(nodeRating.toString()));
            }
        }
//        
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
