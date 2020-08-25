package com.ambientflix.RecEngine;

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
import java.io.File;  
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONException;



@SpringBootApplication
public class RecEngineApplication {

    private static ResourceLoader resourceLoader;
    private static KeywordServiceImpl keywordService = new KeywordServiceImpl();;

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
    public static File getFile(String path) throws IOException {

        Resource resource = resourceLoader.getResource(path);
        InputStream dbAsStream = resource.getInputStream();
        return resource.getFile();
    }

    public static void writeRecListToFile(List<MovieResult> recList) throws IOException {
		File outputFile = new File("recList.txt");
		PrintWriter output = new PrintWriter(outputFile);
		for (MovieResult movie: recList) {
				output.println(movie.getId());

	    }
		
		System.out.println("Complete running");
		
	        
	    output.close();
    }
    
    public static List<Keywords> getKeywordValues(String path) throws IOException {
    	File wordFile = getFile(path);
		
		//read from files and store all the keywords into a list called values
        Scanner fileScan = new Scanner(wordFile);
        List<Keywords> values = new ArrayList<>();
        if (wordFile.exists()) {
            System.out.println("Successfully found data file. ");
            keywordService.readKeywordsFromFile(wordFile, fileScan);
            values = new ArrayList<>(keywordService.getKeywords());
        } else {
            System.out.println("Could not find file. Please make sure repository is correct. ");
        }
        
        return values;
    }
    
    public static void main(String[] args) throws IOException, JSONException {
		SpringApplication.run(RecEngineApplication.class, args);
		RestTemplate restTemplate2 = new RestTemplate();
		
		System.out.println(args[0]);
		//list of recommendations
		List<MovieResult> recList = new ArrayList<MovieResult>();
		
		List<Keywords> values = getKeywordValues(args[0]); //list of all the keywords read from input file
		
		
		//recommendation generator based on the keywords
		RecListGenerator generator = new RecListGenerator(values);
		
		//generate the recommendation list
		recList = generator.generator();
		
		//write the recommendation list to file
		writeRecListToFile(recList);
		
        
        
	}

}
