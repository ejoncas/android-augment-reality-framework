package com.restoar.model;

import java.util.List;

import com.google.common.collect.Lists;

public class Advertisement {
	
	private String id;
	private String title;
	private String description;
	private List<String> tags;
	
	public Advertisement() {
		this.tags = Lists.newArrayList();
	}
	
	public Advertisement(String id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.tags = Lists.newArrayList();
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
