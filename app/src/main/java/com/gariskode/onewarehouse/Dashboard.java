package com.gariskode.onewarehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gariskode.onewarehouse.auth.Login;
import com.gariskode.onewarehouse.fragment.Money;
import com.gariskode.onewarehouse.fragment.dashboard;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MenuView menuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottomnavigationbar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new dashboard()).commit();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId())
                {
                    case R.id.mdashboard:  temp = new dashboard();
                        break;
                    case R.id.mMoney:  temp = new Money();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,temp).commit();
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrScanner = new Intent(Dashboard.this, SearchActivity.class);
                startActivity(qrScanner);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }
    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.myShop:
                Intent myShopPage = new Intent(Dashboard.this, profileActivity.class);
                startActivity(myShopPage);

                break;
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
                preferences.edit().remove("token").commit();

                Intent loginPage = new Intent(Dashboard.this, Login.class);
                startActivity(loginPage);
                finish();
                break;
        }
    }
}