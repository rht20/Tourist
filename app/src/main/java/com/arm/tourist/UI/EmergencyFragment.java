package com.arm.tourist.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arm.tourist.Models.EmergencyContacts;
import com.arm.tourist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyFragment extends Fragment implements View.OnClickListener {


    View myView;
    DatabaseReference ref;

    private String userId;

    private Button caller1, caller2;
    private Button update1, update2;

    private EditText contact1, contact2;
    private TextView name1, name2;
    private EmergencyContacts myContact;
    private EditText editName1, editName2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.emergency_layout, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("emergency").child(userId);

        myContact = new EmergencyContacts();
        myContact.init();

        init();


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EmergencyContacts tmp = dataSnapshot.getValue(EmergencyContacts.class);

                if (tmp != null) {
                    myContact = tmp;
                    // Log.e("tour :", currentTrip.getTourTitle());
                    LoadData();
                } else Log.e("tour is null", "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        caller1.setOnClickListener(this);
        caller2.setOnClickListener(this);

        update1.setOnClickListener(this);
        update2.setOnClickListener(this);


        return myView;
    }

    private void init() {

        caller1 = myView.findViewById(R.id.callContact1);
        caller2 = myView.findViewById(R.id.callContact2);

        update1 = myView.findViewById(R.id.editContact1);
        update2 = myView.findViewById(R.id.editContact2);

        contact1 = myView.findViewById(R.id.emerencyContactNumber1);
        contact2 = myView.findViewById(R.id.emerencyContactNumber2);


        name1 = myView.findViewById(R.id.emergencyContactName1);
        name2 = myView.findViewById(R.id.emergencyContactName2);

        editName1 = myView.findViewById(R.id.editName1);
        editName2 = myView.findViewById(R.id.editName2);
    }

    private void saveToDatabase() {

        // myContact ta save korbo
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("emergency").child(userId).setValue(myContact);

    }

    private void LoadData() {
        name1.setText(myContact.getName1());
        name2.setText(myContact.getName2());

        contact1.setText(myContact.getPhone1());
        contact2.setText(myContact.getPhone2());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.callContact1:
                Intent callIntent1 = new Intent(Intent.ACTION_DIAL);
                String no1 = contact1.getText().toString();
                no1 = "tel:" + no1;
                callIntent1.setData(Uri.parse(no1));
                startActivity(callIntent1);
                break;

            case R.id.callContact2:
                Intent callIntent2 = new Intent(Intent.ACTION_DIAL);
                String no2 = contact2.getText().toString();
                no2 = "tel:" + no2;
                callIntent2.setData(Uri.parse(no2));
                startActivity(callIntent2);
                break;

            case R.id.editContact1:


                if(update1.getText().toString().equals("EDIT"))
                {
                    update1.setText("SAVE");

                    contact1.setFocusableInTouchMode(true);

                    name1.setVisibility(View.GONE);
                    editName1.setVisibility(View.VISIBLE);
                    editName1.setText(name1.getText().toString());
                }

                else
                {
                    update1.setText("EDIT");
                    myContact.setPhone1(contact1.getText().toString());
                    contact1.setFocusable(false);

                    name1.setVisibility(View.VISIBLE);
                    editName1.setVisibility(View.GONE);
                    name1.setText(editName1.getText().toString());
                    myContact.setName1(name1.getText().toString());
                }
                saveToDatabase();
                break;

            case R.id.editContact2:
                if(update2.getText().toString().equals("EDIT"))
                {
                    update2.setText("SAVE");
                    contact2.setFocusableInTouchMode(true);

                    name2.setVisibility(View.GONE);
                    editName2.setVisibility(View.VISIBLE);
                    editName2.setText(name2.getText().toString());

                }

                else
                {
                    update2.setText("EDIT");
                    myContact.setPhone2(contact2.getText().toString());

                    contact2.setFocusable(false);

                    name2.setVisibility(View.VISIBLE);
                    editName2.setVisibility(View.GONE);
                    name2.setText(editName2.getText().toString());
                    myContact.setName2(name2.getText().toString());
                }

                saveToDatabase();
                break;

            default:
                break;

        }
    }
}