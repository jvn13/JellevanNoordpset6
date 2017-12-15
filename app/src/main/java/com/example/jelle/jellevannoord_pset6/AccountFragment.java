package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/*

 */

public class AccountFragment extends Fragment implements View.OnClickListener {

    private Button mConfirm, mLogout;
    private TextView mUsernameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        assignViews(view);
        mConfirm.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        getUsername();
    }

    private void assignViews(View view) {
        mConfirm = view.findViewById(R.id.change);
        mLogout = view.findViewById(R.id.logout);
        mUsernameTextView = view.findViewById(R.id.usernameTextView);
    }

    public void getUsername() {
        ValueEventListener settingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object
                String mUsername;
                if(dataSnapshot.getValue() == null) {
                    mUsername = "";
                } else {
                    mUsername = dataSnapshot.getValue().toString();
                }
                mUsernameTextView.setText(mUsername);
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

    public void setUsername() {
        CharSequence mUsername = mUsernameTextView.getText();
        if(mUsername != null && mUsername != "") {
            MainActivity.sDatabase.child("users").child(MainActivity.sAuth.getCurrentUser().getUid()).child("username").setValue(mUsername.toString());
            Toast.makeText(getContext(),"Username updated",Toast.LENGTH_LONG);
        } else {
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
