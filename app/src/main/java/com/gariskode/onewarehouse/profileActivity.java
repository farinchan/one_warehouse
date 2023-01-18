package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gariskode.onewarehouse.models.ProfileModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        ImageView back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentBack = new Intent(profileActivity.this, Dashboard.class);
//                startActivity(intentBack);
                finish();
            }
        });

        TextView nameShop = findViewById(R.id.profile_nameShop1);
        TextView ProfileEmail = findViewById(R.id.profile_email);
        TextView ProfileNameshop = findViewById(R.id.profile_nameShop);
        TextView ProfileAddress = findViewById(R.id.profile_address);
        TextView ProfileCreatedAt = findViewById(R.id.profile_createdAt);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        //format timestamp
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");



        ApiService.apiEndpoint().profileApi(token).enqueue(new Callback<ProfileModels>() {
            @Override
            public void onResponse(Call<ProfileModels> call, Response<ProfileModels> response) {
                if (response.code() == 200){
                    nameShop.setText(response.body().getResult().getName_shop());
                    ProfileEmail.setText(response.body().getResult().getEmail());
                    ProfileNameshop.setText(response.body().getResult().getName_shop());
                    ProfileAddress.setText(response.body().getResult().getAddress());

                    Timestamp stamp = new Timestamp(response.body().getResult().getCreated_at().getTime());
                    Date date = new Date(stamp.getTime());
                    ProfileCreatedAt.setText( date.toString() );

                }
            }

            @Override
            public void onFailure(Call<ProfileModels> call, Throwable t) {
                Toast.makeText(profileActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();

            }
        });


    }
}