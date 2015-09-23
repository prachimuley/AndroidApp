package com.example.prohunt;

public class Result {
	public final String jobtitle;
	public final String url;
	public final String company;
	public final double longitude;
	public final double latitude;
	public final String city;
	public final String state;

	Result(String jobtitle, String company, String url, double longitude,
			double latitude, String city, String state) {
		this.jobtitle = jobtitle;
		this.company = company;
		this.url = url;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
		this.state = state;
	}

}
