package com.example.pruthvi.driverapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PassengerLocationActivity extends AppCompatActivity {

    private String TAG=PassengerLocationActivity.class.getSimpleName();

    private Button reuestLoc;

    ArrayList<ACRides> rideList;
    private static ACRidesAdapter acRideAdapter;
    private String passengerName;
    private String passengerDestination;
    private String passengerTime;
    private String passengerDate;

    private ListView mList;

    private Button goBack;

    FirebaseAuth mAuth;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_location);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

//        passengerName=getIntent().getStringExtra("pName");
//        passengerTime=getIntent().getStringExtra("pTime");
//        passengerDestination=getIntent().getStringExtra("pDest");
//        passengerDate=getIntent().getStringExtra("pDate");
        Log.d(TAG, "onCreate: "+passengerDestination);
        Log.d(TAG, "onCreate: "+passengerName);

        mList=findViewById(R.id.aclist);
        goBack=findViewById(R.id.dashBoardButton);

        rideList=new ArrayList<ACRides>();

        acRideAdapter=new ACRidesAdapter(rideList,getApplicationContext());
        mList.setAdapter(acRideAdapter);

        DatabaseReference mRoot=FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRef=mRoot.child("AcceptedRides").child(user.getDisplayName());

//        if(passengerName!=null && passengerDate!=null && passengerTime!=null && passengerDestination!=null){
//            ACRides newRide=new ACRides(passengerName,passengerDate,passengerTime,passengerDestination);
//            rideList.add(newRide);
//            acRideAdapter.notifyDataSetChanged();
//        }

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rideList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Passenger passenger=data.getValue(Passenger.class);
                    passengerName=passenger.getPassengerName();
                    passengerDate=passenger.getDate();
                    passengerDestination=passenger.getDestination();
                    passengerTime=passenger.getPickUpTime();
                    ACRides newRide=new ACRides(passengerName,passengerDate,passengerTime,passengerDestination);
                    rideList.add(newRide);
                    acRideAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PassengerLocationActivity.this,DashBoardActivity.class));
            }
        });


    }
}
