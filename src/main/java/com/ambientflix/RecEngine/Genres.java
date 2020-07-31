package com.ambientflix.RecEngine;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;
import java.util.Hashtable;


public class Genres {
	private JSONArray genres;
	private Hashtable<String, Integer> genreLookupTable = new Hashtable<>();
	private JSONService jsonService = new JSONService();
	public Genres() throws IOException, JSONException {
		generateListOfGenres();
	}
	
	private void generateListOfGenres() throws IOException, JSONException {
		JSONObject jo = jsonService.readJsonFromUrl("https://api.themoviedb.org/3/genre/movie/list?api_key=9950b15cd666adb852b2ea54472b7c38&query&language=en-US");
		genres = jo.getJSONArray("genres");
		
		for (int i = 0; i <genres.length(); i++) {
			JSONObject jsonObj = genres.getJSONObject(i);
			genreLookupTable.put(jsonObj.getString("name").toLowerCase(), (Integer) jsonObj.get("id"));
			
		}
		
	}
	
	public Hashtable<String, Integer> getGenresTable() {
		return genreLookupTable;
	}
	
	public Integer getGenreId(String name) {
		return genreLookupTable.get(name);
	}

	
}
