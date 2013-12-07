package com.restoar.model;

public class Advertisement {
	
	private String title;
	private String description;
	private String category;
	private Double latitude;
	private Double longitude;
	
	
	public Advertisement(String title, String description, String category, Double latitude,
			Double longitude) {
		super();
		this.title = title;
		this.description = description;
		this.category = category;
		this.latitude = latitude;
		this.longitude = longitude;
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
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	

}
