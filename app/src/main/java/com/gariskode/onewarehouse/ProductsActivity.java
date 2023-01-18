package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.gariskode.onewarehouse.adapter.ProductsAdapter;
import com.gariskode.onewarehouse.models.GetProductsModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {
    private RecyclerView rvHeroes;
   private ArrayList<GetProductsModels.Result> list = new ArrayList<>();

    public static final String EXTRA_CATEGORYID = "extra_categoryid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //get data intent
        int categoryid = getIntent().getIntExtra(EXTRA_CATEGORYID, 0);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        rvHeroes = findViewById(R.id.rv_heroes);
        rvHeroes.setHasFixedSize(true);



        ApiService.apiEndpoint().getProducts(token, categoryid).enqueue(new Callback<GetProductsModels>() {
            @Override
            public void onResponse(Call<GetProductsModels> call, Response<GetProductsModels> response) {
                Toast.makeText(ProductsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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