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

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public DatabaseReference mDatabase;
    public RelativeLayout progressLayout;
    public BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressLayout = findViewById(R.id.progressLayout);
        progressLayout.setVisibility(View.GONE);
        setListener();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(mAuth.getCurrentUser());
    }

    public void setListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI(firebaseAuth.getCurrentUser());
            }
        };
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            navigation.setVisibility(View.VISIBLE);
            bottomNavigationFragmentManager(navigation.getSelectedItemId());
        } else {
            navigation.setVisibility(View.GONE);
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment, "home").commit();
        }
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
                    writeUsername();
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

    private void writeUsername() {
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
}
