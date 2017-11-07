package com.arm.tourist.Auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arm.tourist.Models.UserProfile;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    public final static String TAG = "UserInfoActivity";

    private static final int GALLERY_INTENT = 2;

    private DatabaseReference mdatabase;
    private StorageReference mStorage;
    private FirebaseUser user;

    private Button updateProfBtn, uploadImgBtn, editProf_btn;
    private EditText fullNameET, emailET, dobET, addrET, instET, phoneET;
    private ImageView propicIV;

    private String profilePicUrl;


    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mdatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        propicIV = myView.findViewById(R.id.img_profilepic);
        fullNameET = myView.findViewById(R.id.nameET);
        emailET = myView.findViewById(R.id.emailET);
        phoneET = myView.findViewById(R.id.phoneET);
        dobET = myView.findViewById(R.id.dobET);
        addrET = myView.findViewById(R.id.addrET);
        instET = myView.findViewById(R.id.instET);

        updateProfBtn = myView.findViewById(R.id.b_profUpdate);
        uploadImgBtn = myView.findViewById(R.id.b_uploadImg);
        editProf_btn = myView.findViewById(R.id.b_editProf);

        uploadImgBtn.setVisibility(View.INVISIBLE);
        updateProfBtn.setVisibility(View.INVISIBLE);

        editProf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProf_btn.setVisibility(View.GONE);
                uploadImgBtn.setVisibility(View.VISIBLE);
                updateProfBtn.setVisibility(View.VISIBLE);
                setEditingEnabled(true);
            }
        });

        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        updateProfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
                setEditingEnabled(false);
                uploadImgBtn.setVisibility(View.INVISIBLE);
                updateProfBtn.setVisibility(View.INVISIBLE);
                editProf_btn.setVisibility(View.VISIBLE);
            }
        });


        return myView;


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String uID = user.getUid();

            final StorageReference filePath = mStorage.child("photos").child(uID).child("ProfilePic");

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Upload Done", Toast.LENGTH_SHORT).show();

                    @SuppressWarnings("VisibleForTests")

                    Uri photoUrl = taskSnapshot.getDownloadUrl();

                    profilePicUrl = photoUrl.toString();
                    Picasso.with(getActivity()).load(photoUrl).into(propicIV);
                }
            });
        }

        else {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference mUserReference;
        String refKey = user.getUid();


        mUserReference = mdatabase.child("users").child(refKey);
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                if (userProfile == null) {
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
                            Picasso.with(getContext()).load(uri).fit().centerCrop().into(propicIV);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"image nai",Toast.LENGTH_LONG).show();
                    }
                });
                fullNameET.setText(userProfile.getName().toString());
                emailET.setText(userProfile.getEmail().toString());
                phoneET.setText(userProfile.getPhone().toString());
                dobET.setText(userProfile.getDOB().toString());
                addrET.setText(userProfile.getAddr().toString());
                instET.setText(userProfile.getInst().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load User Profile.",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void saveToDB() {
        setEditingEnabled(false);
        Toast.makeText(getContext(), "Saving to Database...", Toast.LENGTH_SHORT).show();

        mdatabase.child("users")
                .child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (emailET.getText().toString().equals("")) emailET.setText(user.getEmail());

                        final String name = fullNameET.getText().toString();
                        final String email = emailET.getText().toString();
                        final String phone = phoneET.getText().toString();
                        final String addr = addrET.getText().toString();
                        final String DOB = dobET.getText().toString();
                        final String inst = instET.getText().toString();
                        final String userID = user.getUid().toString();

                        final StorageReference filePath = mStorage.child("photos").child(userID).child("ProfilePic");

                        UserProfile user1 = new UserProfile(name, email, phone, userID, profilePicUrl, DOB, addr, inst);

                        mdatabase.child("users")
                                .child(userID)
                                .setValue(user1);

                        setEditingEnabled(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(), "data change cancelled", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        fullNameET.setEnabled(enabled);
        emailET.setEnabled(enabled);
        dobET.setEnabled(enabled);
        addrET.setEnabled(enabled);
        instET.setEnabled(enabled);
        phoneET.setEnabled(enabled);

        if (enabled) {
            updateProfBtn.setVisibility(View.VISIBLE);
        } else {
            updateProfBtn.setVisibility(View.GONE);
        }
    }

}