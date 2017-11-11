package com.arm.tourist.NewPlan;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.tourist.Firebase.FirebaseHelper;
import com.arm.tourist.Models.CustomPlace;
import com.arm.tourist.Models.Guide;
import com.arm.tourist.Models.Hotel;
import com.arm.tourist.Models.TourPlan;
import com.arm.tourist.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewPlanActivity extends AppCompatActivity {

    private static final String TAG = "NewPlanActivity";
    private static final int RESULT_LOAD_IMAGE = 14543;
    int PLACE_PICKER_REQUEST = 9321;

    //FirebaseDatabase database = FirebaseHelper.getFirebaseDatabaseInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseHelper.getFirebaseUser();
    FirebaseStorage storage = FirebaseHelper.getFirebaseStorageInstance();

    StorageReference storageRef = storage.getReference();
    private UploadTask uploadTask;

    private TextView destTV;
    private EditText planTitle,stDt,enDt,dest;
    private EditText guideNameET, guideContactNoET, hotelNameET, hotelAddrET, hotelContactNoET, hotelEmailET, hotelWebsiteET;
    private Button weatherBtn, saveBtn;

    private String unixTimeSt;

    TourPlan myplan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        myplan = new TourPlan();

        planTitle = findViewById(R.id.titleET);
        stDt = findViewById(R.id.startDateET);
        enDt = findViewById(R.id.endDateET);
        dest = findViewById(R.id.destinationET);
        destTV = findViewById(R.id.destTV);

        guideNameET = findViewById(R.id.guideNameET);
        guideContactNoET = findViewById(R.id.guidePhoneET);
        hotelNameET = findViewById(R.id.hotelNameET);
        hotelAddrET = findViewById(R.id.hotelAddrET);
        hotelContactNoET = findViewById(R.id.hotelPhoneET);
        hotelEmailET = findViewById(R.id.hotelEmailET);
        hotelWebsiteET = findViewById(R.id.hotelWebsiteET);

        weatherBtn = findViewById(R.id.weatherBtn);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dest.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please select a place", Toast.LENGTH_SHORT).show();
                    return;
                }
                weatherForecast();
            }
        });

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavePlan();
            }
        });


        destTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(NewPlanActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(NewPlanActivity.this, "Place Picker Started ",Toast.LENGTH_SHORT).show();
                Place place = PlacePicker.getPlace(this, data);
                if(place == null)
                {
                    Log.e(TAG,"Place is Null, Setting default to Dhaka");

                    myplan.setPlaceName("");
                    myplan.setPlaceLat(23.777176);
                    myplan.setPlaceLongt(90.399452);
                    return;
                }
           //     Toast.makeText(NewPlanActivity.this, "Place Selected: "+place.getName(),Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Place Selected: "+place.getName().toString()+" "+place.getLatLng().longitude+" "+place.getLatLng().longitude);

                String selectedPlaceName = getAddressName(place.getLatLng().latitude,place.getLatLng().longitude);
                dest.setText(selectedPlaceName);

                myplan.setPlaceName(place.getName().toString());
                myplan.setPlaceLat(place.getLatLng().latitude);
                myplan.setPlaceLongt(place.getLatLng().longitude);
            }
        }
    }

    void weatherForecast()
    {
        Intent intent = new Intent(NewPlanActivity.this,SelectedPlaceWeather.class);
        //send place location;
        CustomPlace mPlace = new CustomPlace();
        mPlace.setName(myplan.getPlaceName());
        mPlace.setLat(myplan.getPlaceLat());
        mPlace.setLongt(myplan.getPlaceLongt());
        Bundle bundle = new Bundle();
        bundle.putParcelable("Place",mPlace);
        intent.putExtras(bundle);


        startActivity(intent);
    }

    void SavePlan()
    {
        if(planTitle.getText().toString().trim().equals(""))
        {
            Toast.makeText(NewPlanActivity.this, "Some fields are blank", Toast.LENGTH_SHORT ).show();
            return;
        }
        if(stDt.getText().toString().trim().equals(""))
        {
            Toast.makeText(NewPlanActivity.this, "Some fields are blank", Toast.LENGTH_SHORT ).show();
            return;
        }

        unixTimeSt = dateToUnix(stDt.getText().toString());

        myplan.setUnixTimeSt(unixTimeSt);
        myplan.setTitle(planTitle.getText().toString());
        myplan.setStartDate(stDt.getText().toString());
        myplan.setEndDate(enDt.getText().toString());
        myplan.setUnixTimeEn(dateToUnix(enDt.getText().toString()));

        if(myplan.getPlaceName()=="") myplan.setPlaceName(dest.getText().toString());

        myplan.setGuide(new Guide(guideNameET.getText().toString().trim(), guideContactNoET.getText().toString().trim()));
        myplan.setHotel(new Hotel(hotelNameET.getText().toString().trim(),
                hotelAddrET.getText().toString().trim(),
                hotelContactNoET.getText().toString().trim(),
                hotelEmailET.getText().toString().trim(),
                hotelWebsiteET.getText().toString().trim()));

        FirebaseUser mUser = FirebaseHelper.getFirebaseUser();
        // FirebaseDatabase database = FirebaseHelper.getFirebaseDatabaseInstance();
        DatabaseReference userPlanRef = database.getReference("userPlans").child(mUser.getUid());
        String pushKey = userPlanRef.push().getKey();
        myplan.setPostID(pushKey);
        userPlanRef.child(pushKey).setValue(myplan);

        Toast.makeText(this, "New Plan Saved"+pushKey, Toast.LENGTH_LONG).show();

        finish();
    }

    public static boolean isValidDate(String dateString) {
        if (dateString == null || dateString.length() != "dd/mm/yyyy".length()) {
            return false;
        }
        //if(dateString.charAt(2)!='/' || dateString.charAt(5)!='/') return  false;

        int date;
        try {
            date = Integer.parseInt(dateString.substring(0,1));
        } catch (NumberFormatException e) {
            return false;
        }

        int year = Integer.parseInt(dateString.substring(6,9));
        int month = Integer.parseInt(dateString.substring(3,4));
        int day = Integer.parseInt(dateString.substring(0,1));

        // leap years calculation not valid before 1581
        boolean yearOk = (year >= 1581) && (year <= 2500);
        boolean monthOk = (month >= 1) && (month <= 12);
        boolean dayOk = (day >= 1) && (day <= daysInMonth(year, month));

        return (yearOk && monthOk && dayOk);
    }

    private static int daysInMonth(int year, int month) {
        int daysInMonth;
        switch (month) {
            case 1: // fall through
            case 3: // fall through
            case 5: // fall through
            case 7: // fall through
            case 8: // fall through
            case 10: // fall through
            case 12:
                daysInMonth = 31;
                break;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }
                break;
            default:
                // returns 30 even for nonexistant months
                daysInMonth = 30;
        }
        return daysInMonth;
    }


    private String dateToUnix(String someDate)  {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(someDate);
            //System.out.println(date.getTime());
            return  ""+date.getTime()/1000;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String getAddressName(double lat, double longt)
    {
        Geocoder geocoder = new Geocoder(NewPlanActivity.this, Locale.getDefault() );

        try
        {
            List<Address> addresses  = geocoder.getFromLocation(lat,longt, 1);
            Address obj = addresses.get(0);
            String addrName = obj.getAddressLine(0);
            return addrName;
        }
        catch (Exception e)
        {

        }
        return  null;
    }
}