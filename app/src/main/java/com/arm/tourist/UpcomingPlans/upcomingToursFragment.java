package com.arm.tourist.UpcomingPlans;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arm.tourist.Models.TourPlan;
import com.arm.tourist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class upcomingToursFragment extends Fragment implements View.OnClickListener {

    private CardView cardView[];
    private TextView tourPlace[];
    private TextView tourDate[];
    private TextView tourTitle[];
    private ArrayList<TourPlan> arrays;
    private View myView;
    private TextView noTours;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.upcoming_tours_layout, container, false);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("userPlans").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        init();
        arrays = new ArrayList<>();

        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot tourSnapshot : dataSnapshot.getChildren()) {
                            TourPlan tourPlan = tourSnapshot.getValue(TourPlan.class);

                            Long currentTime = System.currentTimeMillis()/ 1000L;

                            Long planTime = Long.parseLong(tourPlan.getUnixTimeSt());

                            if(planTime>currentTime)
                            {
                                arrays.add(tourPlan);
                            }

                            Log.e("Tag: ", tourPlan.getTitle() + "+++");

                        }
                        setViews();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

        return myView;

    }

    private void init()
    {
        tourDate = new TextView[4];
        cardView = new CardView[4];
        tourPlace = new TextView[4];
        tourTitle = new TextView[4];

        tourTitle[1] = myView.findViewById(R.id.upcomingTitle1);
        tourTitle[2] = myView.findViewById(R.id.upcomingTitle2);
        tourTitle[3] = myView.findViewById(R.id.upcomingTitle3);
        for(int i =1; i<=3; i++)
        {
            tourTitle[i].setOnClickListener(this);
        }


        tourPlace[1] = myView.findViewById(R.id.upcomingPlace1);
        tourPlace[2] = myView.findViewById(R.id.upcomingPlace2);
        tourPlace[3] = myView.findViewById(R.id.upcomingPlace3);

        tourDate[1] = myView.findViewById(R.id.upcomingDate1);
        tourDate[2] = myView.findViewById(R.id.upcomingDate2);
        tourDate[3] = myView.findViewById(R.id.upcomingDate3);

        cardView[1] = myView.findViewById(R.id.upcomingCard1);
        cardView[2] = myView.findViewById(R.id.upcomingCard2);
        cardView[3] = myView.findViewById(R.id.upcomingCard3);

        noTours = myView.findViewById(R.id.tvNoUpcomingTour);

        noTours.setVisibility(View.VISIBLE);

    }

    private void setViews()
    {
        //Collections.sort(arrays);
        int sz = arrays.size();

        for(int i = 1; i<=sz; i++)
        {
            tourTitle[i].setText(arrays.get(i-1).getTitle());
            tourPlace[i].setText(arrays.get(i-1).getPlaceName());

            String tmp = arrays.get(i-1).getStartDate() + " - " + arrays.get(i-1).getEndDate();

            tourDate[i].setText(tmp);

            cardView[i].setVisibility(View.VISIBLE);

            noTours.setVisibility(View.GONE);

        }

        for(int i = 1; i<=3; i++)
        {
            if(tourDate[i].getText().toString().equals("null")) cardView[i].setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.upcomingTitle1)
        {
            planDetails(0);
        }
        else if(id==R.id.upcomingTitle2)
        {
            planDetails(1);
        }
        else if(id==R.id.upcomingTitle2)
        {
            planDetails(2);
        }
    }

    public void planDetails(int indx)
    {
        TourPlan mPlan = getPlan(indx);
        Bundle bundle = new Bundle();
        upcomingPlanDetails fragment = new upcomingPlanDetails();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        bundle.putParcelable("Object", mPlan);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    private TourPlan getPlan(int indx)
    {
        TourPlan  mPlan = arrays.get(indx);
        return mPlan;
    }
}