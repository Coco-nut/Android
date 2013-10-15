package com.egoists.coco_nut.android.board.event;

import java.util.List;

import com.egoists.coco_nut.android.board.card.Card;

public class MyCardsEvent {
    public final List<Card> myCards;
    public MyCardsEvent(List<Card> myCards) {
        this.myCards = myCards;
    }
}
