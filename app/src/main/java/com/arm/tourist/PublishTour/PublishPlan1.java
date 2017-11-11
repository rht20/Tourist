package com.arm.tourist.PublishTour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.arm.tourist.Models.TourEvent;
import com.arm.tourist.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PublishPlan1 extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1 ;
    View myView;
    private static final int GALLERY_INTENT = 2;
    Button nextPage;
    EditText tourTitle, startDate, endDate;
    EditText Place, Residence, Guide, Cost;
    String coverPicUrl;
    ImageButton uploadButton;
    ImageView coverPhoto;

    private DatabaseReference mdatabase;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_plan1);

        mdatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        init();

        coverPicUrl = "";

        nextPage.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {

                TourEvent myEvent = getEvent();

                if(myEvent.getTourTitle().equals(""))
                {
                    Toast.makeText(PublishPlan1.this, "Some fields are blank", Toast.LENGTH_SHORT ).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable("Event",myEvent);
                Intent intent = new Intent(PublishPlan1.this, PublishPlan2.class);
                intent.putExtras(bundle);
                //startActivity(intent);
                startActivityForResult(intent,404);
                finish();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==404)
        {
            finish();
        }
    }

    private void init() {
        nextPage = findViewById(R.id.btnNext);
        tourTitle = findViewById(R.id.etTourTitle);
        startDate = findViewById(R.id.etStartDate);
        endDate = findViewById(R.id.etEndDate);
        Place = findViewById(R.id.etDestination);
        Residence = findViewById(R.id.etResidence);
        Guide = findViewById(R.id.etGuideInfo);
        Cost = findViewById(R.id.etCost);
    }
    private TourEvent getEvent()
    {
        TourEvent myEvent = new TourEvent();

        myEvent.setTourTitle(tourTitle.getText().toString());
        myEvent.setStartDate(startDate.getText().toString());
        myEvent.setEndDate(endDate.getText().toString());
        myEvent.setTourPlace(Place.getText().toString());
        myEvent.setHotelDescription(Residence.getText().toString());
        myEvent.setGuideName(Guide.getText().toString());
        myEvent.setTotalCost(Cost.getText().toString());
        myEvent.setCover(coverPicUrl);


        if(myEvent.getHotelDescription().equals("")) myEvent.setTourTitle("");
        else if(myEvent.getStartDate().equals("")) myEvent.setTourTitle("");
        else if(myEvent.getEndDate().equals("")) myEvent.setTourTitle("");
        else if(myEvent.getTourPlace().equals("")) myEvent.setTourTitle("");
        else if(myEvent.getGuideName().equals("")) myEvent.setTourTitle("");
        else if(myEvent.getTotalCost().equals("")) myEvent.setTourTitle("");



        return myEvent;
    }


}
