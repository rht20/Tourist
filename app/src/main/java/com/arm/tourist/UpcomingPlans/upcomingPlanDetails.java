package com.arm.tourist.UpcomingPlans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.tourist.Firebase.FirebaseHelper;
import com.arm.tourist.Models.CustomPlace;
import com.arm.tourist.Models.Guide;
import com.arm.tourist.Models.Hotel;
import com.arm.tourist.Models.TourPlan;
import com.arm.tourist.NewPlan.SelectedPlaceWeather;
import com.arm.tourist.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class upcomingPlanDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "NewPlanActivity";

    FirebaseDatabase database = FirebaseHelper.getFirebaseDatabaseInstance();
    FirebaseUser user = FirebaseHelper.getFirebaseUser();
    FirebaseStorage storage = FirebaseHelper.getFirebaseStorageInstance();

    StorageReference storageRef = storage.getReference();
    private UploadTask uploadTask;

    private TextView destTV;
    private EditText planTitle,stDt,enDt,dest;
    private EditText guideNameET, guideContactNoET, hotelNameET, hotelAddrET, hotelContactNoET, hotelEmailET, hotelWebsiteET;
    private Button  weatherBtn, updateBtn;

    private String unixTimeSt;

    TourPlan myplan;
    CustomPlace mPlace;
    View myView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public upcomingPlanDetails() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_upcoming_plan_details, container, false);

        init();

        Bundle bundle = getArguments();

        myplan = bundle.getParcelable("Object");
        //Toast.makeText(getActivity(), myplan.getTitle()+"  "+myplan.getPlaceName(),Toast.LENGTH_LONG).show();
        showPlan();
        setEditingEnabled(true);

        // Inflate the layout for this fragment
        return myView;
    }

    private void showPlan()
    {
        planTitle.setText(myplan.getTitle());
        stDt.setText(myplan.getStartDate());
        enDt.setText(myplan.getEndDate());
        dest.setText(myplan.getPlaceName());
        guideNameET.setText(myplan.getGuide().getName());
        guideContactNoET.setText(myplan.getGuide().getPhoneNo());
        hotelNameET.setText(myplan.getHotel().getName());
        hotelAddrET.setText(myplan.getHotel().getAddress());
        hotelContactNoET.setText(myplan.getHotel().getContactNo());
        hotelEmailET.setText(myplan.getHotel().getContactEmail());
        hotelWebsiteET.setText(myplan.getHotel().getWebsiteAddress());
    }



    private void init()
    {
        planTitle = myView.findViewById(R.id.titleET);
        stDt = myView.findViewById(R.id.startDateET);
        enDt = myView.findViewById(R.id.endDateET);
        dest = myView.findViewById(R.id.destinationET);
        destTV = myView.findViewById(R.id.destTV);

        guideNameET = myView.findViewById(R.id.guideNameET);
        guideContactNoET = myView.findViewById(R.id.guidePhoneET);
        hotelNameET = myView.findViewById(R.id.hotelNameET);
        hotelAddrET = myView.findViewById(R.id.hotelAddrET);
        hotelContactNoET = myView.findViewById(R.id.hotelPhoneET);
        hotelEmailET = myView.findViewById(R.id.hotelEmailET);
        hotelWebsiteET = myView.findViewById(R.id.hotelWebsiteET);

        weatherBtn = myView.findViewById(R.id.weatherBtn);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherForecast();
            }
        });

        updateBtn = myView.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePlan();
            }
        });

    }

    void weatherForecast()
    {
        Intent intent = new Intent(getActivity(),SelectedPlaceWeather.class);

        CustomPlace customPlace = new CustomPlace();
        customPlace.setName(myplan.getPlaceName());
        customPlace.setLat(myplan.getPlaceLat());
        customPlace.setLongt(myplan.getPlaceLongt());

        Bundle bundle = new Bundle();
        bundle.putParcelable("Place",customPlace);
        intent.putExtras(bundle);
        startActivity(intent);
        //finish();
    }

    private void updatePlan()
    {
       // savePlan();
        if(!stDt.getText().toString().trim().equals(""))
        {
            unixTimeSt = dateToUnix(stDt.getText().toString().trim());
            myplan.setStartDate(stDt.getText().toString());
            myplan.setUnixTimeSt(unixTimeSt);
        }
        if(!enDt.getText().toString().trim().equals(""))
        {
            myplan.setEndDate(enDt.getText().toString());
        }
        if(!planTitle.getText().toString().trim().equals(""))
        {
            myplan.setTitle(planTitle.getText().toString());
        }
        if(!dest.getText().toString().trim().equals(""))
        {/*
            myplan.setPlaceName(mPlace.getName());
            myplan.setPlaceLat(mPlace.getLat());
            myplan.setPlaceLongt(mPlace.getLongt());*/
            myplan.setPlaceName(dest.getText().toString());
        }

        if(!guideNameET.getText().toString().trim().equals("")) myplan.setGuide(new Guide(guideNameET.getText().toString().trim(), guideContactNoET.getText().toString().trim()));
        if(!hotelNameET.getText().toString().trim().equals("") && !hotelAddrET.getText().toString().trim().equals("") ) myplan.setHotel(new Hotel(hotelNameET.getText().toString().trim(),
                hotelAddrET.getText().toString().trim(),
                hotelContactNoET.getText().toString().trim(),
                hotelEmailET.getText().toString().trim(),
                hotelWebsiteET.getText().toString().trim()));

        Toast.makeText(getActivity(), "Plan Updating", Toast.LENGTH_SHORT).show();

        setEditingEnabled(false);

        FirebaseUser mUser = FirebaseHelper.getFirebaseUser();
        // FirebaseDatabase database = FirebaseHelper.getFirebaseDatabaseInstance()
        DatabaseReference userPlanRef = database.getReference("userPlans").child(mUser.getUid()).child(myplan.getPostID());
        userPlanRef.setValue(myplan); //updated

        //////////
        //setEditingEnabled(true);
    }
    private void setEditingEnabled(boolean enabled) {

        planTitle.setEnabled(enabled);
        stDt.setEnabled(enabled);
        enDt.setEnabled(enabled);
        dest.setEnabled(enabled);
        guideNameET.setEnabled(enabled);
        guideContactNoET.setEnabled(enabled);
        hotelNameET.setEnabled(enabled);
        hotelAddrET.setEnabled(enabled);
        hotelContactNoET.setEnabled(enabled);
        hotelWebsiteET.setEnabled(enabled);

        if (enabled) {
            updateBtn.setVisibility(View.VISIBLE);
        } else {
            updateBtn.setVisibility(View.GONE);
        }
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
}
