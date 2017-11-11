package com.arm.tourist.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm.tourist.Auth.ProfileFragment;
import com.arm.tourist.Auth.SignInActivity;
import com.arm.tourist.CurrentTrip.OntripFragment;
import com.arm.tourist.Maps.MapsActivity;
import com.arm.tourist.Models.UserProfile;
import com.arm.tourist.NewPlan.NewPlanActivity;
import com.arm.tourist.NewsFeed.ExplorePlanActivity;
import com.arm.tourist.PublishTour.PublishPlan1;
import com.arm.tourist.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser mUser;
    FirebaseAuth mAuth;

    TextView userName;
    ImageView userImage;
    String name, img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        userName = header.findViewById(R.id.navDrawerName);
        userImage = header.findViewById(R.id.navDrawerImage);

        init();

        HomepageFragment fragment = new HomepageFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "fragment1");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            ProfileFragment fragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
        }

        else if (id == R.id.nav_currentPlan) {
            OntripFragment fragment = new OntripFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();

        }

        else if (id == R.id.nav_explorePlan) {
            Intent intent = new Intent(MainActivity.this, ExplorePlanActivity.class);
            startActivity(intent);

            /*exploreFragment fragment = new exploreFragment();

            //     InterGalactic fragment = new InterGalactic();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();*/
        }

        else if (id == R.id.nav_emergency) {
            EmergencyFragment fragment = new EmergencyFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
        }

        else if (id == R.id.nav_newPlan) {
            Intent intent = new Intent(MainActivity.this, NewPlanActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_publish) {
            /*PublishFragmentFirst fragment = new PublishFragmentFirst();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();*/
            Intent intent = new Intent(MainActivity.this, PublishPlan1.class);
            startActivity(intent);
        }

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
            //this.finish();
        }

        if (id == R.id.nav_LogOut) {
            mAuth.signOut();

            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mUserReference;
        String refKey = user.getUid();
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();

        mUserReference = mdatabase.child("users").child(refKey);
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                if (userProfile == null) {
                    //Toast.makeText(ProfilePageActivity.this,"You have no info to show, Edit Profile",Toast.LENGTH_SHORT).show();
                    return;
                }

                StorageReference filePath = FirebaseStorage.getInstance().getReference()
                        .child("photos")
                        .child(user.getUid().toString())
                        .child("ProfilePic");

                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri != null)
                            Picasso.with(MainActivity.this).load(uri).fit().centerCrop().into(userImage);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                userName.setText(userProfile.getName().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
