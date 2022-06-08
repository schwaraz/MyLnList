package com.ventustium.MyLnList.UI;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ventustium.MyLnList.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Account extends Fragment {
    View v;
    GoogleSignInClient googleSignInClient;
//    Profile profile = new Profile();

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_account, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handlerResult(task);
                }
            }
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId().build();

        googleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), options);
        SignInButton signInButton = v.findViewById(R.id.signInButton);
        Button buttonLogout = v.findViewById(R.id.signOutButton);
        signInButton.setOnClickListener(view1 -> {
            Intent intent = googleSignInClient.getSignInIntent();
            launcher.launch(intent);
        });

        buttonLogout.setOnClickListener(view1 -> {
            googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Sign Out Success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Sign Out Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account != null){
            LoggedIn(account);
        }
    }

    private void handlerResult(Task<GoogleSignInAccount> task) {
        task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
            @Override
            public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                LoggedIn(googleSignInAccount);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "FAILED", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoggedIn(GoogleSignInAccount googleSignInAccount){
        Fragment Profile = new Profile();
        Bundle bundle = new Bundle();
        bundle.putString("getDisplayName", googleSignInAccount.getDisplayName());
        bundle.putString("getEmail", googleSignInAccount.getEmail());
        Profile.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, Profile).commit();

    }

    public void clientPOST(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    getSessionID();
                } catch (Exception e){
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    public void getSessionID() throws Exception{
        String url = "https://api-mylnlist.ventustium.com/users/";
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
        jsonParam.put("username", "kevin");
        jsonParam.put("password", "value");
        jsonParam.put("email", "kevinlinuhung@gmail.com");

        System.out.println(jsonParam.toString());

        byte[] jsData = jsonParam.toString().getBytes("UTF-8");
        OutputStream os = con.getOutputStream();
        os.write(jsData);

        int responseCode = con.getResponseCode();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);

        os.flush();
        os.close();
    }
}