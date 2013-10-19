package com.egoists.coco_nut.android.board.event;

import java.util.List;

import com.egoists.coco_nut.android.board.card.Card;

public class AllCardsEvent {

    public String groupUuid;
    public final List<Card> cards;
    public AllCardsEvent(List<Card> cards) {
        this.cards = cards;
    }

}
