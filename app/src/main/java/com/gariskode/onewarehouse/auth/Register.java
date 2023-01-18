package com.gariskode.onewarehouse.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gariskode.onewarehouse.R;
import com.gariskode.onewarehouse.models.LoginModels;
import com.gariskode.onewarehouse.models.RegisterModels;
import com.gariskode.onewarehouse.retrofit.ApiService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    EditText email, password, nameShop, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

         email = findViewById(R.id.email);
         password = findViewById(R.id.password);
         nameShop = findViewById(R.id.nameShop);
         address = findViewById(R.id.address);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]+";
        Button registerButton = findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
//                builder.setMessage("test")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // START THE GAME!
//                            }
//                        })
//                        .setIcon(R.drawable.ic_logout)

                if (TextUtils.isEmpty(email.getText())) {
                    email.setError("Email Harus Diisi");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Email Tidak Valid");
                } else if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Password Harus Diisi");
                }else if (TextUtils.isEmpty(nameShop.getText())){
                    nameShop.setError("Nama Toko harus diisi");
                }else if (TextUtils.isEmpty(address.getText())){
                    address.setError("Alamat harus diisi");
                }else {
                    ApiLogin();
                }

            }
        });

        TextView login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginPage = new Intent(Register.this, Login.class);
                startActivity(toLoginPage);
                finish();
            }
        });
    }
    void ApiLogin(){
        ApiService.apiEndpoint().registerApi(email.getText().toString(), password.getText().toString(), nameShop.getText().toString(), address.getText().toString()).enqueue(new Callback<RegisterModels>() {
            @Override
            public void onResponse(Call<RegisterModels> call, Response<RegisterModels> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("User added successfully")) {
                        Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent toLoginPage = new Intent(Register.this, Login.class);
                        startActivity(toLoginPage);
                        finish();
                    } else if ( response.body().getMessage().indexOf("email") != 0){
                        email.setError(response.body().getMessage());
                    }else if ( response.body().getMessage().indexOf("password") != 0){
                        password.setError(response.body().getMessage());
                    }else if ( response.body().getMessage().indexOf("nameShop") != 0){
                        nameShop.setError(response.body().getMessage());
                    }else if ( response.body().getMessage().indexOf("address") != 0){
                        address.setError(response.body().getMessage());
                    }
                } else{
                    Toast.makeText(Register.this, "Terjadi kesalahan (Code: " + response.code() + " )", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterModels> call, Throwable t) {
                Toast.makeText(Register.this, "Error : " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}