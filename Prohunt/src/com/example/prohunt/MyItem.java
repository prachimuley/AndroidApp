package com.example.prohunt;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class MyItem implements Parcelable {
	public int num;
	public LatLng mPosition;
	public String companyName;
	List<MyItemList> mItemList = new ArrayList<MyItemList>();

	MyItem(String companyName, LatLng mPosition, String url, String jobtitle,
			String company) {
		this.companyName = companyName;
		this.num = 1;
		this.mPosition = mPosition;
		MyItemList mil = new MyItemList(url, jobtitle, company);
		mItemList.add(mil);
	}

	public List<MyItemList> jobList() {
		return mItemList;
	}

	/* For incrmenting the number of job in a company */
	public int incrementNum(String url, String jobtitle, String company) {
		MyItemList mil = new MyItemList(url, jobtitle, company);
		mItemList.add(mil);
		num = num + 1;
		return num;
	}

	public LatLng getlatLong() {
		return mPosition;
	}

	public int getNum() {
		return num++;
	}

	public String getCompany() {
		return companyName;
	}

	public MyItem(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		// We just need to write each field into the
		// parcel. When we read from parcel, they
		// will come back in the same order
		arg0.writeList(this.mItemList);
		arg0.writeString(this.companyName);
		arg0.writeInt(this.num);
	}

	public static final Parcelable.Creator<MyItem> CREATOR = new Parcelable.Creator<MyItem>() {
		public MyItem createFromParcel(Parcel in) {
			return new MyItem(in);
		}

		public MyItem[] newArray(int size) {
			return new MyItem[size];
		}
	};

	private void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		this.mItemList = in.readArrayList(null);
		this.companyName = in.readString();
		this.num = in.readInt();
	}
}
