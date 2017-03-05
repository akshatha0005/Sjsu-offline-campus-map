package edu.sjsu.sjsumap.service;

import android.util.Log;

import java.util.HashMap;

import edu.sjsu.sjsumap.util.GoogleGridPoints;
import edu.sjsu.sjsumap.util.LatLngDistance;
import edu.sjsu.sjsumap.util.XY;

/**
 * Created by Akshatha Anantharamu on 11/1/16.
 */

public class LatLngToXY {
    static int i,j = 0;

    //Topmost corner (library)
    static Double Latitude = 37.335827;
    static Double Longitude = 121.885950;

    static int X = 7;
    static int Y = 630;


    static Double tmpLat, tmpLong;
    static int tmpX,tmpY;

    static HashMap<GoogleGridPoints,XY> mapPoint = new HashMap<>();
     static LatLngDistance distanceObj = new LatLngDistance();

    public LatLngToXY(){
        addToMap();
    }

    public static XY caluculateNearestLatLng(Double lat, Double lng) {
        double nearestLatLngDist = 10000000;
        XY nearestXY = new XY(0,0);
        Log.d("calucuate distance",String.valueOf(mapPoint.size()));

        for (GoogleGridPoints key:mapPoint.keySet()
                ) {
            double tmpDist = distanceObj.distance(key.getLatitude(),lat,key.getLongitude(),lng);
            if(tmpDist < nearestLatLngDist){
                nearestLatLngDist = tmpDist;
                nearestXY = mapPoint.get(key);
            }
        }
        return nearestXY;
    }

    public static void addToMap() {
        for (i = 0; i < 22; i++) {
            if(i!=0){
                Latitude = Double.valueOf(Math.round((Latitude - (0.000211)) * 1000000.0) / 1000000.0);
                Longitude = Double.valueOf(Math.round((Longitude - (0.000156)) * 1000000.0) / 1000000.0);
                Y = Y + 71;
            }
            for (j = 0; j < 22; j++){
                if (j!=0){
                    tmpLat = Double.valueOf(Math.round((tmpLat+(.000147)) * 1000000.0) / 1000000.0);
                    tmpLong =  Double.valueOf(Math.round((tmpLong-(.000307)) * 1000000.0) / 1000000.0);
                    tmpX = tmpX + 87;
                    GoogleGridPoints tmpLatLng = new GoogleGridPoints(tmpLat,tmpLong);
                    XY tmpXY = new XY(tmpX,tmpY);
                    mapPoint.put(tmpLatLng,tmpXY);
                }else{
                    GoogleGridPoints tmpLatLng = new GoogleGridPoints(Latitude,Longitude);
                    XY tmpXY = new XY(X,Y);
                    mapPoint.put(tmpLatLng,tmpXY);
                    tmpLat = Latitude;
                    tmpLong = Longitude;
                    tmpX = X;
                    tmpY = Y;
                }
            }
        }
    }
}
