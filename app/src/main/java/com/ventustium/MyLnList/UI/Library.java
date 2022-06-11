package com.ventustium.MyLnList.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ventustium.MyLnList.IdTitleModel;
import com.ventustium.MyLnList.MainActivity;
import com.ventustium.MyLnList.NovelDetail;
import com.ventustium.MyLnList.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Library extends Fragment {
    ListView mylv;
    View view;
    LibraryCustomAdapter aAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    public Library() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().setTitle("Library");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_library, container, false);
        mylv = view.findViewById(R.id.lv1);
        getNovel();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::getNovel);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (aAdapter != null){
                    Log.d("Search", s);
                    aAdapter.getFilter().filter(s);
                }
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getNovel() {
        executor.execute(() -> {
            List<IdTitleModel> result = null;
            try {
                result = getREST();
                swipeRefreshLayout.setRefreshing(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<IdTitleModel> finalResult = result;
            handler.post(() -> {
                if (getActivity() != null && finalResult != null) {
                    aAdapter = new LibraryCustomAdapter(getActivity(), finalResult);
                    mylv.setAdapter(aAdapter);
                    mylv.setTextFilterEnabled(true);
                    mylv.setClickable(true);
                    mylv.setOnItemClickListener((adapterView, view1, i, l) -> {
                        Intent intent = new Intent(getContext(), NovelDetail.class);
                        intent.putExtra("LNId", finalResult.get(i).getId());
                        intent.putExtra("LNTitle", finalResult.get(i).getTitle());
                        intent.putExtra("LNDescription", finalResult.get(i).getDescription());
                        startActivity(intent);
                    });
                }
            });
        });
    }


    private List<IdTitleModel> getREST() throws Exception {
        String url = "https://api-mylnlist.ventustium.com/lnlist/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuilder response = new StringBuilder();
        while ((input = in.readLine()) != null) {
            response.append(input);
        }
        in.close();

        JSONArray myArray = new JSONArray(response.toString());
        List<IdTitleModel> result = new ArrayList<>();
        for (int i = 0; i < myArray.length(); i++) {
            JSONObject arrObj = myArray.getJSONObject(i);
            IdTitleModel u = new IdTitleModel();
            u.setId(arrObj.getInt("id"));
            u.setTitle(arrObj.getString("title"));
            u.setDescription(arrObj.getString("description"));
            result.add(u);
            }
        return result;
    }
}