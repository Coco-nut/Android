package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
                
        for (BaasioEntity baasioCard : baasioCards) {
            String title = "";
            String subTitle = "";
            String description = "";
            float rating = 0;
            int label = -1;
            int state = 0;
            long startMilSec = -1;
            long endMilSec = -1;
            long timeOfDoing = -1;
            long timeOfDone = -1;
            
            // 제목
            JsonNode nodeTitle = baasioCard.getProperty(Card.ENTITY_NAME_TITLE);
            if (!ObjectUtils.isEmpty(nodeTitle)) {
                title = nodeTitle.asText();
            }
            // 부제목
            JsonNode nodeSubTitle = baasioCard.getProperty(Card.ENTITY_NAME_SUBTITLE);
            if (!ObjectUtils.isEmpty(nodeSubTitle)) {
                subTitle = nodeSubTitle.asText();
            }
            // 상세 설명
            JsonNode nodeDescription = baasioCard.getProperty(Card.ENTITY_NAME_DESCRIPTION);
            if (!ObjectUtils.isEmpty(nodeDescription)) {
                description = nodeDescription.asText();
            }
            // 중요도
            JsonNode nodeRating = baasioCard.getProperty(Card.ENTITY_NAME_RATING);
            if (!ObjectUtils.isEmpty(nodeRating)) {
                rating = nodeRating.asInt();
            }
            // 라벨
            JsonNode nodeLabel = baasioCard.getProperty(Card.ENTITY_NAME_LABEL);
            if (!ObjectUtils.isEmpty(nodeLabel)) {
                label = nodeLabel.asInt();
            }
            // 카드 상태
            JsonNode nodeState = baasioCard.getProperty(Card.ENTITY_NAME_STATE);
            if (!ObjectUtils.isEmpty(nodeState)) {
                state = nodeState.asInt();
            }
            // 시작 시간
            JsonNode nodeStartMilSec = baasioCard.getProperty(Card.ENTITY_NAME_START_DATE);
            if (!ObjectUtils.isEmpty(nodeStartMilSec)) {
                startMilSec = nodeStartMilSec.asLong();
            }
            // 종료 시간
            JsonNode nodeEndMilSec = baasioCard.getProperty(Card.ENTITY_NAME_DUETO_DATE);
            if (!ObjectUtils.isEmpty(nodeEndMilSec)) {
                endMilSec = nodeEndMilSec.asLong();
            }
            // 하는 중으로 이동 시간
            JsonNode nodeTimeOfDoing = baasioCard.getProperty(Card.ENTITY_NAME_DOING_DATE);
            if (!ObjectUtils.isEmpty(nodeTimeOfDoing)) {
                timeOfDoing = nodeTimeOfDoing.asLong();
            }
            // 한일로 이동 시간
            JsonNode nodeTimeOfDone = baasioCard.getProperty(Card.ENTITY_NAME_DONE_DATE);
            if (!ObjectUtils.isEmpty(nodeTimeOfDone)) {
                timeOfDone = nodeTimeOfDone.asLong();
            }
            
            // 카드 참가자
            ArrayList<Person> groupUsers = new ArrayList<Person>();
            JsonNode participants = baasioCard.getProperty(Card.ENTITY_NAME_PARTY);
            if (!ObjectUtils.isEmpty(participants)) {
                int nParicipants = participants.size();
                for (int i=0; i<nParicipants; i++) {
                    JsonNode user = participants.get(i);
                    
                    AndLog.d(user.get(Person.ENTITY_NAME_NAME).asText() + " is joined in " + title + " card : " +
                            user.get(Person.ENTITY_NAME_UUID).asText() + " : " + user.get(Person.ENTITY_NAME_PICTURE).asText());
                    groupUsers.add(new Person(
                            user.get(Person.ENTITY_NAME_UUID).asText(),
                            user.get(Person.ENTITY_NAME_NAME).asText(),
                            user.get(Person.ENTITY_NAME_PICTURE).asText(),
                            user.get("isme").asBoolean(),
                            user.get(Person.ENTITY_NAME_SUM_RATE).asInt()));
                }
            }
            
            // 카드 투표자
            ArrayList<Voter> voters = new ArrayList<Voter>();
            JsonNode jnodeVoters = baasioCard.getProperty(Card.ENTITY_NAME_VOTERS);
            if (!ObjectUtils.isEmpty(jnodeVoters)) {
                int n = jnodeVoters.size();
                for (int i=0; i<n; i++) {
                    JsonNode voter = jnodeVoters.get(i);
                    
                    AndLog.d(voter.get(Voter.ENTITY_NAME_UUID).asText() + " voted");
                    voters.add(new Voter(voter.get(Voter.ENTITY_NAME_UUID).asText()));
                }
            }
            
            Card card = new Card(title, subTitle, (int)rating, groupUsers);
            card.voters = (voters.size() > 0) ? voters : null;
            card.discription = description;
            card.uuid = baasioCard.getUuid().toString();
            card.label = label;
            card.status = state;
            Calendar startC = new GregorianCalendar();
            Calendar endC = new GregorianCalendar();
            if (startMilSec > 0) {
                startC.setTimeInMillis(startMilSec);
                card.startdate = startC;
            }
            if (endMilSec > 0) {
                endC.setTimeInMillis(endMilSec);
                card.enddate = endC;
            }
            Calendar timeOfDoingC = new GregorianCalendar();
            Calendar timeOfDoneC = new GregorianCalendar();
            if (timeOfDoing > 0) {
                timeOfDoingC.setTimeInMillis(timeOfDoing);
                card.timeofdoing = timeOfDoingC;
            }
            if (timeOfDone > 0) {
                timeOfDoneC.setTimeInMillis(timeOfDone);
                card.timeofdone = timeOfDoneC;
            }
            
            cards.add(card);
        }
        return cards;
    }
}
