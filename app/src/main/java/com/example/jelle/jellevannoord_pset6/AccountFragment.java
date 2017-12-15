package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/*
Fragment where the user is able to change his/her username and logout.
 */

public class AccountFragment extends Fragment implements View.OnClickListener {

    // Variables
    private Button mConfirm, mLogout;
    private EditText mUsernameEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        // Assign the views
        assignViews(view);
        // Set the onclick listeners for the buttons
        mConfirm.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Get the username of the users from Firebase
        getUsername();
    }

    // Assigns the Buttons and EditText
    private void assignViews(View view) {
        mConfirm = view.findViewById(R.id.change);
        mLogout = view.findViewById(R.id.logout);
        mUsernameEditText = view.findViewById(R.id.usernameEditText);
    }

    // Takes the username of the user from the Firebase database and displays it in the EditText
    public void getUsername() {
        ValueEventListener settingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object
                String mUsername;
                // Get the username value for the database
                if(dataSnapshot.getValue() == null) {
                    mUsername = "";
                } else {
                    mUsername = dataSnapshot.getValue().toString();
                }
                // Set the username to the EditText
                mUsernameEditText.setText(mUsername);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        FirebaseUser mUser = MainActivity.sAuth.getCurrentUser();
        MainActivity.sDatabase.child("users").child(mUser.getUid()).child("username")
                .addListenerForSingleValueEvent(settingsListener);
    }

    // Updates the username in the Firebase database
    public void setUsername() {
        // Get username from the EditText
        CharSequence mUsername = mUsernameEditText.getText();
        // Check if it is not empty
        if(mUsername != null && mUsername != "") {
            // Update the username in the Firebase database
            MainActivity.sDatabase.child("users").child(MainActivity.sAuth.getCurrentUser().getUid())
                    .child("username").setValue(mUsername.toString());
            Toast.makeText(getContext(),"Username updated",Toast.LENGTH_LONG);
        } else {
            // Display error
            Toast.makeText(getContext(),"Invalid username",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            // Change the username
            case R.id.change:
                setUsername();
                break;
            // Logout the current user and update the layout
            case R.id.logout:
                MainActivity.sAuth.signOut();
                ((MainActivity)getActivity()).updateUI(MainActivity.sAuth.getCurrentUser());
                break;
        }
    }
}
