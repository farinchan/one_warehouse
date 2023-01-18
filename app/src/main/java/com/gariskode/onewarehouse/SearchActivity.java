package com.gariskode.onewarehouse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gariskode.onewarehouse.adapter.ProductsAdapter;
import com.gariskode.onewarehouse.models.GetProductsModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ImageButton btnScan;
    private TextView resultText;

    private RecyclerView rvHeroes;
    private ArrayList<GetProductsModels.Result> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btnScan = findViewById(R.id.btn_scan);
        resultText = findViewById(R.id.et_barcode_scanner);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnScan.setOnClickListener(s -> {
            if (ContextCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(SearchActivity.this, Manifest.permission.CAMERA)){
                    startScan();
                } else {
                    ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
            } else {
                startScan();
            }
        });

        rvHeroes = findViewById(R.id.rv_heroes);
        rvHeroes.setHasFixedSize(true);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        resultText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    api(token, charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void startScan(){
        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
        startActivityForResult(intent, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20){
            if (resultCode == RESULT_OK && data!=null){
                String code = data.getStringExtra("result");
                resultText.setText(code);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScan();
            } else {
                Toast.makeText(this, "Gagal membuka kamera!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private  void api(String token, String search){
        list.clear();
        ApiService.apiEndpoint().getSearchProducts(token, search).enqueue(new Callback<GetProductsModels>() {
            @Override
            public void onResponse(Call<GetProductsModels> call, Response<GetProductsModels> response) {
               // Toast.makeText(SearchActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    list.addAll(response.body().getResult());
                    showRecyclerList();
                }
            }

            @Override
            public void onFailure(Call<GetProductsModels> call, Throwable t) {

            }
        });
    }
    private void showRecyclerList(){
        rvHeroes.setLayoutManager(new LinearLayoutManager(this));
        ProductsAdapter productsAdapter = new ProductsAdapter(list);
        rvHeroes.setAdapter(productsAdapter);
    }
}