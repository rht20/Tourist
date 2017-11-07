package com.arm.tourist.NewPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arm.tourist.Firebase.FirebaseHelper;
import com.arm.tourist.Models.Guide;
import com.arm.tourist.Models.Hotel;
import com.arm.tourist.Models.TourPlan;
import com.arm.tourist.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
Add option to see guide list from database
to see hotel list from database
*/
public class GuideHotelActivity extends AppCompatActivity {

    private  static  final  String TAG = "GuideAndHotelActivity";

    TourPlan myplan;

    private Button contBtn;
    private EditText guideNameET, guideContactNoET, hotelNameET, hotelAddrET, hotelContactNoET, hotelEmailET, hotelWebsiteET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_hotel);

        guideNameET = findViewById(R.id.guideNameET);
        guideContactNoET = findViewById(R.id.guidePhoneET);
        hotelNameET = findViewById(R.id.hotelNameET);
        hotelAddrET = findViewById(R.id.hotelAddrET);
        hotelContactNoET = findViewById(R.id.hotelPhoneET);
        hotelEmailET = findViewById(R.id.hotelEmailET);
        hotelWebsiteET = findViewById(R.id.hotelWebsiteET);

        handleExtras(getIntent());

        contBtn = findViewById(R.id.contBtn);
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savePlan();
                nextPage();
            }
        });

    }

    private void handleExtras(Intent intent)
    {
        //Bundle bundle = intent.getBundleExtra("Object");
        Bundle bundle = intent.getExtras();
        myplan = bundle.getParcelable("NewPlan");

        Log.e(TAG, myplan.getTitle() + " "+myplan.getPlaceName()+" "+myplan.getPlaceLat()+" "+myplan.getPlaceLongt());
    }

    private void savePlan()
    {
        myplan.setGuide(new Guide(guideNameET.getText().toString().trim(), guideContactNoET.getText().toString().trim()));
        myplan.setHotel(new Hotel(hotelNameET.getText().toString().trim(),
                hotelAddrET.getText().toString().trim(),
                hotelContactNoET.getText().toString().trim(),
                hotelEmailET.getText().toString().trim(),
                hotelWebsiteET.getText().toString().trim()));

        Log.e(TAG, myplan.toString());

        ///saving to firebase database

        // shduhu guide ar hotel save hoy. ager info gula save hoy na   :(
        FirebaseUser mUser = FirebaseHelper.getFirebaseUser();
        FirebaseDatabase database = FirebaseHelper.getFirebaseDatabaseInstance();
        DatabaseReference userPlanRef = database.getReference("userPlans").child(mUser.getUid());
        String pushKey = userPlanRef.push().getKey();
        userPlanRef.child(pushKey).setValue(myplan);

    }

    private void nextPage()
    {

    }
}
