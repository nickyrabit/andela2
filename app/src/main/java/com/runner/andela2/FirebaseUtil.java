package com.runner.andela2;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.runner.andela2.model.TravelDeal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDtaabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public  static ArrayList<TravelDeal> mDeals;
public static FirebaseAuth.AuthStateListener mAuthListener;
public static FirebaseAuth mFirebaseAuth;
private static Activity caller;
public  static FirebaseStorage mStorage;
public static StorageReference mStorageRef;
    private FirebaseUtil(){};

    public static void openFbReference(String ref, final Activity callerActivity){
        if(firebaseUtil==null){
            firebaseUtil = new FirebaseUtil();
            mFirebaseDtaabase = FirebaseDatabase.getInstance();
            caller = callerActivity;
            mFirebaseAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
    if(mFirebaseAuth.getCurrentUser()==null){
        FirebaseUtil.signIn();

    }
else{
        Toast.makeText(callerActivity.getApplicationContext(),"Welcome back",Toast.LENGTH_LONG).show();

    }
connectStorage();
    }
};





        }
        mDeals = new ArrayList<TravelDeal>();
        mDatabaseReference = mFirebaseDtaabase.getReference().child(ref);
    }

public static void detachListener(){
        mFirebaseAuth.removeAuthStateListener(mAuthListener);

}
public static void attachListener(){
       mFirebaseAuth.addAuthStateListener(mAuthListener);

}
public static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deal_picture");

}

private static void signIn(){
    caller.startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                            new AuthUI.IdpConfig.EmailBuilder().build()
                           ))
                    .build(),
            123 );
}
}
