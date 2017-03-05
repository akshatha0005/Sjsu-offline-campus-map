package edu.sjsu.sjsumap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.sjsumap.service.LatLngToXY;
import edu.sjsu.sjsumap.service.LatLong;
import edu.sjsu.sjsumap.service.LocationService;
import edu.sjsu.sjsumap.service.SearchCampus;
import edu.sjsu.sjsumap.service.SearchableKeyword;
import edu.sjsu.sjsumap.util.CampusBoundryPoints;
import edu.sjsu.sjsumap.util.ColorTool;
import edu.sjsu.sjsumap.util.GoogleGridPoints;
import edu.sjsu.sjsumap.util.XY;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SearchView.OnQueryTextListener, LocationListener {

    String bldg;
    ImageView imageView;
    SearchView searchView;
    final private int ACCESS_COARSE_LOCATION = 1;
    Double lat;
    Double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCurrentLocation();
        imageView = (ImageView) findViewById(R.id.sjsumap);
        Button gps = (Button) findViewById(R.id.gps);


        if (imageView != null) {
            imageView.setOnTouchListener(this);
        }
        Drawable drawable = imageView.getDrawable();
        final LatLngToXY latlngtoXY = new LatLngToXY();
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LocationService.getInstance().getCurrentLocation() != null){
                Double lat = LocationService.getInstance().getCurrentLocation().getLatitude();
                Double lng = LocationService.getInstance().getCurrentLocation().getLongitude() * -1;
                if(contains(new GoogleGridPoints(lat,lng))) {
                    XY mapXY = latlngtoXY.caluculateNearestLatLng(lat, lng);
                    Drawable drawable = imageView.getDrawable();
                    Bitmap currentmap = drawableToBitmap(drawable);
                    Bitmap mutableBitmap = currentmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap);
                    imageView.setImageBitmap(mutableBitmap);
                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.RED);
                    Paint p = new Paint();
                    p.setStyle(Paint.Style.FILL_AND_STROKE);
                    p.setShader(new SweepGradient(0, 0, Color.WHITE, Color.RED));
                    canvas.drawCircle(mapXY.getX(), mapXY.getY(), 60, p);
                    canvas.drawCircle(mapXY.getX(), mapXY.getY(), 15, paint);
                }else {
                    showOffCampus();
                }}else {
                    getCurrentLocation();
                    showNetworkError();
                }

            }
        });



        toast("Touch the screen to discover where the regions are.");
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int action = event.getAction();

        final int evX = (int) event.getX();
        final int evY = (int) event.getY();

        ImageView imageView = (ImageView) v.findViewById(R.id.sjsumap);
        if (imageView == null) return false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                    int touchColor = getHotspotColor(R.id.sjsucolored_map, evX, evY);
                    ColorTool ct = new ColorTool();
                    int tolerance = 25;
                    if(LocationService.getInstance().getCurrentLocation() != null) {

                        if (ct.closeMatch(Color.RED, touchColor, tolerance)) {
                            bldg = "Library";
                            Intent intent = new Intent(MainActivity.this, DetailView.class);
                            intent.putExtra("bldg", bldg);
                            startActivity(intent);
                        } else if (ct.closeMatch(Color.GREEN, touchColor, tolerance)) {
                            bldg = "YUG";
                            Intent intent = new Intent(MainActivity.this, DetailView.class);
                            intent.putExtra("bldg", bldg);
                            startActivity(intent);
                        } else if (ct.closeMatch(Color.YELLOW, touchColor, tolerance)) {
                            bldg = "SouthGrg";
                            Intent intent = new Intent(MainActivity.this, DetailView.class);
                            intent.putExtra("bldg", bldg);
                            startActivity(intent);
                        } else if (ct.closeMatch(Color.MAGENTA, touchColor, tolerance)) {
                            bldg = "StudentUnion";
                            Intent intent = new Intent(MainActivity.this, DetailView.class);
                            intent.putExtra("bldg", bldg);
                            startActivity(intent);
                        } else if (ct.closeMatch(Color.BLUE, touchColor, tolerance)) {
                            bldg = "EnggBldg";
                            Intent intent = new Intent(MainActivity.this, DetailView.class);
                            intent.putExtra("bldg", bldg);
                            startActivity(intent);
                        } else if (ct.closeMatch(Color.CYAN, touchColor, tolerance)) {
                            bldg = "BBC";
                            Intent intent = new Intent(MainActivity.this, DetailView.class);
                            intent.putExtra("bldg", bldg);
                            startActivity(intent);
                        }

                        break;
                    }else {
                        getCurrentLocation();
                        showNetworkError();
                    }
        }
        return false;
    }

    public int getHotspotColor(int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById(hotspotId);
        if (img == null) {
            Log.d("ImageAreasActivity", "Hot spot image not found");
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        ImageView imageView = (ImageView) findViewById(R.id.sjsumap);

        List<String> listOfKeywords = SearchableKeyword.getListOfSearchableKeywords();
        for (String keyword : listOfKeywords) {
            if (keyword.equalsIgnoreCase(query)) {
                Log.d("MainActivity", "Searched Keyword " + query);
                searchView.clearFocus();
                SearchCampus.changeImage(imageView, SearchCampus.getBuildingColor(query.toLowerCase()));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ImageView imageView = (ImageView) findViewById(R.id.sjsumap);
        if (newText != null && "".equals(newText)) {
            searchView.clearFocus();
            SearchCampus.changeImage(imageView, SearchCampus.getBuildingColor("fullcampus"));
            return true;
        }

        return false;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        try {

            if (!isGPSOn && !isNetworkOn) {
                showTapInfo("Please enable GPS/network");
                requestPermissions();
                showWhenGPSOff();
            }

            if (isGPSOn) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    LocationService.getInstance().setCurrentLocation(new LatLong(
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()
                    ));
                    lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                    lng = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                }

            }

            if (isNetworkOn) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                    LocationService.getInstance().setCurrentLocation(new LatLong(
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(),
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude()
                    ));
                }
            }

            if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                LocationService.getInstance().setCurrentLocation(new LatLong(
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()
                ));
            }

        } catch (SecurityException e) {
            e.printStackTrace();
            showTapInfo("Exception while fetching location");
            requestPermissions();
        }
    }
    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_COARSE_LOCATION);
        }
    }

    private void showTapInfo(String message) {
        final AlertDialog.Builder addressBuilder = new AlertDialog.Builder(MainActivity.this);
        addressBuilder.setMessage(message);
        final AlertDialog mapAddress = addressBuilder.create();
        mapAddress.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapAddress.dismiss();
            }
        }, 2000);
    }

    private void showWhenGPSOff(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("GPS is disabled. Enable for GPS? ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@SuppressWarnings("unused") DialogInterface dialog, @SuppressWarnings("unused") int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showOffCampus(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Sorry you are not inside the SJSU Campus")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@SuppressWarnings("unused") DialogInterface dialog, @SuppressWarnings("unused") int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean contains(GoogleGridPoints test) {
        int i;
        int j;
        boolean result = false;
        ArrayList<GoogleGridPoints> points = CampusBoundryPoints.getPoints();

        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).getLongitude() > test.getLongitude()) != (points.get(j).getLongitude() > test.getLongitude()) &&
                    (test.getLatitude() < (points.get(j).getLatitude() - points.get(i).getLatitude()) *
                                            (test.getLongitude() - points.get(i).getLongitude()) /
                                            (points.get(j).getLongitude()-points.get(i).getLongitude()) + points.get(i).getLatitude())) {
                result = !result;
            }
        }
        return result;
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationService.getInstance().setCurrentLocation(new LatLong(location.getLatitude(), location.getLongitude()));
        Log.d("Current latitude:",String.valueOf(location.getLatitude()));
        Log.d("Current longitude:",String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void showNetworkError(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Network is getting enabled! Please wait!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@SuppressWarnings("unused") DialogInterface dialog, @SuppressWarnings("unused") int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
