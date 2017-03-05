package edu.sjsu.sjsumap.util;

import java.util.ArrayList;

/**
 * Created by Akshatha Anantharamu on 11/1/16.
 */

public class CampusBoundryPoints {

    public static ArrayList<GoogleGridPoints> getPoints() {
        ArrayList<GoogleGridPoints> points =  new ArrayList<>();
        points.add(new GoogleGridPoints(37.335821,121.885940));
        points.add(new GoogleGridPoints(37.338867, 121.879713));
        points.add(new GoogleGridPoints(37.334602, 121.876567));
        points.add(new GoogleGridPoints(37.331590, 121.882835));
        return points;
    }
}
