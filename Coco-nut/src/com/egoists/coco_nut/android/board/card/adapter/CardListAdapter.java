package com.egoists.coco_nut.android.board.card.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.board.card.Person;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.DateConverter;

public class CardListAdapter extends BaseAdapter {
    
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Card> mCardList;
    private ImageFetcher mImageFetcher;
    
    public CardListAdapter(Context context, ArrayList<Card> cardList) {
        super();
        mContext = context;
        mCardList = cardList;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageFetcher = new ImageFetcher(mContext);
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
    
    public void update(List<Card> cardList) {
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
            view.mDate = (TextView)convertView.findViewById(R.id.txtCardListDueTo);
            view.mCategory = (TextView)convertView.findViewById(R.id.viewCategory);
            view.mParticipant = (LinearLayout)convertView.findViewById(R.id.layoutCardListParticipant);
            
            if (view != null) {
                convertView.setTag(view);
            }
        } else {
            view = (CardViewHolder)convertView.getTag();
        }
        
        // 검색 결과 출력
        final Card card = mCardList.get(position);
        
        if (card != null) {
            view.mTitle.setText(card.title);
            view.mSubTitle.setText(card.sub_title);
            
            view.mRatingBar.setRating(card.importance);
            Calendar c = card.enddate;
            if (c != null) {
                view.mDate.setText(DateConverter.getStringTime(c.getTimeInMillis()));
            }
            view.mCategory.setBackgroundColor(ColoredCardLabel.getColor(card.label));
            view.mParticipant.removeAllViews();
            
            AndLog.d("show pictures for " + card.participants.size() + " users");
            ImageView pictureView;
            for (Person person : card.participants) {
                pictureView = Person.getImageView(mContext);
                String imageUrl = person.picture;
                if (imageUrl != null) {
                    mImageFetcher.loadImage(imageUrl, pictureView, R.drawable.card_personphoto_default);
                } 
                view.mParticipant.addView(pictureView);
            }
        }
        
        if (view.mRoot != null) {
            view.mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 카드 디테일로 이동
                    Intent i = new Intent(mContext,
                            com.egoists.coco_nut.android.board.card.CardDetailActivity_.class);
                    AndLog.d("Push card detail event");
                    i.putExtra("card_detail", card);
                    mContext.startActivity(i);
                }
            });
        }
        return convertView;
    }

}
