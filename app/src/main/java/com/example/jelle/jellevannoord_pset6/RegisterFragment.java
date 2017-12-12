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

    EditText email;
    EditText password;
    Button register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.register));
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        register = view.findViewById(R.id.registerButton);
        register.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                handleRegistration();
                break;
        }
    }

    public void handleRegistration() {
        // check email not empty
        if(isNotEmpty(email)) {
            // check password not empty
            if(isNotEmpty(password)) {
                ((MainActivity)getActivity()).createAccount(email.getText().toString(),password.getText().toString());
            } else {
                Toast.makeText(getContext(), "Please provide a password",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please provide an email",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNotEmpty(EditText etText) {
        return etText.getText().toString().trim().length() != 0;
    }
}
