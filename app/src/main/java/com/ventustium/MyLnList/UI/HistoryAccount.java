package com.ventustium.MyLnList.UI;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class HistoryAccount extends Fragment {
    View v;
    GoogleSignInClient googleSignInClient;
//    Profile profile = new Profile();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        requireActivity().setTitle("History");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_history_account, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handlerResult(task);
                }
            }
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId().build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity().getApplicationContext(), options);
        SignInButton signInButton = v.findViewById(R.id.HistorySignInButton);
        signInButton.setOnClickListener(view1 -> {
            Intent intent = googleSignInClient.getSignInIntent();
            launcher.launch(intent);
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
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

    public void LoggedIn(GoogleSignInAccount googleSignInAccount) {
        Fragment Profile = new History();
        Bundle bundle = new Bundle();
        bundle.putString("getDisplayName", googleSignInAccount.getDisplayName());
        bundle.putString("getEmail", googleSignInAccount.getEmail());
        bundle.putString("getID", googleSignInAccount.getId());
        Profile.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, Profile).commit();
    }
}
