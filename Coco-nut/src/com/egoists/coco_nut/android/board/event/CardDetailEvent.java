package com.egoists.coco_nut.android.board.event;

import com.egoists.coco_nut.android.board.card.Card;

public class CardDetailEvent {
    public Card card;
    
    public CardDetailEvent(Card card) {
        this.card = card;
    }

}
