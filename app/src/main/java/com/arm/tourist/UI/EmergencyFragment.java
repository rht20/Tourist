package com.arm.tourist.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arm.tourist.R;

public class EmergencyFragment extends Fragment {


    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.emergency_layout, container, false);

     //   return super.onCreateView(inflater, container, savedInstanceState);


        final Button caller1 = myView.findViewById(R.id.btn_call_contact1);
        final Button caller2 = myView.findViewById(R.id.btn_call_contact2);
        final EditText contact1 = myView.findViewById(R.id.et_contact1);
        final EditText contact2 = myView.findViewById(R.id.et_contact2);

        caller1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            String no = contact1.getText().toString();
            no = "tel:" + no;
            callIntent.setData(Uri.parse(no));
            startActivity(callIntent);
        }
    });

        caller2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String no = contact2.getText().toString();
                no = "tel:" + no;
                callIntent.setData(Uri.parse(no));
                startActivity(callIntent);
            }
        });


        return  myView;
    }
}
