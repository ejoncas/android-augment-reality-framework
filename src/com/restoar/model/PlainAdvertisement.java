package com.restoar.model;

public class PlainAdvertisement extends Advertisement {

	private Long commerceId;
	private String commerceName;
	private String commerceDescription;
	private String address;
	private String category;
	private Double latitude;
	private Double longitude;


	public Long getCommerceId() {
		return commerceId;
	}

	public void setCommerceId(Long commerceId) {
		this.commerceId = commerceId;
	}

	public String getCommerceName() {
		return commerceName;
	}

	public void setCommerceName(String commerceName) {
		this.commerceName = commerceName;
	}

	public String getCommerceDescription() {
		return commerceDescription;
	}

	public void setCommerceDescription(String commerceDescription) {
		this.commerceDescription = commerceDescription;
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

}
