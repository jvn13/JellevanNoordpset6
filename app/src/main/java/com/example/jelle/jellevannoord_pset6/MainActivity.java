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

    public static DatabaseReference sDatabase;
    public static FirebaseAuth sAuth;
    public static RelativeLayout sProgressLayout;
    public BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sAuth = FirebaseAuth.getInstance();
        sDatabase = FirebaseDatabase.getInstance().getReference();
        sProgressLayout = findViewById(R.id.progressLayout);
        sProgressLayout.setVisibility(View.INVISIBLE);

        if(null == savedInstanceState) {
            setListener();
        }

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

    public void setListener() {
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI(firebaseAuth.getCurrentUser());
            }
        };
    }

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

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

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

    private void addUserToDatabase() {
        ValueEventListener settingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Object mNewUser = dataSnapshot.getValue();
                String mNumber;
                if(mNewUser == null) {
                    mNumber = "1884";
                } else {
                    mNumber = mNewUser.toString();
                }
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
        sDatabase.child("settings").child("newUser").addListenerForSingleValueEvent(settingsListener);
    }

    static public void addKarma(final int earnedKarma) {
        ValueEventListener karmaListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Object mObject = dataSnapshot.getValue();
                int mKarma = Integer.parseInt(mObject.toString());
                mKarma = mKarma + earnedKarma;
                sDatabase.child("users").child(sAuth.getCurrentUser().getUid()).child("karma").setValue(mKarma);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        sDatabase.child("users").child(sAuth.getCurrentUser().getUid()).child("karma").addListenerForSingleValueEvent(karmaListener);
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
