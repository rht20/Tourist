package com.arm.tourist.PublishTour;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.tourist.Models.OnTripDetails;
import com.arm.tourist.Models.TourEvent;
import com.arm.tourist.Models.UserProfile;
import com.arm.tourist.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class PublishPlan2 extends AppCompatActivity implements View.OnClickListener {

    Calendar calendar = Calendar.getInstance();

    String userRef;

    TextView tourTitle, tourLocation;
    Button publish, importDays;
    EditText shortNote;
    ImageView coverPhoto, photoFF, photoSS, photoTT;
    TourEvent myEvent;
    private DatabaseReference mdatabase;
    private StorageReference mStorage;
    String userName, userImage, cover, img1, img2, img3;
    String pushKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_plan2);

        mStorage = FirebaseStorage.getInstance().getReference();

        init();

        userRef = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Bundle bundle = getIntent().getExtras();
        myEvent = new TourEvent();
        myEvent = bundle.getParcelable("Event");


        tourTitle.setText(myEvent.getTourTitle());

        tourLocation.setText(myEvent.getTourPlace());
        mdatabase = FirebaseDatabase.getInstance().getReference();
        pushKey = mdatabase.child("Tours").push().getKey();


        final String userId = (FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        Log.e("User Id: ", userId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot tourSnapshot : dataSnapshot.getChildren()) {
                            UserProfile profile = tourSnapshot.getValue(UserProfile.class);

                            if (profile.getUserID().equals(userId)) {
                                Log.e("Usernames: ", "****************");
                                userName = profile.getName();
                                userImage = profile.getProfilePicUrl();

                            }

                            // if less than current date, don't add**************************************

                            Log.e("Usernames: ", profile.getName());

                        }
                        //   setViews();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

        publish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                myEvent.setNote(shortNote.getText().toString().trim());
                //   myEvent.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                saveToFirebase(myEvent);

                finish();
                //getParent().finish();

            }
        });

        coverPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ACTION_GET_CONTENT
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        photoFF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        photoSS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        photoTT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 4);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        final Uri uri = data.getData();

        ImageView iv = coverPhoto;
        String mChild = "cover";

        switch (requestCode) {
            case 2:
                iv = photoFF;
                mChild = "image1";
                break;
            case 3:
                iv = photoSS;
                mChild = "image2";
                break;
            case 4:
                iv = photoTT;
                mChild = "image3";
                break;
            default:
                break;
        }

        Log.e("mchild = ", mChild);

        final String finalMChild = mChild;
        mStorage.child("tourCovers").child(pushKey).child(mChild).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PublishPlan2.this, "Upload Done: " + finalMChild, Toast.LENGTH_SHORT).show();
                //Log.e("mchild = ", finalMChild);
                @SuppressWarnings("VisibleForTests")

                Uri photoUrl = taskSnapshot.getDownloadUrl();

                switch (finalMChild) {
                    case "cover":
                        cover = photoUrl.toString();
                        break;
                    case "image1":
                        img1 = photoUrl.toString();
                        break;
                    case "image2":
                        img2 = photoUrl.toString();
                        break;
                    case "image3":
                        img3 = photoUrl.toString();
                        break;
                    default:
                        break;
                }
              //  cover = photoUrl.toString();
            }
        });

        Picasso.with(PublishPlan2.this).load(uri).noPlaceholder().centerCrop().fit().into((iv));

    }

    private void saveToFirebase(TourEvent myEvent) {

        if(cover.equals("blank"))
        {
            Toast.makeText(this, "Set Cover Photo", Toast.LENGTH_SHORT).show();
            return;
        }
        myEvent.setUserImage(userImage);
        myEvent.setUserName(userName);
        myEvent.setCover(cover);
        Log.e("Cover: ", cover);
        if(img1.equals("blank") && img2.equals("blank") && img3.equals("blank"))
        {
            img1 = img2 = img3 = cover;
        }
        else if(img1.equals("blank") && img2.equals("blank") && !img3.equals("blank"))
        {
            img1 = img3;
            img2 = cover;
        }
        else if(img1.equals("blank") && !img2.equals("blank") && img3.equals("blank"))
        {
            img1 = img2;
            img2 = cover;
            img3 = img1;
        }
        else if(!img1.equals("blank") && img2.equals("blank") && img3.equals("blank"))
        {
            img2 = cover;
            img3 = img1;
        }
        else if(img1.equals("blank") && !img2.equals("blank") && !img3.equals("blank"))
        {
            img1 = img3;
        }
        else if(!img1.equals("blank") && img2.equals("blank") && !img3.equals("blank"))
        {
            img2 = cover;
        }
        else if(!img1.equals("blank") && !img2.equals("blank") && img3.equals("blank"))
        {
            img3 = img1;
        }
/*

       if(img3.equals("blank"))
       {
           myEvent.setImg1(cover);
       }

        if(img2.equals("blank"))
        {
            myEvent.setImg2(cover);
        }

        if(img1.equals("blank"))
        {
            myEvent.setImg1(cover);
        }
*/
        myEvent.setImg1(img1);
        myEvent.setImg2(img2);
        myEvent.setImg3(img3);
        myEvent.setLikeCount("0");
        myEvent.setCommentCount("0");
        myEvent.setTime(processDate());
        myEvent.setPostId(pushKey);

        mdatabase = FirebaseDatabase.getInstance().getReference();

        mdatabase.child("Tours").child(pushKey).setValue(myEvent);

        //   Toast.makeText(getActivity(), "Firebase e jaitese", Toast.LENGTH_SHORT).show();
        Log.e("pushkey: ", pushKey);

        return;
    }

    private void init() {
        coverPhoto = findViewById(R.id.ivCoverPhoto);
        tourTitle = findViewById(R.id.tourTitle);
        tourLocation = findViewById(R.id.tourLocation);
        publish = findViewById(R.id.publishPlan_btn);
        shortNote = findViewById(R.id.shortNote);
        photoFF = findViewById(R.id.ivTourImageOne);
        photoSS = findViewById(R.id.ivTourImageTwo);
        photoTT = findViewById(R.id.ivTourImageThree);
        cover = "blank";
        img1 = "blank";
        img2 = "blank";
        img3 = "blank";

        importDays = findViewById(R.id.importDays);
        importDays.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.importDays:

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("currentTrip").child(userRef);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        OnTripDetails tmp = dataSnapshot.getValue(OnTripDetails.class);

                        if (tmp != null) {
                            shortNote.append(extractDay(tmp));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                break;
            default:  break;

        }
    }

    private String extractDay(OnTripDetails plan)
    {
        String res = "";

        if(plan.getDayFirst() != null)
        {
            res += "\nDay 1\n";
            res += plan.getDayFirst();
        }

        if(plan.getDaySecond() != null)
        {
            res += "\nDay 2\n";
            res += plan.getDaySecond();
        }

        if(plan.getDayThird() != null)
        {
            res += "\nDay 3\n";
            res += plan.getDayThird();
        }

        if(plan.getDayFourth() != null)
        {
            res += "\nDay 4\n";
            res += plan.getDayFourth();
        }

        if(plan.getDayFifth() != null)
        {
            res += "\nDay 5\n";
            res += plan.getDayFifth();
        }
        return res;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    protected void onStop() {
        setResult(404);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        setResult(404);
        super.onDestroy();
    }

    private String processDate()
    {
        String date = calendar.getTime().toString();
        String temp[] = date.split(" ");
        date = temp[0];
        date += " ";
        date += temp[1];
        date += " ";
        date += temp[2];
        return date;
    }
}
