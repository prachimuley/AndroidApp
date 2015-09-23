package com.example.prohunt;

public class Node {
	public final String jobtitle;
	public final String company;
	public final String url;
	public final double longitude;
	public final double latitude;
	public final String city;
	public final String state;

	Node(String company, String jobtitle, String url, double longitude,
			double latitude, String city, String state) {
		this.jobtitle = jobtitle;
		this.company = company;
		this.url = url;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
		this.state = state;
	}

	public String getJob() {
		return jobtitle;
	}

	public String getCompany() {
		return company;
	}

	public String getUrl() {
		return url;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

}
