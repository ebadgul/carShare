package com.wit.getaride;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class SharedPreference {
    public static final String PREFS_NAME = "SAVED_DESTINATIONS";
    public static final String DESTINATIONS = "destinations";
    public SharedPreference() {
        super();
    }
public void storeFavorites(Context context, List<Destination> favorites) {
// used for store arrayList in json format
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(DESTINATIONS, jsonFavorites);
        editor.commit();
    }
    public ArrayList<Destination> loadDestinations(Context context) {
// used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List<Destination> destinations;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if (settings.contains(DESTINATIONS)) {
            String jsonFavorites = settings.getString(DESTINATIONS, null);
            Gson gson = new Gson();
            Destination[] dests = gson.fromJson(jsonFavorites, Destination[].class);
            destinations = Arrays.asList(dests);
            destinations = new ArrayList<Destination>(destinations);
        } else
            return null;
        return (ArrayList<Destination>) destinations;
    }
    public void addDestination(Context context, Destination destination) {
        ArrayList<Destination> destinations = loadDestinations(context);
        if (destinations == null)
        	destinations = new ArrayList<Destination>();
        destinations.add(destination);
        storeFavorites(context, destinations);
    }
    public void removeDestination(Context context, int i) {
        ArrayList<Destination> destinations = loadDestinations(context);
        if (destinations != null) {
        	destinations.remove(i);
        	int size = destinations.size();
            storeFavorites(context, destinations);
        }
    }
}
