package com.ventustium.MyLnList.NovelDetailFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ventustium.MyLnList.R;
import com.ventustium.MyLnList.databinding.FragmentNovelHistoryBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NovelHistory extends Fragment {
    FragmentNovelHistoryBinding binding;
    View view;
    Bundle bundle;

    String LNId, notes, status;
    String volume, chapter, score;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNovelHistoryBinding.inflate(inflater,container,false);
        view = inflater.inflate(R.layout.fragment_novel_history, container, false);
        bundle = this.getArguments();
        assert bundle != null;
        requireActivity().setTitle(bundle.getString("LNTitle"));
        Log.d("Kevin", bundle.getString("LNId")+ " " +bundle.getString("IdGoogle"));
        clientGET();
        binding.editTextVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clientPUT();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.editTextChapter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clientPUT();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.editTextScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clientPUT();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return binding.getRoot();

    }

    public void clientGET() {
        executor.execute(() -> {
            try {
                GetLnUserHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                if(score != null && chapter != null && volume != null){
                    binding.editTextScore.setText(score);
                    binding.editTextChapter.setText(chapter);
                    binding.editTextVolume.setText(volume);
                }
            });
        });
    }

    private void GetLnUserHistory() throws Exception{
        String url = "https://api-mylnlist.ventustium.com/get/lnListUser/";
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
        jsonParam.put("idLN", bundle.getString("LNId"));
        jsonParam.put("googleID", bundle.getString("IdGoogle"));
        Log.d("Kevin", bundle.getString("LNId")+ " " +bundle.getString("IdGoogle"));
        byte[] jsData = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
        OutputStream os = con.getOutputStream();
        os.write(jsData);

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
        Log.d("kevin", response.toString());
        JSONArray myArray = new JSONArray(response.toString());
        JSONObject arrObj = myArray.getJSONObject(0);
        volume = arrObj.getString("volume");
        chapter = arrObj.getString("chapter");
        score = arrObj.getString("score");
        status = arrObj.getString("status");
        os.flush();
        os.close();
    }

    public void clientPUT() {
        executor.execute(() -> {
            try {
                updateLnUserHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                if(notes != null){
                    Log.d("Kevin", notes);
//                    if(notes.equals("Updated")){
//                        Toast.makeText(requireActivity(), "Data Updated", Toast.LENGTH_SHORT).show();
//                        notes = "Error";
//                    }
                }

            });
        });
    }

    private void updateLnUserHistory() throws Exception{
        String url = "https://api-mylnlist.ventustium.com/lnListUser/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("PUT"); //PUT / DELETE
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.connect();
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("idLN", bundle.getString("LNId"));
        jsonParam.put("googleID", bundle.getString("IdGoogle"));
        jsonParam.put("volume", binding.editTextVolume.getText());
        jsonParam.put("chapter", binding.editTextChapter.getText());
        jsonParam.put("score", binding.editTextScore.getText());
        Log.d("Kevin", bundle.getString("LNId")+ " " +bundle.getString("IdGoogle"));
        byte[] jsData = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
        OutputStream os = con.getOutputStream();
        os.write(jsData);

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
        Log.d("kevin", response.toString());
        JSONArray myArray = new JSONArray(response.toString());
        JSONObject arrObj = myArray.getJSONObject(0);
        notes = arrObj.getString("notes");
        os.flush();
        os.close();
    }
}