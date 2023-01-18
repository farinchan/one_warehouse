package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gariskode.onewarehouse.auth.Login;
import com.gariskode.onewarehouse.models.RegisterModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {

    EditText EtCategoryName;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Tambah Kategori");
        setContentView(R.layout.activity_add_category);

        EtCategoryName = findViewById(R.id.et_Category);
        btnAdd = findViewById(R.id.btn_addCategory);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(EtCategoryName.getText())){
                    EtCategoryName.setError("Field Ini Harus diisi");
                }else{
                    ApiService.apiEndpoint().addCategory(token, EtCategoryName.getText().toString(), EtCategoryName.getText().toString().toLowerCase().replaceAll("[^a-z0-9-]", "")).enqueue(new Callback<RegisterModels>() {
                        @Override
                        public void onResponse(Call<RegisterModels> call, Response<RegisterModels> response) {
                            if (response.code() == 200){
                                Toast.makeText(AddCategoryActivity.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddCategoryActivity.this, "Terjadi kesalahan (Code: " + response.code() + " )", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterModels> call, Throwable t) {
                            Toast.makeText(AddCategoryActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }
}