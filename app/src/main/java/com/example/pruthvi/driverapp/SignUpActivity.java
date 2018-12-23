package com.example.pruthvi.driverapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SignUpActivity extends AppCompatActivity {

    public EditText mEmailEn;
    public EditText mPasswordEn;
    private Button mRegister;
    private EditText mFullName;
    private EditText mPhoneNo;

    private DatabaseReference mReference;


    private FirebaseAuth mAuth;
    private String emailEn;
    private String passwordEn;
    private String mFullNameText;
    private String mPhoneText;



    private Spinner locationSpinner;
    private ArrayAdapter<String> locationAdapter;

    private String mLocationSelect;

    private String photoUrl;

    GoogleSignInClient mGoogleSignInClient;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        mEmailEn=findViewById(R.id.emailEnText);
        mPasswordEn=findViewById(R.id.passwordEnText);
        mRegister=findViewById(R.id.registerButton);
        mFullName=findViewById(R.id.nameText);
        mPhoneNo=findViewById(R.id.phoneText);


        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mReference=FirebaseDatabase.getInstance().getReference();





        List<String> locationList;

        locationSpinner=findViewById(R.id.locationSpinner);
        locationList=new ArrayList<>();
        locationList.add("Panjim");
        locationList.add("Vasco Da Gama");
        locationList.add("Margao");
        locationList.add("Verna");


        locationAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //photoUrl=mAuth.getCurrentUser().getPhotoUrl();
        photoUrl="empty";
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEn=mEmailEn.getText().toString();
                passwordEn=mPasswordEn.getText().toString();
                Log.d("SignUpActivity", "emailEn: "+emailEn);
                Log.d("SignUpActivity", "passwordEn: "+passwordEn);
                Log.d("SignUpActivity", "ref : "+mReference.toString());
                mFullNameText=mFullName.getText().toString();
                mPhoneText=mPhoneNo.getText().toString();
                mLocationSelect=locationSpinner.getSelectedItem().toString();

                DriverDetails driverDetails=new DriverDetails(mFullNameText,mPhoneText,mLocationSelect,photoUrl);
                Log.d("SignUpActivity", "location : "+mLocationSelect);
                mReference.child("cabDrivers").child(mFullNameText).setValue(driverDetails);

                startActivity(new Intent(SignUpActivity.this,HomeScreenActivity.class));
                //register();
            }
        });

    }


    /**
     *
     * @param menu
     * @return boolean value
     */
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return boolean value.option selected from toolbar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                signOut();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     *
     */
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SignUpActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this,HomeScreenActivity.class));
                    }
                });
    }
}
