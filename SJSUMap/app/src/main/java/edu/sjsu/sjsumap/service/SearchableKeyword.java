package edu.sjsu.sjsumap.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamlendra Singh Chauhan on 10/28/2016.
 */

public class SearchableKeyword {

    public static List<String> getListOfSearchableKeywords() {
        List<String> listOfKeywords = new ArrayList<>();
        listOfKeywords.add("King");
        listOfKeywords.add("King Library");
        listOfKeywords.add("Engineering");
        listOfKeywords.add("Engineering Building");
        listOfKeywords.add("Yoshihiro");
        listOfKeywords.add("Yoshihiro Uchida");
        listOfKeywords.add("Yoshihiro Uchida Hall");
        listOfKeywords.add("Student");
        listOfKeywords.add("Student Union");
        listOfKeywords.add("BBC");
        listOfKeywords.add("South");
        listOfKeywords.add("South Parking");
        listOfKeywords.add("South Parking Garage");
        return listOfKeywords;
    }
}
