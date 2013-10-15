package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.egoists.coco_nut.android.kanban.card.Person;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.utils.ObjectUtils;

public class Cards {
    
    public List<Card> toCardArray(List<BaasioEntity> baasioCards) {
        ArrayList<Card> cards = new ArrayList<Card>();
        String title = "";
        String subTitle = "";
        float rating = 0;
        
        for (BaasioEntity baasioCard : baasioCards) {
            JsonNode nodeTitle = baasioCard.getProperty(Card.ENTITY_NAME_TITLE);
            if (!ObjectUtils.isEmpty(nodeTitle)) {
                title = nodeTitle.toString().replaceAll("\"", "");
            }
            
            JsonNode nodeSubTitle = baasioCard.getProperty(Card.ENTITY_NAME_SUBTITLE);
            if (!ObjectUtils.isEmpty(nodeSubTitle)) {
                subTitle = nodeSubTitle.toString().replaceAll("\"", "");
            }
            
            JsonNode nodeRating = baasioCard.getProperty(Card.ENTITY_NAME_RATING);
            if (!ObjectUtils.isEmpty(nodeRating)) {
                rating = Float.parseFloat(nodeRating.toString());
            }
            
            cards.add(new Card(title, subTitle, (int)rating, new ArrayList<Person>()));
        }
        return cards;
    }
}
