package com.ventustium.MyLnList.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class History extends Fragment {
    View view;
    ListView mylv;
    ArrayAdapter<String> aAdapter;

    SwipeRefreshLayout swipeRefreshLayout;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    GoogleSignInAccount account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, Account).commit();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Kevin", "request Novel List");
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
                if (getActivity() != null) {
                    ArrayList<Integer> id = new ArrayList<>();
                    ArrayList<Integer> idLN = new ArrayList<>();
                    ArrayList<String> title = new ArrayList<>();
                    ArrayList<String> description = new ArrayList<>();
                    if (finalResult != null){
                        Log.d("finalResult", "Size: "+ finalResult.size());
                        for(int i = 0; i < finalResult.size(); i++){
                            id.add(finalResult.get(i).getId());
                            idLN.add(finalResult.get(i).getIdLN());
                            title.add(finalResult.get(i).getTitle());
                            description.add(finalResult.get(i).getDescription());
                        }
                        LibraryCustomAdapter aAdapter = new LibraryCustomAdapter(getActivity(), id, title);
                        mylv.setAdapter(aAdapter);
                        mylv.setClickable(true);
                        mylv.setOnItemClickListener((adapterView, view1, i, l) -> {
                            Intent intent = new Intent(getContext(), NovelDetail.class);
                            intent.putExtra("LNId", idLN.get(i));
                            intent.putExtra("LNTitle", title.get(i));
                            intent.putExtra("LNDescription", description.get(i));
                            Log.d("LNTitle1", description.get(i));
                            startActivity(intent);
                        });
                    }
                }
            });
        });
    }


    private List<IdTitleModel> getREST() throws Exception {
        Log.d("Kevin", "request Novel List Begin");
        String url = "https://api-mylnlist.ventustium.com/lnListUserHistory1/";
//        String url = "https://api-mylnlist.ventustium.com/lnlist/";
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
        Log.d("Kevin","Send Get Request to : " + url);
        Log.d("Kevin","Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuilder response = new StringBuilder();
        while ((input = in.readLine()) != null) {
            response.append(input);
        }
        in.close();
//        Log.d("Kevin","Data : \n" + response);

        JSONArray myArray1 = new JSONArray(response.toString());
        Log.d("Kevin","Array : \n" + myArray1.length());
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
        Log.d("Kevin", "Finish");
        return result;
    }
}