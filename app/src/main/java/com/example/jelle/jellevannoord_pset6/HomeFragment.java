package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Set custom title
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.app_name));
        // Assign the Button views
        Button mRegister = view.findViewById(R.id.registerButton);
        TextView mLogin = view.findViewById(R.id.loginLink);
        // Set the onclick listeners of the buttons
        mRegister.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Go to the Register Fragment with a custom transition
            case R.id.registerButton:
                RegisterFragment registerFragment = new RegisterFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right,R.animator.slide_out_left,R.animator.slide_in_right)
                        .replace(R.id.fragment, registerFragment).addToBackStack(null).commit();
                break;
            // Go to the Login Fragment with a custom transition
            case R.id.loginLink:
                LoginFragment loginFragment = new LoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right,R.animator.slide_out_left,R.animator.slide_in_right)
                        .replace(R.id.fragment, loginFragment).addToBackStack(null).commit();
                break;
        }
    }
}
