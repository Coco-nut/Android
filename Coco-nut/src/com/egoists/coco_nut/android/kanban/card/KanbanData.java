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
			return Color.parseColor("#F4C9C3");
		case 2:
			return Color.parseColor("#F58D7A");
		case 3:
			return Color.parseColor("#BCE0C7");
		case 4:
			return Color.parseColor("#79C799");
		case 5:
			return Color.parseColor("#AFCCE1");
		case 6:
			return Color.parseColor("#728FAF");
		case 7:
			return Color.parseColor("#F9AE68");
		case 8:
			return Color.parseColor("#9E8BB0");
		case 9:
			return Color.parseColor("#BCBEC0");
		case 0:
			return Color.parseColor("#D0B998");
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
