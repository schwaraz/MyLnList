package com.ventustium.MyLnList.NovelDetailFragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ventustium.MyLnList.R;

import java.util.Objects;

public class NovelSignInGoogle extends Fragment {
    View view;
    Bundle bundle;
    GoogleSignInClient googleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();

        requireActivity().setTitle(bundle.getString("LNTitle"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_novel_detail_sign_in_google, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handlerResult(task);
            }
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId().build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity().getApplicationContext(), options);
        SignInButton signInButton = view.findViewById(R.id.novelDetailSignInButton);
        signInButton.setOnClickListener(view1 -> {
            Intent intent = googleSignInClient.getSignInIntent();
            launcher.launch(intent);
        });
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
                Toast.makeText(getContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoggedIn(GoogleSignInAccount googleSignInAccount) {
        Toast.makeText(getContext(), "Sign In Success", Toast.LENGTH_SHORT).show();
        Fragment NovelNoHistory = new NovelNoHistory();
        NovelNoHistory.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDetailNovel, NovelNoHistory).commit();
    }
}