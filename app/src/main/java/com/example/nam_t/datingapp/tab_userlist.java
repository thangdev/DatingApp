package com.example.nam_t.datingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_userlist extends Fragment {
    RangeSeekBar<Integer> age;
    private Button btn_save;
    private int min,max;
    public tab_userlist() {
        // Required empty public constructor
    }
    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab_userlist, container, false);
        age=(RangeSeekBar<Integer>) view.findViewById(R.id.filter_age);
        btn_save=(Button) view.findViewById(R.id.save);
        final TextView age_view=view.findViewById(R.id.age_view);
        // Get noticed while dragging
        age.setNotifyWhileDragging(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                min=age.getSelectedMinValue();
                max=age.getSelectedMaxValue();
                age_view.setText(min+"-"+max+" years old");
            }
        });
        return view;
    }

}