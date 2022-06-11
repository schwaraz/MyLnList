package com.ventustium.MyLnList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;




public class AutoCompleteAdapter extends ArrayAdapter<IdTitleModel> {
    private List<IdTitleModel> novelListFull;
    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<IdTitleModel> novelList) {
        super(context, 0, novelList);
        novelListFull = new ArrayList<>(novelList);
    }

    public Filter getFilter() {
        return novelFilter;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview1, parent, false);
        }

        TextView textViewTitle = convertView.findViewById(R.id.tv1);

        IdTitleModel idTitleModel = getItem(position);
        if(idTitleModel != null){
            textViewTitle.setText(idTitleModel.getTitle());
        }

        return convertView;
    }

    private Filter novelFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<IdTitleModel> suggestions = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                suggestions.addAll(novelListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (IdTitleModel item : novelListFull){
                    if (item.getTitle().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List)filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((IdTitleModel) resultValue).getTitle();
        }
    };
}