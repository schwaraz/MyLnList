package com.ventustium.MyLnList.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ventustium.MyLnList.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Profile extends Fragment {
    GoogleSignInClient googleSignInClient;
    TextView stringName, stringEmail, stringUsername;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    String sessionID, StringUsername, StringEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Bundle bundle = this.getArguments();
        stringName = view.findViewById(R.id.stringName);
        stringEmail = view.findViewById(R.id.stringEmail);
        stringUsername = view.findViewById(R.id.stringUsername);
        stringName.setText(bundle.getString("getDisplayName"));
        stringEmail.setText(bundle.getString("getEmail"));
        Button buttonLogout = view.findViewById(R.id.signOutButton);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId().build();

        googleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), options);
        sessionID = bundle.getString("getID");
        StringEmail = bundle.getString("getEmail");
        buttonLogout.setOnClickListener(view1 -> {
            googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Sign Out Success", Toast.LENGTH_SHORT).show();
                    sessionID = "";
                    Log.d("SESSION ID", sessionID);
                    Fragment account = new Account();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, account).commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Sign Out Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
        clientPOST();
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
                        stringUsername.setText(StringUsername);
                    }
                });
            }
        });
    }

    public void getSessionID() throws Exception {
        String url = "https://api-mylnlist.ventustium.com/login/";
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
        jsonParam.put("googleID", sessionID);
        jsonParam.put("email", StringEmail);

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
        System.out.println("Data : \n" + response.toString());
        JSONArray myArray = new JSONArray(response.toString());
        JSONObject arrObj = myArray.getJSONObject(0);
//        System.out.println(arrObj.getInt("googleID"));
        sessionID = Integer.toString(arrObj.getInt("googleID"));
        StringUsername = arrObj.getString("username");
//        Log.d("SESSION ID", sessionID);
//        Log.d("SESSION ID", arrObj.getString("username"));
        os.flush();
        os.close();
    }
}