package edu.sjsu.sjsumap;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.sjsumap.service.LatLong;
import edu.sjsu.sjsumap.service.LocationService;
import edu.sjsu.sjsumap.service.RestHandler;

public class DetailView extends AppCompatActivity {

    ImageView bldgView;
    TextView title;
    TextView address;
    TextView distanceView;
    TextView time;
    ImageButton streetView;
    Double lat;
    Double lng;

    LatLong currentLoc = LocationService.getInstance().getCurrentLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail_view);
        setView();
    }

    private void setView() {
        bldgView = (ImageView) findViewById(R.id.bldg);
        title = (TextView) findViewById(R.id.bldg_name);
        address = (TextView) findViewById(R.id.bldgAddress);
        distanceView = (TextView) findViewById(R.id.distance);
        time = (TextView) findViewById(R.id.time);
        streetView = (ImageButton) findViewById(R.id.streetView);
        String bldg = getIntent().getStringExtra("bldg");
        if (bldg.equals("Library")) {
            setTitle("King Library");
            lat = 37.335912;
            lng = -121.885812;
            bldgView.setImageResource(R.drawable.library);
            title.setText(R.string.library);
            address.setText(R.string.library_addr);
            StreetView street = new StreetView();
            street.setLatLng(lat, lng);
            getDistanceInfo(currentLoc.getLatitude(), currentLoc.getLongitude(), "150 E San Fernando St, San Jose, CA 95112");
            streetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailView.this, StreetView.class);
                    startActivity(intent);
                }
            });
        } else if (bldg.equals("EnggBldg")) {
            setTitle("Engineering Building");
            lat = 37.337860;
            lng = -121.881745;
            StreetView street = new StreetView();
            street.setLatLng(lat, lng);
            bldgView.setImageResource(R.drawable.engg_bldg);
            title.setText(R.string.enggbldg);
            address.setText(R.string.enggbldg_addr);
            getDistanceInfo(currentLoc.getLatitude(), currentLoc.getLongitude(), "1 Washington Square, San Jose, CA 95112");
            streetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailView.this, StreetView.class);
                    startActivity(intent);
                }
            });
        } else if (bldg.equals("StudentUnion")) {
            setTitle("Student Union");
            lat = 37.337497;
            lng = -121.882905;
            bldgView.setImageResource(R.drawable.studentunion);
            title.setText(R.string.studentunion);
            address.setText(R.string.studentunion_addr);
            StreetView street = new StreetView();
            street.setLatLng(lat, lng);
            Log.d("Current Location ", "Lati" + currentLoc.getLatitude() + " Long " + currentLoc.getLongitude());
            getDistanceInfo(currentLoc.getLatitude(), currentLoc.getLongitude(), "San José State University, 211 S 9th St, San Jose, CA 95112");
            streetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailView.this, StreetView.class);
                    startActivity(intent);
                }
            });
        } else if (bldg.equals("BBC")) {
            setTitle("Boccardo Buisness Centre");
            lat = 37.336922;
            lng = -121.878274;
            bldgView.setImageResource(R.drawable.bbc);
            title.setText(R.string.bbc);
            address.setText(R.string.bbc_addr);
            StreetView street = new StreetView();
            street.setLatLng(lat, lng);
            getDistanceInfo(currentLoc.getLatitude(), currentLoc.getLongitude(), "220 S 10th St, San Jose, CA 95112");
            streetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailView.this, StreetView.class);
                    startActivity(intent);
                }
            });
        } else if (bldg.equals("YUG")) {
            setTitle("Yoshihiro Uchida Hall");
            lat = 37.333922;
            lng = -121.884274;
            bldgView.setImageResource(R.drawable.uchidahall);
            title.setText(R.string.uchida);
            address.setText(R.string.uchida_addr);
            StreetView street = new StreetView();
            street.setLatLng(lat, lng);
            getDistanceInfo(currentLoc.getLatitude(), currentLoc.getLongitude(), "Yoshihiro Uchida Hall, San Jose, CA 95112");
            streetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailView.this, StreetView.class);
                    startActivity(intent);
                }
            });
        } else if (bldg.equals("SouthGrg")) {
            setTitle("South Garage");
            lat = 37.333495;
            lng = -121.879965;
            bldgView.setImageResource(R.drawable.southgarage);
            title.setText(R.string.southgarage);
            address.setText(R.string.southgarage_addr);
            StreetView street = new StreetView();
            street.setLatLng(lat, lng);
            getDistanceInfo(currentLoc.getLatitude(), currentLoc.getLongitude(), "San Jose State University South Garage, 330 South 7th Street, San Jose, CA 95112");
            streetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailView.this, StreetView.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void getDistanceInfo(final double lat1, final double lng1, final String destinationAddress) {

        RestHandler handler = new RestHandler();
        handler.setValues(lat1,lng1,destinationAddress,distanceView,time);
        handler.execute();
        Log.d("Async","async task started");
    }

}
