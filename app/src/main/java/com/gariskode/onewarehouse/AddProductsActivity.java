package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gariskode.onewarehouse.auth.Login;
import com.gariskode.onewarehouse.models.GetCategory;
import com.gariskode.onewarehouse.models.ProductModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductsActivity extends AppCompatActivity {
    Spinner category;
    List<String> paths =new ArrayList<String>();
    List<Integer> pathsId =new ArrayList<Integer>();

    EditText namaBarang, deskripsi, barcode , stok, hargaJual, hargaBeli;
    Button addProduct;
    Integer categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);



        namaBarang = findViewById(R.id.et_nama_barang);
        deskripsi = findViewById(R.id.et_deskripsi);
        barcode = findViewById(R.id.et_barcode);
        stok = findViewById(R.id.et_stok);
        hargaJual = findViewById(R.id.et_harga_jual);
        hargaBeli = findViewById(R.id.et_harga_modal);
        addProduct = findViewById(R.id.btn_add_product);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        ApiService.apiEndpoint().getCategoryApi(token).enqueue(new Callback<GetCategory>() {
            @Override
            public void onResponse(Call<GetCategory> call, Response<GetCategory> response) {
//                Toast.makeText(CategoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.code() == 200){
                    for (int i = 0; i < response.body().getResults().size(); i++){
                        paths.add(response.body().getResults().get(i).getName());
                        pathsId.add(response.body().getResults().get(i).getId());
                    }
                    category = (Spinner)findViewById(R.id.spiner_category);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddProductsActivity.this,
                            android.R.layout.simple_spinner_item, paths);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    category.setAdapter(adapter);
                    category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            categoryId = pathsId.get(i);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Toast.makeText(AddProductsActivity.this, "Terjadi kesalahan (Code: " + response.code() + " )", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategory> call, Throwable t) {

            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(namaBarang.getText())){
                    namaBarang.setError("Field Ini Harus diisi");
                }else if (TextUtils.isEmpty(deskripsi.getText())){
                    deskripsi.setError("Field Ini Harus diisi");
                }else if (TextUtils.isEmpty(barcode.getText())){
                    barcode.setError("Field Ini Harus diisi");
                }else if (TextUtils.isEmpty(stok.getText())){
                    stok.setError("Field Ini Harus diisi");
                }else if (TextUtils.isEmpty(hargaJual.getText())){
                    hargaJual.setError("Field Ini Harus diisi");
                }else if (TextUtils.isEmpty(hargaBeli.getText())){
                    hargaBeli.setError("Field Ini Harus diisi");
                }else{
                    ApiService.apiEndpoint().addProduct(token, namaBarang.getText().toString(), deskripsi.getText().toString(), barcode.getText().toString(),Integer.valueOf(stok.getText().toString()) , Integer.valueOf(hargaJual.getText().toString()) ,Integer.valueOf(hargaBeli.getText().toString()) , categoryId).enqueue(new Callback<ProductModels>() {
                        @Override
                        public void onResponse(Call<ProductModels> call, Response<ProductModels> response) {
                            if (response.code() == 200){
                                Toast.makeText(AddProductsActivity.this, "barang berhasil Ditmbah", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddProductsActivity.this, "Terjadi kesalahan (Code: " + response.code() + " )", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ProductModels> call, Throwable t) {

                        }
                    });

                }
            }
        });
    }


}