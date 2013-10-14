package com.egoists.coco_nut.android.kanban.card;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;

public class KanbanData {
	public HashMap<String, Person> participants;
	public ArrayList<Card> Do;
	public ArrayList<Card> Doing;
	public ArrayList<Card> Done;
	private String[] label_name_list = 
		{"라벨 1", "라벨 2", "라벨 3", "라벨 4", "라벨 5", 
		"라벨 6", "라벨 7", "라벨 8", "라벨 9", "라벨 0"};
	
	public KanbanData()
	{
		participants = new HashMap<String, Person>();
		Do = new ArrayList<Card>();
		Doing = new ArrayList<Card>();
		Done = new ArrayList<Card>();
	}
	
	public static int getLabelColor(int label)
	{
		switch(label)
		{
		case 1:
			return Color.rgb(245, 140, 126);
		case 2:
			return Color.rgb(119, 199, 157);
		case 3:
			return Color.rgb(114, 144, 173);
		case 4:
			return Color.rgb(249, 173, 115);
		case 5:
			return Color.rgb(158, 140, 173);
		case 6:
			return Color.rgb(246, 201, 197);
		case 7:
			return Color.rgb(187, 224, 201);
		case 8:
			return Color.rgb(195, 217, 231);
		case 9:
			return Color.rgb(208, 185, 156);
		case 0:
			return Color.rgb(189, 190, 192);
		default:
			return -1;
		}
	}
	public String getLabelName(int label)
	{
		return label_name_list[label];
	}
	public void setLabelName(String[] label_names)
	{
		label_name_list = label_names;
		//TODO: report change to server
	}
}
