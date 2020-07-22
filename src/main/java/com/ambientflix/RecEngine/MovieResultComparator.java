package com.ambientflix.RecEngine;
import java.util.Comparator;

public class MovieResultComparator implements Comparator<MovieResult> {
	@Override
	public int compare(MovieResult movie1, MovieResult movie2) {
		return movie2.getScore() - movie1.getScore();
	}
}
