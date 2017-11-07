package com.arm.tourist.NewsFeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.arm.tourist.Models.PostComment;
import com.arm.tourist.Models.UserProfile;
import com.arm.tourist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentSection extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PostComment> list;

    EditText comment;
    ImageView send;
    private String userName, userImage;

    //private ImageView
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        getUserInfoForComment();
        load_data();
    }

    public void load_data()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    PostComment postComment = dataSnapshot1.getValue(PostComment.class);
                    list.add(postComment);
                }

                adapter = new MyAdapter2(list,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

   private void getUserInfoForComment(){

       final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

       DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

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
                           }
                       }
                       //   setViews();

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
   }

    public void init()
    {
        comment = (EditText) findViewById(R.id.comment_edit_text);

    }
}