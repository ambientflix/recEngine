package com.ambientflix.RecEngine;
import java.util.Hashtable;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;
import java.util.ArrayList;

public class MovieDetail {
    
//   private String title;
//   private JSONObject keywords;
//   private JSONArray mainKeywords;
//   private List<Genre> genres;
//   private JSONService jsonService = new JSONService();
   
    private List<String> listOfKeywords;
	private JSONService jsonService;
	private String movieID;
	private String title;
	private List<String> listOfGenres;
	
	public MovieDetail(String movieID) throws IOException, JSONException {
		this.movieID = movieID;
		jsonService = new JSONService();
		listOfKeywords = new ArrayList<>();
		listOfGenres = new ArrayList<>();
		generator();
		
	}
	
	private void generator() throws IOException, JSONException {
		JSONObject jo = jsonService.readJsonFromUrl("https://api.themoviedb.org/3/movie/"+movieID+"?api_key=64a51e683b1854ed324abcdc797de47a&language=en-US&append_to_response=keywords");
		
		//get movie title
		title = (String) jo.getString("title");
		
		//get keywords in movie detail
		JSONObject keywordsTemp = (JSONObject) jo.get("keywords");
		JSONArray jsonArray = keywordsTemp.getJSONArray("keywords");
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			listOfKeywords.add(0,(String) jsonObj.get("name"));
		}
	
		
		//get genres in movie detail
		jsonArray = jo.getJSONArray("genres");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			listOfGenres.add(0,jsonObj.getString("name").toLowerCase());
		}
	}
	
	public List<String> getListOfMovieKeywords() {
		return listOfKeywords;
	}

   public String getTitle(){
       return title;
   }
   public void setTitle(String title){
       this.title = title;
   }

   public List<String> getListOfGenres() {
	   return listOfGenres;
   }
   
   
    
}