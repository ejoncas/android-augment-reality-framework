package com.restoar.model;

import java.util.ArrayList;
import java.util.List;

public class Commerce {

	private Long id;
	private String name;
	private String description;
	private String address;
	private String category;
	private Double latitude;
	private Double longitude;
	private List<Advertisement> advertisements;
	
	
	public Commerce(Long id, String name, String description, String address,
			String category, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.address = address;
		this.category = category;
		this.latitude = latitude;
		this.longitude = longitude;
		this.advertisements = new ArrayList<Advertisement>();
	}

	public void addAdvertisement(Advertisement ad) {
		advertisements.add(ad);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public List<Advertisement> getAdvertisements() {
		return advertisements;
	}
	public void setAdvertisements(List<Advertisement> advertisements) {
		this.advertisements = advertisements;
	}
}
