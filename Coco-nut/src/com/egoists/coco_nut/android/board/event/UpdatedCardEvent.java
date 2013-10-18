package com.egoists.coco_nut.android.board.event;

import com.egoists.coco_nut.android.board.card.Card;

public class UpdatedCardEvent {
    public Card card;
    
    public UpdatedCardEvent(Card card) {
        this.card = card;
    }

}
