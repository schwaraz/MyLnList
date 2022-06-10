package com.ventustium.MyLnList.NovelDetailFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ventustium.MyLnList.R;

public class NovelHistory extends Fragment {
    View view;
    Bundle bundle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_novel_history, container, false);
        bundle = this.getArguments();
        assert bundle != null;
        requireActivity().setTitle(bundle.getString("LNTitle"));
        return view;

    }
}