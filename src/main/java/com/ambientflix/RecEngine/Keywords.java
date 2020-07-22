package com.ambientflix.RecEngine;


import java.util.ArrayList;
import java.util.List;

public class Keywords {
    //String arraylist
    private List<String> keywords = new ArrayList<String>();
    private int weight;

    public Keywords() {
        weight = 1;

    }

    public List<String> getkeywords() {
        return keywords;
    }

    public void setKeywords(List<String> value) {
        this.keywords = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int value) {
        weight = value;
    }

    public void addWords(String words) {
        keywords.add(words);
    }

    public void printWords() {
        for (String name : keywords) {
            System.out.println(name);
        }
    }

}