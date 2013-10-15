package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import android.content.res.Resources;

import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.utils.ObjectUtils;

public class Cards {
    
    public List<Card> toCardArray(Resources res, List<BaasioEntity> baasioCards, List<BaasioUser> baasioUsers) {
        ArrayList<Card> cards = new ArrayList<Card>();
        String title = "";
        String subTitle = "";
        float rating = 0;
        int label = -1;
        ArrayList<Person> groupUsers = new ArrayList<Person>();
        for (BaasioEntity baasioCard : baasioCards) {
            JsonNode nodeTitle = baasioCard.getProperty(Card.ENTITY_NAME_TITLE);
            if (!ObjectUtils.isEmpty(nodeTitle)) {
                title = nodeTitle.asText();
            }
            
            JsonNode nodeSubTitle = baasioCard.getProperty(Card.ENTITY_NAME_SUBTITLE);
            if (!ObjectUtils.isEmpty(nodeSubTitle)) {
                subTitle = nodeSubTitle.asText();
            }
            
            JsonNode nodeRating = baasioCard.getProperty(Card.ENTITY_NAME_RATING);
            if (!ObjectUtils.isEmpty(nodeRating)) {
                rating = nodeRating.asInt();
            }
            
            JsonNode nodeLabel = baasioCard.getProperty(Card.ENTITY_NAME_LABEL);
            if (!ObjectUtils.isEmpty(nodeLabel)) {
                label = nodeLabel.asInt();
            }
            
            // TODO 카드 등록 사용자 검색
            groupUsers.clear();
            groupUsers.add(new Person("최영근", res, false));
            
            Card card = new Card(title, subTitle, (int)rating, groupUsers);
            card.label = label;
            
            cards.add(card);
        }
        return cards;
    }
}
