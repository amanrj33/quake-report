package com.example.android.quakereport;

 class Earthquake {
    private String mLocation;
    private double mMagnitude;
    private long mDate;
    private String mUrl;

     Earthquake(String location, double magnitude, long date, String url){
        mLocation=location;
        mMagnitude=magnitude;
        mDate=date;
        mUrl=url;
    }

     String getLocation() {
        return mLocation;
    }

     double getMagnitude() {
        return mMagnitude;
    }

     long getDate() {
        return mDate;
    }

     String getUrl(){ return mUrl;}
}
