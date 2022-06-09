package com.ventustium.MyLnList.NovelDetailFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ventustium.MyLnList.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NovelNoHistory extends Fragment {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    View view;
    Button btnAddHistory;
    int LNId;
    Bundle bundle;
    String notes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_novel_no_history, container, false);
        btnAddHistory = view.findViewById(R.id.btnAddToHistory);

        bundle = this.getArguments();
        assert bundle != null;
        requireActivity().setTitle(bundle.getString("LNTitle"));
        LNId = bundle.getInt("LNId");
        btnAddHistory.setOnClickListener(view1 -> {
            clientPOST();
        });
        return view;
    }

    public void clientPOST() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getSessionID();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(Objects.equals(notes, "success")){
                            Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show();
                        }
                        if(Objects.equals(notes, "INVALID SESSION")){
                            Toast.makeText(requireActivity(), "Please Re Sign In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void getSessionID() throws Exception {
        String url = "https://api-mylnlist.ventustium.com/lnListUser/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST"); //PUT / DELETE
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.connect();
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("idLN", LNId);
        jsonParam.put("googleID", bundle.getString("IdGoogle"));
        jsonParam.put("volume", 0);
        jsonParam.put("chapter", 0);
        jsonParam.put("status", "PTR");
        jsonParam.put("score", 0);

        byte[] jsData = jsonParam.toString().getBytes("UTF-8");
        OutputStream os = con.getOutputStream();
        os.write(jsData);

        int responseCode = con.getResponseCode();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuffer response = new StringBuffer();
        while ((input = in.readLine()) != null) {
            response.append(input);
        }
        in.close();
        JSONArray myArray = new JSONArray(response.toString());
        JSONObject arrObj = myArray.getJSONObject(0);
        System.out.println(arrObj.getString("notes"));
        notes = arrObj.getString("notes");
        os.flush();
        os.close();
    }
}