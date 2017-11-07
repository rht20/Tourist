package com.arm.tourist.PublishTour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class PublishFragmentFirst extends Fragment {


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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.publish_first, container, false);

        init();


        mdatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        coverPicUrl = "";

        nextPage.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {

                TourEvent myEvent = getEvent();

                if(myEvent.getTourTitle().equals(""))
                {
                    Toast.makeText(getActivity(), "Some fields are blank", Toast.LENGTH_SHORT ).show();
                    return;
                }

                Bundle bundle = new Bundle();
                PublishFragmentSecond fragment = new PublishFragmentSecond();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                bundle.putParcelable("Object", myEvent);
                fragment.setArguments(bundle);
                fragmentTransaction.commit();

            }
        });

//        uploadButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT);
//            }
//        });
//


        return  myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_INTENT) {

                //Get ImageURi and load with help of picasso
                //Uri selectedImageURI = data.getData();
               // Picasso.with(getActivity()).load(uri).fit().centerCrop().into(coverPhoto);
                Picasso.with(getActivity()).load(data.getData()).noPlaceholder().centerCrop().fit()
                        .into(coverPhoto);
            }

        }
    }

    private void init() {
        nextPage = myView.findViewById(R.id.btnNext);
        tourTitle = myView.findViewById(R.id.etTourTitle);
        startDate = myView.findViewById(R.id.etStartDate);
        endDate = myView.findViewById(R.id.etEndDate);
        Place = myView.findViewById(R.id.etDestination);
        Residence = myView.findViewById(R.id.etResidence);
        Guide = myView.findViewById(R.id.etGuideInfo);
        Cost = myView.findViewById(R.id.etCost);
    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getActivity(), "asche", Toast.LENGTH_SHORT ).show();
//        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
//        {
//
//            Uri uri = data.getData();
//            Picasso.with(getContext()).load(uri).fit().centerCrop().into(coverPhoto);
//           //    String uID = user.getUid();
//            String uID = "yglzaSZddQNHiMojsF07AFaVOjA2";
//            final StorageReference filePath = mStorage.child("photos").child(uID).child("ProfilePic");
//            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getContext(), "Upload done",Toast.LENGTH_SHORT).show();
//
//                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            coverPicUrl = uri.toString();
//                            Picasso.with(getContext()).load(uri).fit().centerCrop().into(coverPhoto);
//                        }
//                    });
//
//                }
//            });
//
//
//        }
//    }

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
