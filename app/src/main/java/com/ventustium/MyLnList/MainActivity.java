package com.ventustium.MyLnList;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.*;

public class MainActivity extends AppCompatActivity {
    Button button;
    ListView mylv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mylv =  findViewById(R.id.lv1);
        button = findViewById(R.id.button1);
        button.setOnClickListener(view -> new HTTPReqTask().execute());

        Log.d("TAG", "Function has generated zero.");
    }

    private String[] getREST() throws Exception{
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
        while ((input=in.readLine())!=null){
            response.append(input);
        }
        in.close();
        System.out.println("Data : \n" + response);

        JSONArray myArray = new JSONArray(response.toString());
        ArrayList<String> result = new ArrayList<>();
        for (int i=0; i < myArray.length();i++){
            JSONObject arrObj = myArray.getJSONObject(i);
            System.out.println("Title : " + arrObj.getString("title"));
            result.add("Title : " + arrObj.getString("title"));
        }
        return result.toArray(new String[0]);
    }

    private void updateInterface(String[] result) {
        ArrayAdapter<String> aAdapter = new ArrayAdapter<>(this, R.layout.listview1, R.id.tv1, result);
        mylv.setAdapter(aAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private class HTTPReqTask extends AsyncTask<String, Void, String[]> { //String pertama do in background, string ke tiga, on
        @Override
        protected String[] doInBackground(String... strings) {
            String[] result = null;
            try {
                result = getREST();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
            }

        @Override
        protected void onPostExecute(String[] result) {
            updateInterface(result);
            super.onPostExecute(result);
        }
    }
}