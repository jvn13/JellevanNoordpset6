package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText mEmail, mPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.register));
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.password);
        Button mRegister = view.findViewById(R.id.registerButton);
        mRegister.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                checkFields();
                break;
        }
    }

    public void checkFields() {
        // Check if the email is not empty
        if(isNotEmpty(mEmail)) {
            // Check if the password is not empty
            if(isNotEmpty(mPassword)) {
                ((MainActivity)getActivity()).createAccount(mEmail.getText().toString(),mPassword.getText().toString());
            } else {
                Toast.makeText(getContext(), "Please provide a password",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please provide an email",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNotEmpty(EditText mEditText) {
        return mEditText.getText().toString().trim().length() != 0;
    }
}
