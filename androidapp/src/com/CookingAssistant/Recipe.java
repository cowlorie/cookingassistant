package com.CookingAssistant;

import java.io.Serializable;

public class Recipe implements Serializable{
	Long id;
	String name;
	boolean favorite;
	String cook_time;
	String[] ingredients;
	String serv_size;
	String[] steps;
	String photo_urls;
	String desc;	
}
