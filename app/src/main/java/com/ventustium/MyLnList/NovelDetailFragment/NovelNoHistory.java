package com.ventustium.MyLnList.NovelDetailFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ventustium.MyLnList.R;

import java.util.Objects;

public class NovelNoHistory extends Fragment {
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_novel_no_history, container, false);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        requireActivity().setTitle(bundle.getString("LNTitle"));
        return view;
    }
}