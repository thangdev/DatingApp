package com.example.nam_t.datingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_profile extends Fragment {


    public tab_profile() {
        // Required empty public constructor
    }

    private CircleImageView imgProfile;
    private TextView txtNameProfile, txtDayProfile, txtMonthProfile, txtYearProfile, txtBioProfile;
    private Button btnSave, btnLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.current_user_profile, container, false);

        txtNameProfile = (TextView) rootView.findViewById(R.id.txtNameProfile);

        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Test", Toast.LENGTH_LONG).show();
            }
        });


        return rootView;

    }

}
