package com.ventustium.MyLnList.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ventustium.MyLnList.R;


public class History extends Fragment {
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requireActivity().setTitle("History");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);


        return view;
    }
}