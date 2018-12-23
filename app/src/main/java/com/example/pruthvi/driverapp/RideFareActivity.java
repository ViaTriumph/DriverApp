package com.example.pruthvi.driverapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Set;

public class RideFareActivity extends AppCompatActivity {

    private String TAG=RideFareActivity.class.getSimpleName();

    private EditText mFare;
    private String fareCost;

    private DatabaseReference mReference;
    private DatabaseReference mRef;

    private Button mSend;
    private String passengerName;
    private String passengerDestination;
    private String passengerTime;
    private String passengerDate;

    private String mPhoneNumber;
    private String mUserName;
    private String mEmail;
    private String mProfileUUID;

    private TextView pName;
    private TextView pDestination;
    private TextView pTime;
    private TextView pDate;

    DriverDetails currDriverDetails;



    FirebaseAuth mAuth;
    private String photoUrl;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_fare);

        mFare=findViewById(R.id.fareText);
        mSend=findViewById(R.id.sendButton);
        pName=findViewById(R.id.pNameText);
        pDestination=findViewById(R.id.pDest);
        pTime=findViewById(R.id.pTime);
        pDate=findViewById(R.id.pDate);

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();
        photoUrl=user.getPhotoUrl().toString();

        mRef=FirebaseDatabase.getInstance().getReference();
        mReference=mRef.child("cabDrivers").child(user.getDisplayName());


        passengerName=getIntent().getExtras().getString("name");
        passengerDestination=getIntent().getExtras().getString("destinationPass");
        passengerTime=getIntent().getExtras().getString("time");
        passengerDate=getIntent().getExtras().getString("date");
        pName.setText("Name : "+passengerName);
        pDestination.setText("Destination : "+passengerDestination);
        pTime.setText("Time : "+passengerTime);
        pDate.setText("Date : "+passengerDate);
        Log.d(TAG, "onCreate: "+passengerDestination);


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currDriverDetails=new DriverDetails();
                currDriverDetails=dataSnapshot.getValue(DriverDetails.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO

                mPhoneNumber=currDriverDetails.getPhoneNo();
                mUserName=currDriverDetails.getDriverName();
                Log.d("RideFareActivity", "passengerName: "+passengerName);
                Log.d("RideFareActivity", "userName: "+mUserName);
                Log.d("RideFareActivity", "phoneNo: "+mPhoneNumber);
                fareCost=mFare.getText().toString();
                sendFare(passengerName,fareCost,passengerDestination,mPhoneNumber,mUserName,passengerDate,passengerTime);
                rideAccepted(passengerName,passengerDestination,passengerTime,mUserName,passengerDate);
                SharedPreferences sharedPref = RideFareActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lastAcceptedPassenger",passengerName);
                editor.commit();
                Intent sendPassengerDetails =new Intent(RideFareActivity.this,PassengerLocationActivity.class);
                sendPassengerDetails.putExtra("pName",passengerName);
                sendPassengerDetails.putExtra("pTime",passengerTime);
                sendPassengerDetails.putExtra("pDate",passengerDate);
                sendPassengerDetails.putExtra("pDest",passengerDestination);
                startActivity(sendPassengerDetails);
            }
        });

    }

    /**
     *
     * @param passengerName
     * @param fare
     * @param destination
     * @param phoneNo
     * @param driverName
     * @param passengerDate
     * @param passengerTime
     */
    public void sendFare(String passengerName,String fare,String destination,String phoneNo,String driverName,String passengerDate,String passengerTime){
        FareDetails newFare=new FareDetails(driverName,photoUrl,fare,destination,phoneNo,passengerDate,passengerTime);

        mRef.child("farequery").child(passengerName).child(driverName).setValue(newFare);
    }

    /**
     *
     * @param passengerName
     * @param passengerDestination
     * @param passengerTime
     * @param driverName
     * @param passengerDate
     */
    public void rideAccepted(String passengerName,String passengerDestination,String passengerTime,String driverName,String passengerDate){
        Passenger newPassenger=new Passenger(passengerDestination,passengerTime,passengerName,passengerDate);
        Log.d(TAG, "rideAccepted: "+passengerName);
        mRef.child("AcceptedRides").child(driverName).child(passengerName).setValue(newPassenger);
    }
}
