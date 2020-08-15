package com.example.android.quakereport;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String mUrl;
     EarthquakeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl == null)
            return null;
        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}

