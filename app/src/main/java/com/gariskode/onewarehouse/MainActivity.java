package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.gariskode.onewarehouse.auth.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");
        //System.out.println( "TEST : " + token);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setelah loading maka akan langsung berpindah ke Login activity
                if (token.isEmpty()){
                    Intent loginPage = new Intent(MainActivity.this, Login.class);
                    startActivity(loginPage);
                }else {
                    Intent homePage = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(homePage);
                }
                finish();
            }
        },4000);

    }

}