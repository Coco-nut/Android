package com.egoists.coco_nut.android.board.card.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.egoists.coco_nut.android.board.card.Voter;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.EtcUtils;
import com.kth.baasio.Baas;

public class CardListAdapter extends BaseAdapter {
    
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Card> mCardList;
    private ImageFetcher mImageFetcher;
    private String mMyUuid;
    
    public CardListAdapter(Context context, ArrayList<Card> cardList) {
        super();
        mContext = context;
        mCardList = cardList;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageFetcher = new ImageFetcher(mContext);
        mMyUuid = Baas.io().getSignedInUser().getUuid().toString(); // 본인 UUID 획득
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
            view.mComments = (TextView)convertView.findViewById(R.id.txtCardListComment);
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
            
            // 카드 상태에 대한 색상 지정
            if (card.status == 2) { // 완료 중 평가 대기 빨간색, 평가 완료 회색
                if (hasParticipants(card) == true && didIVote(card) == false) {
                    view.mTitle.setTextColor(ColoredCardLabel.getColor(0));
                } else {
                    view.mTitle.setTextColor(ColoredCardLabel.getColor(10));
                }
                view.mSubTitle.setTextColor(ColoredCardLabel.getColor(10));
                view.mDate.setTextColor(ColoredCardLabel.getColor(10));
                view.mComments.setTextColor(ColoredCardLabel.getColor(10));
            } else {    // 할일, 하는중은 검은색
                view.mTitle.setTextColor(Color.BLACK);
                view.mSubTitle.setTextColor(Color.BLACK);
                view.mDate.setTextColor(Color.BLACK);
                view.mComments.setTextColor(Color.BLACK);
            }
            view.mSubTitle.setText(card.sub_title);
            
            view.mRatingBar.setRating(card.importance);
            // 시간
            String strStartCal = (card.startdate == null) 
                    ? "" : EtcUtils.getOnlyDateString(card.startdate.getTimeInMillis());
            String strEndCal = (card.enddate == null) 
                    ? "" : EtcUtils.getOnlyDateString(card.enddate.getTimeInMillis());
            if (strStartCal.length() == 0 && strEndCal.length() == 0)
                view.mDate.setText("?");
            else
                view.mDate.setText(strStartCal + " ~ " + strEndCal);
            
            view.mCategory.setBackgroundColor(ColoredCardLabel.getColor(card.label));
            
            if (card.comments != null)  // 댓글 수
                view.mComments.setText(""+card.comments.size());
            
            // 참가자
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
                    // 카드가 완료되고 내가 아직 평가를 안했으면 평점 입력으로 이동한다
                    if (card.status == 2 && hasParticipants(card) == true) {
                        
                        boolean didVote = didIVote(card);
                        
                        if (didVote == false) {
                            // 평점 입력으로 이동
                            Intent i = new Intent(mContext, com.egoists.coco_nut.android.board.card.CardVoteActivity_.class);
                            i.putExtra("card_detail", card);
                            mContext.startActivity(i);
                            return;
                        }
                    }
                    
                    // 카드 디테일로 이동
                    Intent i = new Intent(mContext, com.egoists.coco_nut.android.board.card.CardDetailActivity_.class);
                    AndLog.d("Push card detail event");
                    i.putExtra("card_detail", card);
                    mContext.startActivity(i);
                    
                }
            });
        }
        return convertView;
    }
    
    boolean hasParticipants(Card card) {
        return (card.participants != null && card.participants.size() > 0) ? true : false;
    }
    
    // 내가 평가했는지 여부
    boolean didIVote(Card card) {
        boolean didIVote = false;
        
        if (hasParticipants(card)) {
            AndLog.d("Check whether I have voted");
            if (card.voters != null) {
                for (Voter voter : card.voters) {
                    if (mMyUuid.equals(voter.uuid)) {
                        didIVote = true;
                        break;
                    }
                }
            }
        }
        
        return didIVote;
    }

}
