package com.egoists.coco_nut.android.board.card.adapter;

public class ColoredCardLabel {
    public static int getColor(int category) {
        switch(category) {
        case 0: //1
            return 0xfff58d7a;
        case 1: //3
            return 0xff79c799;
        case 2: //5
            return 0xff728faf;
        case 3: //6
            return 0xfff9ae68;
        case 4: //7
            return 0xff9e8bb0;
        case 5: //0
            return 0xfff4c9c3;
        case 6: //2
            return 0xffbce0c7;
        case 7: //4
            return 0xffafcce1;
        case 8: //9
            return 0xffd0b998;
        case 9: //8
            return 0x00000000;
        case 10:
            return 0xffbcbec0;
        default:
            return 0xffbcbec0;
        
        }
    }
    
}
