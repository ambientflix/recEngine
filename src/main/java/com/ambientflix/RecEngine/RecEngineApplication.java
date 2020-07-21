package com.ambientflix.RecEngine;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import com.ambientflix.RecEngine.MovieResult;
import com.ambientflix.RecEngine.MovieResults;

@SpringBootApplication
public class RecEngineApplication {
	static Hashtable<Integer, List<MovieResult>> recList = new Hashtable<>();
	static Hashtable<String, Integer> weightTable;
	
	public static void weightTableInit() {
		weightTable = new Hashtable<String, Integer>();
		weightTable.put("Name", 1);
		weightTable.put("Weather", 1);
		weightTable.put("Spotify", 1);

	}
	
	public void updateRecList(MovieResults results) {
		Collection<List<MovieResult>> values = recList.values();
		
		for (MovieResult movie: results.getResults()) {
			for (List<MovieResult> movieList: values) {
				int index = movieList.indexOf(movie);
				if (index != -1) {
					movieList.get(index).updateScore(movie.getScore());
				} else {
					if (!recList.contains(movie.getScore())) {
//						recList.put(movie.getScore(), new List<MovieResult>(movie));
					}
				}
			}
		}
		if (!recList.contains(results.getScore())){
			recList.put(results.getScore(), results.getResults());
		} else {
			recList.get(results.getScore()).addAll(results.getResults());
		}
		
	}
	public static MovieResults getMovieResults(String type, String keyword) {
		RestTemplate restTemplate = new RestTemplate();
		//HttpEntity<String> request = new HttpEntity<>("");
		String query = keyword.replaceAll("\\s+", "+");
		MovieResults result = restTemplate.getForObject("https://api.themoviedb.org/3/search/movie?api_key=9950b15cd666adb852b2ea54472b7c38&query="+query, MovieResults.class);		
		
		result.setScore(weightTable.get(type));

		System.out.println("Total: " + result.getTotal_results());

		for(MovieResult movieResult : result.getResults()) {
			System.out.println(movieResult.getTitle());
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RecEngineApplication.class, args);
		
	}

}
