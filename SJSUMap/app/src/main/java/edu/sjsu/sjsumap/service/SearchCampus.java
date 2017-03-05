package edu.sjsu.sjsumap.service;

import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import edu.sjsu.sjsumap.R;

/**
 * Created by Kamlendra Singh Chauhan on 10/28/2016.
 */

public class SearchCampus {

    public static Map<String, BUILDING_COLOR> mapOfBuildingAndColor = new HashMap<>();

    static {
        mapOfBuildingAndColor.put("king", BUILDING_COLOR.RED);
        mapOfBuildingAndColor.put("king library", BUILDING_COLOR.RED);
        mapOfBuildingAndColor.put("engineering", BUILDING_COLOR.BLUE);
        mapOfBuildingAndColor.put("engineering building", BUILDING_COLOR.BLUE);
        mapOfBuildingAndColor.put("yoshihiro", BUILDING_COLOR.GREEN);
        mapOfBuildingAndColor.put("yoshihiro uchida", BUILDING_COLOR.GREEN);
        mapOfBuildingAndColor.put("yoshihiro uchida hall", BUILDING_COLOR.GREEN);
        mapOfBuildingAndColor.put("student", BUILDING_COLOR.MANGENTA);
        mapOfBuildingAndColor.put("student union", BUILDING_COLOR.MANGENTA);
        mapOfBuildingAndColor.put("bbc", BUILDING_COLOR.CYAN);
        mapOfBuildingAndColor.put("south", BUILDING_COLOR.YELLO);
        mapOfBuildingAndColor.put("south parking", BUILDING_COLOR.YELLO);
        mapOfBuildingAndColor.put("south parking garage", BUILDING_COLOR.YELLO);
        mapOfBuildingAndColor.put("fullcampus", BUILDING_COLOR.WHITE);
    }

    public static BUILDING_COLOR getBuildingColor(String keyword) {
        return mapOfBuildingAndColor.get(keyword);
    }

    public static void changeImage(ImageView imageView, BUILDING_COLOR buildingColor) {
        switch (buildingColor) {
            case RED:
                imageView.setImageResource(R.drawable.campusmap_king);
                break;
            case BLUE:
                imageView.setImageResource(R.drawable.campusmap_eng);
                break;
            case GREEN:
                imageView.setImageResource(R.drawable.campusmap_yuh);
                break;
            case MANGENTA:
                imageView.setImageResource(R.drawable.campusmap_su);
                break;
            case CYAN:
                imageView.setImageResource(R.drawable.campusmap_bbc);
                break;
            case YELLO:
                imageView.setImageResource(R.drawable.campusmap_spg);
                break;
            default:
                imageView.setImageResource(R.drawable.campusmap);
        }
    }

    enum BUILDING_COLOR {
        RED, BLUE, GREEN, MANGENTA, CYAN, YELLO, WHITE
    }
}
