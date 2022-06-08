package com.ventustium.MyLnList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ventustium.MyLnList.NovelDetailFragment.NovelNoHistory;
import com.ventustium.MyLnList.NovelDetailFragment.NovelSignInGoogle;

public class NovelDetail extends AppCompatActivity {
    NovelNoHistory novelNoHistory = new NovelNoHistory();
    NovelSignInGoogle novelSignInGoogle = new NovelSignInGoogle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_detail);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        
        Intent intent = this.getIntent();
        if (intent != null){
            Bundle bundle = new Bundle();
            bundle.putString("LNTitle", intent.getStringExtra("LNTitle"));


            if (account != null) {
                novelNoHistory.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDetailNovel, novelNoHistory).commit();
            }

            if (account == null) {
                novelSignInGoogle.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDetailNovel, novelSignInGoogle).commit();

            }


        }

    }
}