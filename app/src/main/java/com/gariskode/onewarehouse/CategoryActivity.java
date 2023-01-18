package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.gariskode.onewarehouse.adapter.CategoryAdapter;
import com.gariskode.onewarehouse.models.GetCategory;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView rvHeroes;
    private ArrayList<GetCategory.Result> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        rvHeroes = findViewById(R.id.rv_heroes);
        rvHeroes.setHasFixedSize(true);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        ApiService.apiEndpoint().getCategoryApi(token).enqueue(new Callback<GetCategory>() {
            @Override
            public void onResponse(Call<GetCategory> call, Response<GetCategory> response) {
                Toast.makeText(CategoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.code() == 200){
                    list.addAll(response.body().getResults());
                    showRecyclerList();
                }

            }

            @Override
            public void onFailure(Call<GetCategory> call, Throwable t) {

            }
        });

    }
    private void showRecyclerList(){
        rvHeroes.setLayoutManager(new LinearLayoutManager(this));
        CategoryAdapter categoryAdapter = new CategoryAdapter(list);
        rvHeroes.setAdapter(categoryAdapter);
    }
}