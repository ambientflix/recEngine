package com.ambientflix.RecEngine;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.Scanner;


@SpringBootApplication
public class RecEngineApplication {

    private static ResourceLoader resourceLoader;
    private static List<MovieResult> recList;


    /**
     * Check to see if any of the new movie results appear in recList.
     * If the movie is already in recList, update the score of that movie.
     * Else, add that movie to the list
     * @param results - new list of MovieResult by querying a new keyword
     */
	public static void updateRecList(MovieResults results) {
//		Collection<List<MovieResult>> values = recList.values();
//		
//		for (MovieResult movie: results.getResults()) {
//			for (List<MovieResult> movieList: values) {
//				int index = movieList.indexOf(movie);
//				if (index != -1) {
//					movieList.get(index).updateScore(movie.getScore());
//				} else {
//					if (!recList.contains(movie.getScore())) {
////						recList.put(movie.getScore(), new List<MovieResult>(movie));
//					}
//				}
//			}
//		}
//		if (!recList.contains(results.getScore())){
//			recList.put(results.getScore(), results.getResults());
//		} else {
//			recList.get(results.getScore()).addAll(results.getResults());
//		}
		
		for (MovieResult movieResult: results.getResults()) {
			boolean hasSeen = false;
			for (MovieResult movie: recList) {
				if (movie.getTitle().equals(movieResult.getTitle())) {
					movie.updateScore(movieResult.getScore());
					hasSeen = true;
					break;
				} 
			}
			
			if (!hasSeen) {
				recList.add(movieResult);
			}
		}
		
	}
	
	/**
	 * get a list of MovieResults by querying all the keywords in the values list.
	 * update the recList based on the MovieResults acquired
	 * @param keywords
	 */
	public static void getMovieResults(Keywords keywords) {
		RestTemplate restTemplate = new RestTemplate();
		for (String keyword: keywords.getkeywords()) {
			String query = keyword.replaceAll("\\s+", "+");
			MovieResults results = restTemplate.getForObject("https://api.themoviedb.org/3/search/movie?api_key=9950b15cd666adb852b2ea54472b7c38&query="+query, MovieResults.class);		
			
			results.setScore(keywords.getWeight());

			updateRecList(results);			
		}
	}
	
	
	/**
	 * 
	 * @param resourceLoader
	 */
	public RecEngineApplication(ResourceLoader resourceLoader) {
        RecEngineApplication.resourceLoader = resourceLoader;
    }

	/**
	 * get input files
	 * @return
	 * @throws IOException
	 */
    public static File getFile() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:static/Twitter.txt");
        InputStream dbAsStream = resource.getInputStream();
        return resource.getFile();
    }

    /**
     * grabs keywords from txt file and creates Word object.
     * @param inputFile
     * @param fileScan
     * @return
     */
    public static Keywords getKeywords(File inputFile, Scanner fileScan) {
        Keywords keywords = new Keywords();
        while(fileScan.hasNext()){
            keywords.addWords(fileScan.nextLine());
        }
        return keywords;
    }
    
    
    public static void main(String[] args) throws IOException {
		SpringApplication.run(RecEngineApplication.class, args);
		recList = new ArrayList<MovieResult>();
		File wordFile = getFile();
		
		//read from files and store all the keywords into a list called values
        Scanner fileScan = new Scanner(wordFile);
        ArrayList<Keywords> values = new ArrayList<>();
        if (wordFile.exists()) {
            System.out.println("Successfully found data file. ");
           values.add(getKeywords(wordFile, fileScan));
        } else {
            System.out.println("Could not find file. Please make sure repository is correct. ");
        }
        
        //get movieResults of each keyword in the list
        for (Keywords keywords: values) {
        	getMovieResults(keywords);
        }
        
        //sort recList and print out recommendations in order
        Collections.sort(recList, new MovieResultComparator());
        for (MovieResult movie: recList) {
        	System.out.println(movie.getTitle());
        	System.out.println(movie.getScore());

        }
        
        
	}

}
