package com.cmput301f20t21.bookfriends;

import android.content.Intent;
import android.os.Bundle;

import com.cmput301f20t21.bookfriends.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    // saved for later when MainActivity is starting another activity
//    public enum ActivityRequestCode {
//
//        private final int requestCode;
//
//        ActivityRequestCode(int requestCode) {
//            this.requestCode = requestCode;
//        }
//
//        public int getRequestCode() {
//            return this.requestCode;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_library,
                R.id.navigation_borrow,
                R.id.navigation_notifications,
                R.id.navigation_profile
        )
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // this intent should contain the user class that is used to log in?
        // Intent userIntent = getIntent();
    }
}