package com.ventustium.MyLnList.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ventustium.MyLnList.R;

import java.util.ArrayList;

class LibraryCustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<Integer> id;
    ArrayList<String> title;
    LayoutInflater inflater;

    public LibraryCustomAdapter(Context c, ArrayList<Integer> id, ArrayList<String> title) {
        this.c = c;
        this.id = id;
        this.title = title;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listview1, null);
        TextView tvtitle = view.findViewById(R.id.tv1);
        tvtitle.setText(title.get(i));
        return view;
    }
}
