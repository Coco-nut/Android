package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import android.content.res.Resources;

import com.egoists.coco_nut.android.util.AndLog;
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
        int state = 0;
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
            
            JsonNode nodeState = baasioCard.getProperty(Card.ENTITY_NAME_STATE);
            if (!ObjectUtils.isEmpty(nodeState)) {
                state = nodeState.asInt();
            }
            
            groupUsers.clear();
            JsonNode participants = baasioCard.getProperty(Card.ENTITY_NAME_PARTY);
            if (!ObjectUtils.isEmpty(participants)) {
                int nParicipants = participants.size();
                for (int i=0; i<nParicipants; i++) {
                    JsonNode user = participants.get(i);
                    AndLog.d(user.get(Person.ENTITY_NAME_NAME).asText() + " is joined in this card : " +
                            user.get(Person.ENTITY_NAME_UUID).asText() + " : " + user.get(Person.ENTITY_NAME_PICTURE).asText());
                    groupUsers.add(new Person(
                            user.get(Person.ENTITY_NAME_UUID).asText(),
                            user.get(Person.ENTITY_NAME_NAME).asText(),
                            user.get(Person.ENTITY_NAME_PICTURE).asText(),
                            user.get("isme").asBoolean()));
                }
            }
//            for (BaasioUser baasioUser : baasioUsers) {
//                // TODO groupUsers 와 baasioUsers 를 가지고
//                // TODO 카드에 추가된 참가자들을 추가한다
//                groupUsers.add(new Person(baasioUser.getUuid().toString(), 
//                        baasioUser.getName(), baasioUser.getPicture(), false));
//            }
            
            Card card = new Card(title, subTitle, (int)rating, groupUsers);
            card.uuid = baasioCard.getUuid().toString();
            card.label = label;
            card.status = state;
            
            cards.add(card);
        }
        return cards;
    }
}
