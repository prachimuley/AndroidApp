package com.example.prohunt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XMLParsar {
	private static final String ns = null;

	public List<Result> parse(InputStream in) throws XmlPullParserException,
			IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Result> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Result> results = new ArrayList<Result>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the result tag
			if (name.equals("results")) {
			} else if (name.equals("result")) {
				results.add(readResult(parser));
			} else {
				skip(parser);
			}
		}
		return results;
	}

	private Result readResult(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String jobtitle = null;
		String company = null;
		String url = null;
		String city = null;
		String state = null;
		double latitude = 0.0;
		double longitude = 0.0;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("jobtitle")) {
				jobtitle = readTitle(parser);
			} else if (name.equals("company")) {
				company = readCompany(parser);
			} else if (name.equals("url")) {
				url = readUrl(parser);
			} else if (name.equals("latitude")) {
				latitude = readlatitude(parser);
			} else if (name.equals("longitude")) {
				longitude = readlongitude(parser);
			} else if (name.equals("city")) {
				city = readCity(parser);
			} else if (name.equals("state")) {
				state = readState(parser);
			} else {
				skip(parser);
			}
		}
		return new Result(jobtitle, company, url, longitude, latitude, city,
				state);
	}

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "jobtitle");
		String jobtitle = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "jobtitle");
		return jobtitle;
	}

	// Processes summary tags in the feed.
	private String readCompany(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "company");
		String company = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "company");
		return company;
	}

	private String readUrl(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "url");
		String url = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "url");
		return url;
	}

	private double readlatitude(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "latitude");
		double latitude = stringToDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "latitude");
		return latitude;
	}

	private double readlongitude(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "longitude");
		double longitude = stringToDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "longitude");
		return longitude;
	}

	private double stringToDouble(String text) {
		double aDouble = Double.parseDouble(text);
		return aDouble;
	}

	private String readCity(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "city");
		String city = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "city");
		return city;
	}

	private String readState(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "state");
		String state = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "state");
		return state;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}
