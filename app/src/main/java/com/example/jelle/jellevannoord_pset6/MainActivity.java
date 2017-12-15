package com.example.jelle.jellevannoord_pset6;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/*
MainActivity which handles the registration and login.
It also handles the QuizFragment, HighscoreFragment and AccountFragment
 */

public class MainActivity extends AppCompatActivity {

    public static DatabaseReference sDatabase;
    public static FirebaseAuth sAuth;
    public static RelativeLayout sProgressLayout;
    public BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set all the variables
        sAuth = FirebaseAuth.getInstance();
        sDatabase = FirebaseDatabase.getInstance().getReference();
        sProgressLayout = findViewById(R.id.progressLayout);
        sProgressLayout.setVisibility(View.INVISIBLE);
        // Set listener when no instance is saved
        if(null == savedInstanceState) {
            setListener();
        }
        // Set the bottom navigation
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        sAuth = FirebaseAuth.getInstance();
        sDatabase = FirebaseDatabase.getInstance().getReference();
        updateUI(sAuth.getCurrentUser());
    }

    // Sets a custom title
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    // Listens to a change in the Authentication state
    public void setListener() {
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI(firebaseAuth.getCurrentUser());
            }
        };
    }

    // Signs in a Firebase User, displays an error when it fails
    public void signIn(String email, String password) {
        sProgressLayout.setVisibility(View.VISIBLE);
        sAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    sProgressLayout.setVisibility(View.GONE);
                    Log.d("logged in", "signInWithEmail:success");
                    FirebaseUser mUser = sAuth.getCurrentUser();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    updateUI(mUser);
                } else {
                    // If sign in fails, display a message to the user.
                    sProgressLayout.setVisibility(View.GONE);
                    Log.w("failed login", "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    // Creates a Firebase User and signs in that user, displays an error when it fails
    public void createAccount(String email, String password) {
        sProgressLayout.setVisibility(View.VISIBLE);
        sAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("created users", "createUserWithEmail:success");
                    addUserToDatabase();
                    sProgressLayout.setVisibility(View.GONE);
                    FirebaseUser mUser = sAuth.getCurrentUser();
                    updateUI(mUser);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("failed to create user", "createUserWithEmail:failure", task.getException());
                    sProgressLayout.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    // Updates the interface based on the Firebase User
    public void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            navigation.setVisibility(View.VISIBLE);
            bottomNavigationFragmentManager(navigation.getSelectedItemId());
        } else {
            navigation.setVisibility(View.GONE);
            HomeFragment mFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mFragment, "home").commit();
        }
    }

    // Adds a user with an initial username and karma to the Firebase database
    private void addUserToDatabase() {
        ValueEventListener settingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Object mNewUser = dataSnapshot.getValue();
                // Get the initial user number
                String mNumber;
                if(mNewUser == null) {
                    mNumber = "1884";
                } else {
                    mNumber = mNewUser.toString();
                }
                // Add the user to the database
                FirebaseUser mUser = sAuth.getCurrentUser();
                sDatabase.child("users").child(mUser.getUid()).child("username").setValue("user_"+mNumber);
                sDatabase.child("users").child(mUser.getUid()).child("karma").setValue(0);
                sDatabase.child("settings").child("newUser").setValue(String.valueOf(Integer.parseInt(mNumber)+1));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        // Get the initial user number from the settings and add the user to the database
        sDatabase.child("settings").child("newUser").addListenerForSingleValueEvent(settingsListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return bottomNavigationFragmentManager(item.getItemId());
        }
    };

    // Handles the fragments when clicking the bottom navigator
    private boolean bottomNavigationFragmentManager(int id) {
        switch (id) {
            case R.id.navigation_home:
                QuizFragment mQuizFragment = new QuizFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mQuizFragment, "quiz").commit();
                return true;
            case R.id.navigation_highscore:
                HighscoreFragment mHighscoreFragment = new HighscoreFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mHighscoreFragment, "quiz").commit();
                return true;
            case R.id.navigation_account:
                AccountFragment mAccountFragment = new AccountFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mAccountFragment, "quiz").commit();
                return true;
        }
        return false;
    }
}
