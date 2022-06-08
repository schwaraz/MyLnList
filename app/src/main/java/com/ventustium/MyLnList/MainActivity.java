package com.ventustium.MyLnList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ventustium.MyLnList.UI.Account;
import com.ventustium.MyLnList.UI.Home;
import com.ventustium.MyLnList.UI.Library;
import com.ventustium.MyLnList.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Home home = new Home();
    Library library = new Library();
    Account account = new Account();

    ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,home).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homeFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, home).commit();
                    break;
                case R.id.libraryFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, library).commit();
                    break;
                case R.id.accountFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, account).commit();
                    break;
            }
            return false;
        });
    }
}