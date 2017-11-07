package com.arm.tourist.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arm.tourist.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.arm.tourist.R.id.restaurant;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String GOOGLE_API_KEY = "@string/google_map_api" ;

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentMarker;

    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlace();
            }
        });

        Button hospitalButton = (Button)findViewById(R.id.hospital);
        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchHospital();
            }
        });

        Button restaurantButton = (Button)findViewById(restaurant);
        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRestaurant();
            }
        });

        Button PoliceStationButton = (Button)findViewById(R.id.police_station);
        PoliceStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoliceStation();
            }
        });

        Button goButton = (Button)findViewById(R.id.Go);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MapsActivity2.class);
                startActivity(intent);
            }
        });
    }



    //this method is for permission request handle response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //Permission granted
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                    else {
                        // Permission denied
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (checkLocationPermission()) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    protected boolean checkLocationPermission() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (checkLocationPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;

        if (currentMarker != null) currentMarker.remove();

        Log.d("lat = ", "" + latitude);

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        //String titleStr = getAddress(latlng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        //markerOptions.title(titleStr);

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    private String getAddress(LatLng latLng) {

        Geocoder geocoder = new Geocoder(this); //Geocoder object to turn a latitude and longitude coordinate into an address and vice versa.
        String addressText = "";
        List<Address> addresses = null;
        Address address = null;
        try
        {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty())
            {
                address = addresses.get(0);
                for (int i=0; i<address.getMaxAddressLineIndex(); i++)
                {
                    addressText += (i == 0)?address.getAddressLine(i):("\n" + address.getAddressLine(i));
                }
            }
        }
        catch (IOException e) {}

        return addressText;
    }

    public void searchPlace() {
        EditText editText = findViewById(R.id.search_text);
        String locationName = editText.getText().toString();

        List<Address> addressList = null;
        MarkerOptions markerOptions = new MarkerOptions();

        if (!locationName.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(locationName, 5);
            } catch (Exception e) {
            }

            if (addressList != null) {
                mMap.clear();

                for (int i = 0; i < addressList.size(); i++) {
                    Address address = addressList.get(i);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    /*String titleStr = getAddress(latLng);
                    markerOptions.title(titleStr);
                    */
                    markerOptions.title(locationName);
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                }
            }
            else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
        Nearby Places List = https://developers.google.com/places/supported_types
    */
    public void searchHospital()
    {
        Object dataTransfer[] = new Object[2];
        GetNearByPlacesData getNearbyPlacesData = new GetNearByPlacesData();

        mMap.clear();
        String hospital = "hospital";
        String url = getUrl(latitude, longitude, hospital);
        System.out.println(url);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
    }

    public void searchRestaurant()
    {
        Object dataTransfer[] = new Object[2];
        GetNearByPlacesData getNearbyPlacesData = new GetNearByPlacesData();

        mMap.clear();
        String resturant = "restaurant";
        String  url = getUrl(latitude, longitude, resturant);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
    }

    public void searchPoliceStation()
    {
        Object dataTransfer[] = new Object[2];
        GetNearByPlacesData getNearbyPlacesData = new GetNearByPlacesData();

        mMap.clear();
        String policeStation = "police";
        String url = getUrl(latitude, longitude, policeStation);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
    }

    private String getUrl(double latitude,double longitude,String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCAvPWWFAXG9zD6MdYoH5sJTVbtGlMdVNQ");

         return googlePlaceUrl.toString();
    }
}
