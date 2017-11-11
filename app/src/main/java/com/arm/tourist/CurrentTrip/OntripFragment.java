package com.arm.tourist.CurrentTrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.tourist.Models.OnTripDetails;
import com.arm.tourist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OntripFragment extends Fragment implements View.OnClickListener {

    private TextView[] myTextViews;
    private TextView[] dayIdentifiers;
    private Button addDay, deleteDay, saveData, addAmount, deleteAmount;
    private EditText dayDescription, amountAdder;
    private TextView approxCost;
    private OnTripDetails currentTrip;
    private String userId;
    DatabaseReference ref;
    private TextView onTripTitle, onTripDate, onTripPlace;

    private int totalCost;

    View view;

    int currentDay = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ontrip_layout, container, false);

        init();

        currentTrip = new OnTripDetails();
        currentTrip.settitle("###");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        ref = FirebaseDatabase.getInstance().getReference().child("currentTrip").child(userId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OnTripDetails tmp = dataSnapshot.getValue(OnTripDetails.class);

                if(tmp != null)
                {
                    currentTrip = tmp;
                    Log.e("tour :", currentTrip.gettitle());
                    LoadData();
                }

                else Log.e("tour is null", "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        Log.e("tour :", currentTrip.gettitle());
        LoadData();

        return view;
    }

    private void init()
    {
        myTextViews = new TextView[6];
        dayIdentifiers = new TextView[6];

        addDay = view.findViewById(R.id.btnAddDay);
        deleteDay = view.findViewById(R.id.btnDeleteDay);
        saveData = view.findViewById(R.id.saveData);
        deleteDay.setVisibility(View.GONE);
        addAmount = view.findViewById(R.id.addAmount);
        deleteAmount = view.findViewById(R.id.deleteAmount);
        approxCost = view.findViewById(R.id.costDetails);
        dayDescription = view.findViewById(R.id.etDayDescription);
        amountAdder = view.findViewById(R.id.amountAdder);
        addDay.setOnClickListener(this);
        saveData.setOnClickListener(this);
        deleteDay.setOnClickListener(this);
        addAmount.setOnClickListener(this);
        deleteAmount.setOnClickListener(this);

        dayIdentifiers[1] = view.findViewById(R.id.dayIdentifier1);
        dayIdentifiers[2] = view.findViewById(R.id.dayIdentifier2);
        dayIdentifiers[3] = view.findViewById(R.id.dayIdentifier3);
        dayIdentifiers[4] = view.findViewById(R.id.dayIdentifier4);
        dayIdentifiers[5] = view.findViewById(R.id.dayIdentifier5);

        myTextViews[1] = view.findViewById(R.id.dayDetails1);
        myTextViews[2] = view.findViewById(R.id.dayDetails2);
        myTextViews[3] = view.findViewById(R.id.dayDetails3);
        myTextViews[4] = view.findViewById(R.id.dayDetails4);
        myTextViews[5] = view.findViewById(R.id.dayDetails5);

        onTripDate = view.findViewById(R.id.onTripDate);
        onTripTitle = view.findViewById(R.id.onTripTitle);
        onTripPlace = view.findViewById(R.id.onTripPlace);

        for(int i = 1; i<=5; i++)
        {
            myTextViews[i].setVisibility(View.GONE);
            dayIdentifiers[i].setVisibility(View.GONE);
        }

    }

    private void LoadData(){

        Log.e("load data :", currentTrip.gettitle());

        if(currentTrip.gettitle().equals("###"))
        {
            onTripTitle.setText("No ongoing tour");
            onTripDate.setVisibility(View.INVISIBLE);
            onTripPlace.setText("");

            ScrollView mScrollView = view.findViewById(R.id.currentScroll);
            mScrollView.setVisibility(View.GONE);

        }
        else
        {
            onTripTitle.setText(currentTrip.gettitle());
            onTripDate.setText(currentTrip.getTourDate());
            onTripPlace.setText(currentTrip.getTourPlace());
            approxCost.setText("Cost so far: " + currentTrip.getCost() + " BDT");

            myTextViews[1].setText(currentTrip.getDayFirst());
            if(myTextViews[1].getText().toString().trim().equals("") == false)
            {
                myTextViews[1].setVisibility(View.VISIBLE);
                dayIdentifiers[1].setVisibility(View.VISIBLE);
                dayIdentifiers[1].setText(" Day 1 ");
                currentDay = 1;
            }

            myTextViews[2].setText(currentTrip.getDaySecond());
            if(myTextViews[2].getText().toString().trim().equals("") == false)
            {
                myTextViews[2].setVisibility(View.VISIBLE);
                dayIdentifiers[2].setVisibility(View.VISIBLE);
                dayIdentifiers[2].setText(" Day 2 ");
                currentDay = 2;
            }

            myTextViews[3].setText(currentTrip.getDayThird());
            if(myTextViews[3].getText().toString().trim().equals("") == false)
            {
                myTextViews[3].setVisibility(View.VISIBLE);
                dayIdentifiers[3].setVisibility(View.VISIBLE);
                dayIdentifiers[3].setText(" Day 3 ");
                currentDay = 3;
            }
            myTextViews[4].setText(currentTrip.getDayFourth());
            if(myTextViews[4].getText().toString().trim().equals("") == false)
            {
                myTextViews[4].setVisibility(View.VISIBLE);
                dayIdentifiers[4].setVisibility(View.VISIBLE);
                dayIdentifiers[4].setText(" Day 4 ");
                currentDay = 4;
            }

            myTextViews[5].setText(currentTrip.getDayFifth());
            if(myTextViews[5].getText().toString().trim().equals("") == false)
            {
                myTextViews[5].setVisibility(View.VISIBLE);
                dayIdentifiers[5].setVisibility(View.VISIBLE);
                dayIdentifiers[5].setText(" Day 5 ");
                currentDay = 5;
            }

            onTripTitle.setVisibility(View.VISIBLE);
            onTripDate.setVisibility(View.VISIBLE);
            onTripPlace.setVisibility(View.VISIBLE);

            ScrollView mScrollView = view.findViewById(R.id.currentScroll);
            mScrollView.setVisibility(View.VISIBLE);

        }
    }

    private void saveData(){

        for(int i = 1; i<=5; i++)
        {
            String tmp = myTextViews[i].getText().toString().trim();

            if(tmp.length() >0)
            {
                switch (i){
                    case 1: currentTrip.setDayFirst(tmp);
                        break;
                    case 2: currentTrip.setDaySecond(tmp);
                        break;
                    case 3: currentTrip.setDayThird(tmp);
                        break;
                    case 4: currentTrip.setDayFourth(tmp);
                        break;
                    case 5: currentTrip.setDayFifth(tmp);
                        break;
                    default: break;
                }

            }
        }

        currentTrip.setCost(String.valueOf(totalCost));

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                .child("currentTrip")
                .child(userId);
        mReference.setValue(currentTrip);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDay:

                if(currentDay == 5)
                {
                    Toast.makeText(getActivity(), "Limit reached", Toast.LENGTH_SHORT).show();
                    return;
                }

                String des = dayDescription.getText().toString().trim();
                dayDescription.setText("");
                if (des.length() == 0) return;
                ++currentDay;

                if (currentDay == 1) deleteDay.setVisibility(View.VISIBLE);

                dayIdentifiers[currentDay].setText(" Day " + currentDay + " ");
                dayIdentifiers[currentDay].setVisibility(View.VISIBLE);

                myTextViews[currentDay].setText(des);
                myTextViews[currentDay].setVisibility(View.VISIBLE);


                break;

            case R.id.btnDeleteDay:


                dayIdentifiers[currentDay].setText("");
                dayIdentifiers[currentDay].setVisibility(View.GONE);

                myTextViews[currentDay].setText("");
                myTextViews[currentDay].setVisibility(View.GONE);

                currentDay--;

                if (currentDay == 0) deleteDay.setVisibility(View.GONE);

                break;

            case R.id.saveData:

                saveData();
             //   Toast.makeText(getActivity(), "Save button pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.addAmount:

                String value = amountAdder.getText().toString().trim();

                amountAdder.setText("       ");
                if(value.length() == 0) return;

                int amount = Integer.parseInt(value);
                totalCost += amount;

                approxCost.setText("Cost so far: " + totalCost + " BDT.");

                break;

            case R.id.deleteAmount:
                String nValue = amountAdder.getText().toString().trim();

                amountAdder.setText("       ");
                if(nValue.length() == 0) return;

                int nAmount = Integer.parseInt(nValue);
                totalCost -= nAmount;

                if(totalCost<0 ) totalCost = 0;

                approxCost.setText("Cost so far: " + totalCost + " BDT.");
                break;

            default:
                break;
        }
    }
}