package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gariskode.onewarehouse.models.GetCategory;
import com.gariskode.onewarehouse.models.ProductModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductsActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCTID = "extra_productid";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_BARCODE = "extra_barcode";
    public static final String EXTRA_STOCK = "extra_stock";
    public static final String EXTRA_SELLINGPRICE = "extra_sellingPrice";
    public static final String EXTRA_CAPITALPRICE = "extra_capitalPrice";
    public static final String EXTRA_CATEGORYID = "extra_categoryId";

    Spinner category;
    List<String> paths =new ArrayList<String>();
    List<Integer> pathsId =new ArrayList<Integer>();

    EditText namaBarang, deskripsi, barcode , stok, hargaJual, hargaBeli;
    Button addProduct;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);



        namaBarang = findViewById(R.id.et_nama_barang);
        deskripsi = findViewById(R.id.et_deskripsi);
        barcode = findViewById(R.id.et_barcode);
        stok = findViewById(R.id.et_stok);
        hargaJual = findViewById(R.id.et_harga_jual);
        hargaBeli = findViewById(R.id.et_harga_modal);
        addProduct = findViewById(R.id.btn_add_product);

        int productId = getIntent().getIntExtra(EXTRA_PRODUCTID, 0);
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String description = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        String barcodes = getIntent().getStringExtra(EXTRA_BARCODE);
        int stock = getIntent().getIntExtra(EXTRA_STOCK, 0);
        int sellingPrice = getIntent().getIntExtra(EXTRA_SELLINGPRICE, 0);
        int capitalPrice = getIntent().getIntExtra(EXTRA_CAPITALPRICE,0);
        int categoryIds = getIntent().getIntExtra(EXTRA_CATEGORYID, 0);

        namaBarang.setText(name);
        deskripsi.setText(description);
        barcode.setText(String.valueOf(barcodes));
        stok.setText(String.valueOf(stock));
        hargaJual.setText(String.valueOf(sellingPrice));
        hargaBeli.setText(String.valueOf(capitalPrice));
        categoryId = categoryIds;

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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProductsActivity.this,
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
                            categoryId = categoryIds;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetCategory> call, Throwable t) {

            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiEndpoint().editProduct(token, productId, namaBarang.getText().toString(), deskripsi.getText().toString(), barcode.getText().toString(),Integer.valueOf(stok.getText().toString()) , Integer.valueOf(hargaJual.getText().toString()) ,Integer.valueOf(hargaBeli.getText().toString()) , categoryId).enqueue(new Callback<ProductModels>() {
                    @Override
                    public void onResponse(Call<ProductModels> call, Response<ProductModels> response) {
                        if (response.code() == 200){
                            Toast.makeText(EditProductsActivity.this, "barang berhasil diupdate", Toast.LENGTH_SHORT).show();
                            finish();

                        }else{
                            Toast.makeText(EditProductsActivity.this, "Terjadi Kesalahan (Code: "+ response.code() +")", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ProductModels> call, Throwable t) {

                    }
                });
            }
        });
    }
}