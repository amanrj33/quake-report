package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
 final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /*Sample JSON response for a USGS query */
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
     static List<Earthquake> fetchEarthquakeData(String requestUrl){
        List<Earthquake> earthquakesFromJson;
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        earthquakesFromJson= extractEarthquakesFromJson(jsonResponse);
        return earthquakesFromJson;
    }
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating url",e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws  IOException{
        String jsonResponse = "";
        if(url == null)
            return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<Earthquake> extractEarthquakesFromJson(String jsonResponse) {
       if (TextUtils.isEmpty(jsonResponse))
           return  null;
        List<Earthquake> earthquakes = new ArrayList<>();
       try {
           JSONObject baseResponse = new JSONObject(jsonResponse);
           JSONArray featuresArray = baseResponse.getJSONArray("features");
           for (int i = 0; i < featuresArray.length(); i++) {
               JSONObject featureElements = featuresArray.getJSONObject(i);
               JSONObject properties = featureElements.getJSONObject("properties");
               double mag = properties.getDouble("mag");
               String place = properties.getString("place");
               long time = properties.getLong("time");
               String url = properties.getString("url");
               earthquakes.add(new Earthquake(place,mag,time,url));
           }
       } catch (JSONException e) {
           Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
       }
       return earthquakes;
    }

}