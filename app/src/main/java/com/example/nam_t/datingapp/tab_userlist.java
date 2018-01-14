package com.example.nam_t.datingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_userlist extends Fragment {
    RangeSeekBar<Integer> age;
    public tab_userlist() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_userlist, container, false);
    }

}