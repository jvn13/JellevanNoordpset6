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

public class AccountFragment extends Fragment implements View.OnClickListener {

    Button confirm, logout;
    TextView usernameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        confirm = view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        getUsername();
    }

    public void getUsername() {
        ValueEventListener settingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String username;
                if(dataSnapshot.getValue() == null) {
                    username = "";
                } else {
                    username = dataSnapshot.getValue().toString();
                }
                usernameTextView.setText(username);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        MainActivity.mDatabase.child("users").child(MainActivity.mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(settingsListener);
    }

    public void setUsername() {
        CharSequence username = usernameTextView.getText();
        if(username != null && username != "") {
            MainActivity.mDatabase.child("users").child(MainActivity.mAuth.getCurrentUser().getUid()).child("username").setValue(username.toString());
            Toast.makeText(getContext(),"Username updated",Toast.LENGTH_LONG);
        } else {
            Toast.makeText(getContext(),"Invalid username",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.confirm:
                setUsername();
                break;
            case R.id.logout:
                MainActivity.mAuth.signOut();
                ((MainActivity)getActivity()).updateUI(MainActivity.mAuth.getCurrentUser());
                break;
        }
    }
}
