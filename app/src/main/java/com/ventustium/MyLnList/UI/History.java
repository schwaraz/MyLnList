package com.ventustium.MyLnList.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ventustium.MyLnList.IdTitleModel;
import com.ventustium.MyLnList.NovelDetail;
import com.ventustium.MyLnList.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class History extends Fragment {
    View view;
    ListView mylv;
    LibraryCustomAdapter aAdapter;

    SwipeRefreshLayout swipeRefreshLayout;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    GoogleSignInAccount account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().setTitle("History");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        mylv = view.findViewById(R.id.lvHistory);


        account = GoogleSignIn.getLastSignedInAccount(requireActivity());

        if (account == null) {
            Fragment Account = new HistoryAccount();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, Account).commit();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNovel();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshHistory);
        swipeRefreshLayout.setOnRefreshListener(this::getNovel);
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
                    mylv.setClickable(true);
                    mylv.setOnItemClickListener((adapterView, view1, i, l) -> {
                        Intent intent = new Intent(getContext(), NovelDetail.class);
                        intent.putExtra("LNId", finalResult.get(i).getIdLN());
                        intent.putExtra("LNTitle", finalResult.get(i).getTitle());
                        intent.putExtra("LNDescription", finalResult.get(i).getDescription());
                        startActivity(intent);
                    });
                }
            });
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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

    private List<IdTitleModel> getREST() throws Exception {
        Log.d("Kevin", "request Novel List Begin");
        String url = "https://api-mylnlist.ventustium.com/lnListUserHistory1/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.connect();
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("googleID", account.getId());
        Log.d("Kevin", "Send googleID: " + account.getId());

        byte[] jsData = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
        OutputStream os = con.getOutputStream();
        os.write(jsData);
        os.flush();
        os.close();
        Log.d("Kevin", "Wait Response");

        int responseCode = con.getResponseCode();
        Log.d("Kevin", "Send Get Request to : " + url);
        Log.d("Kevin", "Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuilder response = new StringBuilder();
        while ((input = in.readLine()) != null) {
            response.append(input);
        }
        in.close();

        JSONArray myArray1 = new JSONArray(response.toString());
        Log.d("Kevin", "Array : \n" + myArray1.length());
        List<IdTitleModel> result = new ArrayList<>();
        for (int i = 0; i < myArray1.length(); i++) {
            JSONObject arrObj = myArray1.getJSONObject(i);
            IdTitleModel u = new IdTitleModel();
            u.setId(arrObj.getInt("id"));
            u.setIdLN(arrObj.getInt("idLN"));
            u.setTitle(arrObj.getString("title"));
            u.setDescription(arrObj.getString("description"));
            result.add(u);
        }
        return result;
    }
}