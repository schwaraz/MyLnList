package com.ventustium.MyLnList.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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


public class Profile extends Fragment {
    GoogleSignInClient googleSignInClient;
    TextView stringName, stringEmail, stringUsername;

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

        stringName.setText(bundle.getString("getDisplayName"));
        stringEmail.setText(bundle.getString("getEmail"));
        Button buttonLogout = view.findViewById(R.id.signOutButton);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId().build();

        googleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), options);
        buttonLogout.setOnClickListener(view1 -> {
            googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Sign Out Success", Toast.LENGTH_SHORT).show();
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

        return view;
    }
}