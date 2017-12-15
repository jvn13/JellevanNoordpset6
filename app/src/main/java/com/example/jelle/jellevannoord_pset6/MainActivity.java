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

public class MainActivity extends AppCompatActivity {

    static public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    static public DatabaseReference mDatabase;
    static public RelativeLayout progressLayout;
    public BottomNavigationView navigation;
    public Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressLayout = findViewById(R.id.progressLayout);
        progressLayout.setVisibility(View.INVISIBLE);

        if(null == savedInstanceState) {
            QuizFragment quizFragment = new QuizFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, quizFragment, "quiz").commit();
        }

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        updateUI(mAuth.getCurrentUser());
    }


    public void signIn(String email, String password) {
        progressLayout.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressLayout.setVisibility(View.GONE);
                    Log.d("logged in", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    progressLayout.setVisibility(View.GONE);
                    Log.w("failed login", "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void createAccount(String email, String password) {
        progressLayout.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("created users", "createUserWithEmail:success");
                    addUserToDatabase();
                    progressLayout.setVisibility(View.GONE);
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("failed to create user", "createUserWithEmail:failure", task.getException());
                    progressLayout.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void updateUI(FirebaseUser currentUser) {
        if(currentUser == null) {
            navigation.setVisibility(View.GONE);
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment, "home").commit();
        } else {
            navigation.setVisibility(View.VISIBLE);
        }
    }

    private void addUserToDatabase() {
        ValueEventListener settingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Object newUser = dataSnapshot.getValue();
                String number;
                if(newUser == null) {
                    number = "1884";
                } else {
                    number = newUser.toString();
                }
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabase.child("users").child(user.getUid()).child("username").setValue("user_"+number);
                mDatabase.child("users").child(user.getUid()).child("karma").setValue(0);
                mDatabase.child("settings").child("newUser").setValue(String.valueOf(Integer.parseInt(number)+1));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("settings").child("newUser").addListenerForSingleValueEvent(settingsListener);
    }

    static public void addKarma(final int earnedKarma) {
        ValueEventListener karmaListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Object object = dataSnapshot.getValue();
                int karma = 0;
                if(object != null) {
                    karma = Integer.parseInt(object.toString());
                }
                karma = karma + earnedKarma;
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("karma").setValue(karma);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("user").child(mAuth.getCurrentUser().getUid()).child("karma").addListenerForSingleValueEvent(karmaListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return bottomNavigationFragmentManager(item.getItemId());
        }
    };

    private boolean bottomNavigationFragmentManager(int id) {
        switch (id) {
            case R.id.navigation_home:
                QuizFragment quizFragment = new QuizFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, quizFragment, "quiz").commit();
                return true;
            case R.id.navigation_highscore:
                HighscoreFragment highscoreFragment = new HighscoreFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, highscoreFragment, "quiz").commit();
                return true;
            case R.id.navigation_account:
                AccountFragment accountFragment = new AccountFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, accountFragment, "quiz").commit();
                return true;
        }
        return false;
    }
}
