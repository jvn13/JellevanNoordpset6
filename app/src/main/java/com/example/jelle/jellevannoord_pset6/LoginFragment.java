package com.example.jelle.jellevannoord_pset6;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
Fragment which is displayed when the users wants to sign in with his/her account.
It handles the input of the email and password and then call the sign in method in the MainActivity.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    // Variables
    private EditText mEmail, mPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // Set custom title
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.login));
        // Assign the views
        assignViews(view);
        // Set the onclick listeners for the login button
        view.findViewById(R.id.loginButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Check is the email and password fields aren't empty and sign in the user
            case R.id.loginButton:
                checkFields();
                break;
        }
    }

    // Assigns the TextViews
    private void assignViews(View view) {
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.password);
    }

    // If the email and password fields aren't empty sign in the user, else display error
    private void checkFields() {
        // check email not empty
        if(isNotEmpty(mEmail)) {
            // check password not empty
            if(isNotEmpty(mPassword)) {
                // Sign in the user
                ((MainActivity)getActivity()).signIn(mEmail.getText().toString(),mPassword.getText().toString());
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
