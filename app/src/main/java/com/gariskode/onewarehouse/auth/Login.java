package com.gariskode.onewarehouse.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gariskode.onewarehouse.Dashboard;
import com.gariskode.onewarehouse.R;
import com.gariskode.onewarehouse.models.LoginModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        TextView register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
            }
        });

        email = findViewById(R.id.email);
        email.setMaxLines(1);
        email.setSingleLine(true);
        email.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        password = findViewById(R.id.password);
        password.setMaxLines(1);
        password.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Button loginbtn = findViewById(R.id.loginbtn);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]+";
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText())) {
                    email.setError("Email Harus Diisi");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Email Tidak Valid");
                } else if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Password Harus Diisi");
                } else {
                    ApiLogin();
                }
            }
        });

    }

    void ApiLogin() {
        ApiService.apiEndpoint().loginApi(email.getText().toString(), password.getText().toString()).enqueue(new Callback<LoginModels>() {
            @Override
            public void onResponse(Call<LoginModels> call, Response<LoginModels> response) {
                if (response.body().getMesssage() != null) {
                System.out.println("TOKEN : " + response.body().getToken());
                    if (response.code() == 200) {
                        if (response.body().getMesssage().equals("loggedd In ")) {
                            //shared Preferences
                            SharedPreferences sharedPref = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPref.edit();
                            myEdit.putString("token", response.body().getToken());
                            myEdit.apply();

                            Toast.makeText(Login.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();

                            Intent intentToDashboard = new Intent(Login.this, Dashboard.class);
                            startActivity(intentToDashboard);
                            finish();
                        } else if (response.body().getMesssage().indexOf("email") != 0) {
                            email.setError(response.body().getMesssage());
                        } else if (response.body().getMesssage().indexOf("password") != 0) {
                            password.setError(response.body().getMesssage());
                        }
                    } else {
                        Toast.makeText(Login.this, "Terjadi kesalahan (Code: " + response.code() + " )", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(Login.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModels> call, Throwable t) {
                Toast.makeText(Login.this, t.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(t.toString());

            }
        });

    }
}