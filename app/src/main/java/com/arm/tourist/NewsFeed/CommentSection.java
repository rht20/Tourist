package com.arm.tourist.NewsFeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm.tourist.Firebase.FirebaseHelper;
import com.arm.tourist.Models.PostComment;
import com.arm.tourist.Models.TourEvent;
import com.arm.tourist.Models.UserProfile;
import com.arm.tourist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommentSection extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PostComment> list;

    private ImageView imageView0, imageView1, imageView2, imageView3;
    private TextView dTitle, dNote, dDate, dResidence, dGuide, dCost;

    EditText comment;
    ImageView send;

    private PostComment postComment;
    private DatabaseReference reference1,reference2;
    private FirebaseDatabase database =  FirebaseHelper.getFirebaseDatabaseInstance();
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private String userName,userImage,str_comment,pushKey,postID,cmntCnt;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);
        postComment = new PostComment();

        //handle intents
        Bundle bundle = getIntent().getExtras();
        String temp  = (String) bundle.getString("PostID");
        String str[] = temp.split(" ");
        postID = str[0];
        cmntCnt = str[1];

        //Toast.makeText(this,postID,Toast.LENGTH_LONG).show();

        reference1 = database.getReference().child("Comments");
        reference2 = database.getReference().child("Tours").child(postID);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        init();
        getUserInfoForComment();

        load_data2();
        load_data1();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_comment = comment.getText().toString();
                comment.setText("");
                saveToFirebase();
            }
        });
    }

    public void load_data1()
    {
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    postComment = dataSnapshot1.getValue(PostComment.class);
                    if(postComment.getPostId().equals(postID)) list.add(postComment);
                }

                adapter = new MyAdapter2(list,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void load_data2()
    {
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TourEvent tourEvent = dataSnapshot.getValue(TourEvent.class);

                Log.e("cover: ", tourEvent.getCover());
                Picasso.with(CommentSection.this).load(tourEvent.getCover()).centerCrop().fit().into(imageView0);
                Picasso.with(CommentSection.this).load(tourEvent.getImg1()).centerCrop().fit().into(imageView1);
                Picasso.with(CommentSection.this).load(tourEvent.getImg2()).centerCrop().fit().into(imageView2);
                Picasso.with(CommentSection.this).load(tourEvent.getImg3()).centerCrop().fit().into(imageView3);

                dTitle.setText(tourEvent.getTourTitle());
                dNote.setText(tourEvent.getNote());
                dResidence.setText(tourEvent.getHotelDescription());
                dGuide.setText(tourEvent.getGuideName());
                dCost.setText(tourEvent.getTotalCost());

                String date = tourEvent.getStartDate()+" - "+tourEvent.getEndDate();

                dDate.setText(date);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

   private void getUserInfoForComment(){

       //Toast.makeText(this,userId,Toast.LENGTH_LONG).show();

       DatabaseReference ref = database.getReference().child("users");

       ref.addValueEventListener(
               new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                       for (DataSnapshot tourSnapshot : dataSnapshot.getChildren()) {
                           UserProfile profile = tourSnapshot.getValue(UserProfile.class);

                           if (profile.getUserID().equals(userId)) {
                               Log.e("Username: ", "***" + profile.getName() + "  ***");
                               userName = profile.getName();
                               userImage = profile.getProfilePicUrl();
                               //Toast.makeText(getApplicationContext(),userName+" "+userImage,Toast.LENGTH_LONG).show();
                               break;
                           }
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
   }

    private void saveToFirebase() {

        while (str_comment.length()>0 && str_comment.charAt(0)==' ')    str_comment = str_comment.substring(1);

        if(!str_comment.equals("")) {
            postComment.setPostId(postID);
            postComment.setUserName(userName);
            postComment.setUserImage(userImage);
            postComment.setCommentText(str_comment);
            postComment.setCommentTime(processDate());

            pushKey = database.getReference().child("Comments").child(postID).push().getKey();
            DatabaseReference commentRef = database.getReference().child("Comments").child(pushKey);

            commentRef.setValue(postComment);

            // Start updating comment count
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            int x = Integer.parseInt(cmntCnt);
            x++;
            cmntCnt = Integer.toString(x);
            databaseReference.child("Tours").child(postID).child("commentCount").setValue(cmntCnt);
            // End
        }
        return;
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
        date += "\n";
        date += temp[3];

        return date;
    }

    public void init()
    {
        comment = (EditText) findViewById(R.id.comment_edit_text);
        send = (ImageView)findViewById(R.id.post_button);

        imageView0 = (ImageView)findViewById(R.id.ivDetailsCover0);
        imageView1 = (ImageView)findViewById(R.id.ivDetailsCover1);
        imageView2 = (ImageView)findViewById(R.id.ivDetailsCover2);
        imageView3 = (ImageView)findViewById(R.id.ivDetailsCover3);

        dTitle = (TextView)findViewById(R.id.detailsTitle);
        dNote = (TextView)findViewById(R.id.detailsNote);
        dDate = (TextView)findViewById(R.id.detailsDate);
        dResidence = (TextView)findViewById(R.id.detailsResidence);
        dGuide = (TextView)findViewById(R.id.detailsGuide);
        dCost = (TextView)findViewById(R.id.detailsCost);
    }
}