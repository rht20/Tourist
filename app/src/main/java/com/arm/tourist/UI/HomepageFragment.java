package com.arm.tourist.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.tourist.CurrentTrip.OntripFragment;
import com.arm.tourist.Models.TourPlan;
import com.arm.tourist.NewPlan.NewPlanActivity;
import com.arm.tourist.NewsFeed.ExplorePlanActivity;
import com.arm.tourist.R;
import com.arm.tourist.UpcomingPlans.upcomingToursFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.util.Date;

import static android.content.Context.LOCATION_SERVICE;

public class HomepageFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "HomePage";
    private static final String OpenWeatherAPIKey = "e6422ef211669ef1c69fb6cf42119749";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView temperature, skySts, locationTV, sunrise, sunset, currentTour;

    private Location curLocation;

    private GoogleApiClient mGoogleApiClient;

    LocationRequest mLocationRequest;

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 100 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 30; // 30 minutes

    protected LocationManager locationManager;

    TourPlan toAdd;

    View myView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.home_page_layout2, container, false);

        final ImageView createTour = myView.findViewById(R.id.add_trip);
        final ImageView explore = myView.findViewById(R.id.explore_trip);
        final ImageView upcoming = myView.findViewById(R.id.btnUpcomingTrips);

        currentTour = myView.findViewById(R.id.homePageTVcurrent);

        currentTour.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                OntripFragment fragment = new OntripFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment,"upcoming").addToBackStack("")
                        .commit();
            }
        });


        explore.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExplorePlanActivity.class);
                startActivity(intent);
            }
        });

        createTour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewPlanActivity.class);
                startActivity(intent);
            }
        });

        upcoming.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                upcomingToursFragment fragment = new upcomingToursFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment,"upcoming").addToBackStack("")
                        .commit();
            }
        });

        temperature =  myView.findViewById(R.id.tempTV);
        skySts =  myView.findViewById(R.id.skyStatusTV);
        locationTV =  myView.findViewById(R.id.curLocTV);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getLocation();

        setCurrentTrip();



        if (getServicesAvailable()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        OpenWeatherMapHelper openWeatherMapHelper = new OpenWeatherMapHelper();
        openWeatherMapHelper.setApiKey(OpenWeatherAPIKey);
        openWeatherMapHelper.setUnits(Units.METRIC);

        if(curLocation == null)
        {

            openWeatherMapHelper.getCurrentWeatherByCityName("Dhaka", new OpenWeatherMapHelper.CurrentWeatherCallback(){
                @Override
                public void onSuccess(CurrentWeather currentWeather)
                {
                    temperature.setText(""+currentWeather.getMain().getTemp()+" °C");
                    skySts.setText(""+currentWeather.getWeatherArray().get(0).getMain());
                    locationTV.setText(""+currentWeather.getName() + ", " + currentWeather.getSys().getCountry());


                    Log.v(TAG,
                            "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLat() +"\n"
                                    +"Weather Description: " + currentWeather.getWeatherArray().get(0).getDescription() + "\n"
                                    +"Max Temperature: " + currentWeather.getMain().getTempMax()+"\n"
                                    +"Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                                    +"City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                    );
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.v(TAG, "Failed to fetch weather info");
                }
            });
        }

        else
        {
            openWeatherMapHelper.getCurrentWeatherByGeoCoordinates(curLocation.getLatitude(), curLocation.getLongitude(), new OpenWeatherMapHelper.CurrentWeatherCallback() {
                @Override
                public void onSuccess(CurrentWeather currentWeather) {

                    temperature.setText(""+currentWeather.getMain().getTemp()+" °C");
                    skySts.setText(""+currentWeather.getWeatherArray().get(0).getMain());
                    locationTV.setText(""+currentWeather.getName() + ", " + currentWeather.getSys().getCountry());

                    Log.i(TAG, ""+unixTimeStampToDateTime(currentWeather.getSys().getSunrise()));

                    Log.e(TAG,
                            "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLat() +"\n"
                                    +"Weather Description: " + currentWeather.getWeatherArray().get(0).getDescription() + "\n"
                                    +"Max Temperature: " + currentWeather.getMain().getTempMax()+"\n"
                                    +"Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                                    +"City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                    );
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.v(TAG, throwable.getMessage());
                }
            });
        }

        return  myView;
        //   return super.onCreateView(inflater, container, savedInstanceState);
    }


    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }


    public void getLocation() {
        curLocation = null;
        android.location.LocationListener listener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {}

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        double latitude, longitude;
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Log.e(TAG, "No GPS, No Network!");

        } else {


            if (isNetworkEnable) {
                curLocation = null;
                if (ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
                if (locationManager!=null){

                    curLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (curLocation!=null){

                        Log.e("latitude",curLocation.getLatitude()+"");
                        Log.e("longitude",curLocation.getLongitude()+"");

                        latitude = curLocation.getLatitude();
                        longitude = curLocation.getLongitude();

                    }
                }

            }


            else if (isGPSEnable) {
                curLocation = null;
                if (ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,listener);
                if (locationManager!=null){

                    curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (curLocation!=null){
                        latitude = curLocation.getLatitude();
                        longitude = curLocation.getLongitude();
                    }
                }
            }

        }
    }

    public static String unixTimeStampToDateTime(double unixTime)
    {
        Date date = new Date((long) unixTime*1000); // *1000 is to convert seconds to milliseconds
        DateFormat dateFormat = new DateFormat();
        dateFormat.format("MM dd, yyyy hh:mma",date);
        return dateFormat.toString();
    }


    @Override
    public void onLocationChanged(final Location location) {

        Location mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        getLocation();


    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public boolean getServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getContext());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {

            Dialog dialog = api.getErrorDialog(getActivity(), isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "Cannot Connect To Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void setCurrentTrip()
    {
        final String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("user ", uID);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("userPlans").child(uID);

      //  Toast.makeText(getActivity(), uID, Toast.LENGTH_SHORT);

        toAdd = new TourPlan();

        toAdd.setTitle("");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot tourSnapshot : dataSnapshot.getChildren()) {

                    TourPlan tmp = tourSnapshot.getValue(TourPlan.class);

                    Long currentTime = System.currentTimeMillis()/ 1000L;

                  //  Log.e("String: ", tmp.getUnixTimeSt());

                    Long planStart = Long.parseLong(tmp.getUnixTimeSt());

                    Long planEnd = Long.parseLong(tmp.getUnixTimeEn());

                    if(currentTime >= planStart && currentTime <= planEnd)
                    {
                        if(tmp!= null)
                        {


                    //    Log.e("Add korsi", tmp.getTitle());

                        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                                .child("currentTrip")
                                .child(uID);
                      //  mReference.setValue(tmp);
                        return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onClick(View view) {

    }
}