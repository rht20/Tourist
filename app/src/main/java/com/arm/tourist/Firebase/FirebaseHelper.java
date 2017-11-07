package com.arm.tourist.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public final class FirebaseHelper {

    public static FirebaseDatabase mDatabase;
    public static FirebaseStorage mStorage;
    public static FirebaseUser mUser;

    public static FirebaseDatabase getFirebaseDatabaseInstance() {

        if (mDatabase == null) {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance();
        }

        return mDatabase;
    }

    public static FirebaseStorage getFirebaseStorageInstance() {

        if (mStorage == null) {
            mStorage = FirebaseStorage.getInstance();
        }

        return mStorage;
    }

    public static FirebaseUser getFirebaseUser()
    {
        if(mUser==null)
        {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        return mUser;
    }

}
