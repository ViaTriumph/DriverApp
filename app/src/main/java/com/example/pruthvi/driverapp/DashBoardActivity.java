package com.example.pruthvi.driverapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    ArrayList<Ride> rideList;
    private static RideAdapter requestRideAdapter;
    private ListView mList;
    private DatabaseReference mRootRef;

    FirebaseAuth mAuth;

    private String mLocation;

    ValueEventListener requestListener;
    private String lastPassenger;

    private Button mRideCheckButton;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        mAuth = FirebaseAuth.getInstance();

        mRideCheckButton=findViewById(R.id.checkRidesButton);

        FirebaseUser user = mAuth.getCurrentUser();

        Log.d("DashBoardActivity", "user: "+user.getDisplayName());



        mList=findViewById(R.id.list);

        rideList=new ArrayList<Ride>();

        requestRideAdapter=new RideAdapter(rideList,getApplicationContext());
        mList.setAdapter(requestRideAdapter);

        mRootRef=FirebaseDatabase.getInstance().getReference();



        Query query=mRootRef.child("cabDrivers").orderByChild("driverName").equalTo(user.getDisplayName());
        Query que=mRootRef.child("cabDrivers").child(user.getDisplayName());

        que.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DriverDetails currUser=dataSnapshot.getValue(DriverDetails.class);
                mLocation=currUser.getLocation();
                Log.d("DashBoardActivity", "location(query) " +mLocation);
                DatabaseReference mReference=mRootRef.child("location").child(mLocation);

                mReference.addValueEventListener(requestListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        requestListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                SharedPreferences sharedPref = DashBoardActivity.this.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                String prevPassenger=sharedPref.getString("lastAcceptedPassenger","none");
                Log.d("DashBoardActivity", "onDataChange: "+prevPassenger);
                rideList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String name = data.getKey();
                    Log.d("DashBoardActivity", "onDataChange name : "+name);
                    if(!prevPassenger.equals(name)) {
                        Passenger passenger = data.getValue(Passenger.class);
                        String destination = passenger.getDestination();
                        String pickUpTime = passenger.getPickUpTime();
                        String passengerName=passenger.getPassengerName();
                        String date=passenger.getDate();
                        String listadd = name + "-" + destination + "-" + pickUpTime;
                        Log.d("DashBoardActivity", "onDataChange : " + listadd);
                        Ride newRide=new Ride(R.drawable.taxi,name,date,pickUpTime,destination);
                        rideList.add(newRide);
                        requestRideAdapter.notifyDataSetChanged();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DashBoardActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };





        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ride passengerDetails=(Ride) mList.getItemAtPosition(i);

                String name=passengerDetails.getPassengerName();
                Log.d("DashBoardActivity", "onItemClick: "+passengerDetails.getDestination());
                String destination=passengerDetails.getDestination();
                String time=passengerDetails.getTime();
                String date=passengerDetails.getDate();
                Intent intent=new Intent(DashBoardActivity.this,RideFareActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("destinationPass",destination);
                intent.putExtra("time",time);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });

        mRideCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this,PassengerLocationActivity.class));
            }
        });
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is not logged in
        //opening the login activity
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(DashBoardActivity.this, HomeScreenActivity.class));
        }
    }
}
