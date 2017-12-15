package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
Fragment which is displayed when the users wants to register an account.
It handles the input of the email and password and then call the register method in the MainActivity.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {

    // Variables
    private EditText mEmail, mPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // Set a custom title in the action bar
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.register));
        // Assign the views
        assignViews(view);
        // Set the onclick listeners for the register button
        view.findViewById(R.id.registerButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Check is the email and password fields aren't empty and register the user
            case R.id.registerButton:
                checkFields();
                break;
        }
    }

    // Assigns the TextViews
    private void assignViews(View view) {
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.password);
    }

    // If the email and password fields aren't empty register the user, else display error
    public void checkFields() {
        // Check if the email is not empty
        if(isNotEmpty(mEmail)) {
            // Check if the password is not empty
            if(isNotEmpty(mPassword)) {
                // Register the user
                ((MainActivity)getActivity()).createAccount(mEmail.getText().toString(),mPassword.getText().toString());
            } else {
                Toast.makeText(getContext(), "Please provide a password",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please provide an email",Toast.LENGTH_SHORT).show();
        }
    }

    // Checks if an editText is empty
    private boolean isNotEmpty(EditText mEditText) {
        return mEditText.getText().toString().trim().length() != 0;
    }
}
