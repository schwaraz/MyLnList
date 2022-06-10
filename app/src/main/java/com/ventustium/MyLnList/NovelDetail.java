package com.ventustium.MyLnList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ventustium.MyLnList.NovelDetailFragment.NovelHistory;
import com.ventustium.MyLnList.NovelDetailFragment.NovelNoHistory;
import com.ventustium.MyLnList.NovelDetailFragment.NovelSignInGoogle;
import com.ventustium.MyLnList.NovelDetailFragment.UserNovelHistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
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

public class NovelDetail extends AppCompatActivity {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    Bundle bundle;
    String notes;
    String id, LNid,idLN, volume, chapter, score;
    String googleID, status;
    TextView tvlnDescription;
    NovelNoHistory novelNoHistory = new NovelNoHistory();
    NovelHistory novelHistory = new NovelHistory();
    NovelSignInGoogle novelSignInGoogle = new NovelSignInGoogle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_detail);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        tvlnDescription = findViewById(R.id.tvStringDescription);
        bundle = new Bundle();
        Intent intent = this.getIntent();
        if (intent != null){
            LNid = Integer.toString(intent.getIntExtra("LNId", 0));
            Log.d("NovelDetail", LNid);
            bundle.putString("LNTitle", intent.getStringExtra("LNTitle"));
            bundle.putString("LNId", LNid);
            tvlnDescription.setText(intent.getStringExtra("LNDescription"));

            clientPost();

            if (account != null) {
                bundle.putString("IdGoogle", account.getId());
                novelNoHistory.setArguments(bundle);
                clientPost();
            }

            if (account == null) {
                novelSignInGoogle.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDetailNovel, novelSignInGoogle).commit();

            }
        }
    }

    private void clientPost() {
        executor.execute(() -> {
            try {
                getUserHistoryNovelDetail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(() -> {
//                Log.d("Kevin", notes);
                if(Objects.equals(notes, "NO DATA")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDetailNovel, novelNoHistory).commit();
                }
                if(Objects.equals(notes, "DATA")) {
                    novelHistory.setArguments(bundle);
                    Log.d("Kevin1", bundle.getString("LNTitle"));
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDetailNovel, novelHistory).commit();
                }
            });
        });
    }

    private void getUserHistoryNovelDetail() throws Exception, JSONException {
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
        jsonParam.put("idLN", LNid);
        jsonParam.put("googleID", bundle.getString("IdGoogle"));


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
        try {
            getData(response);
        }catch (Exception e){
            e.printStackTrace();
        }

        in.close();
        Log.d("Kevin","LN List : \n" + response);
        Log.d("Kevin", notes);
        os.flush();
        os.close();
    }

    public JSONObject getData(StringBuilder response) throws JSONException{
        JSONArray myArray = new JSONArray(response.toString());
        JSONObject arrObj = myArray.getJSONObject(0);
        UserNovelHistoryModel u = new UserNovelHistoryModel();
        notes = (arrObj.getString("notes"));
        return arrObj;
    }
}