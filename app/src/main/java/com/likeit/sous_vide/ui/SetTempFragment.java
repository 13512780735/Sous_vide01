package com.likeit.sous_vide.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeit.sous_vide.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetTempFragment extends Fragment {


    public SetTempFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_temp, container, false);
    }

}
