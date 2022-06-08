package com.ventustium.MyLnList;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ventustium.MyLnList.UI.Account;
import com.ventustium.MyLnList.UI.Library;
import com.ventustium.MyLnList.UI.History;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Library library = new Library();
    History history = new History();
    Account account = new Account();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, library).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homeFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, library).commit();
                    break;
                case R.id.libraryFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, history).commit();
                    break;
                case R.id.accountFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, account).commit();
                    break;
            }
            return true;
        });
    }
}