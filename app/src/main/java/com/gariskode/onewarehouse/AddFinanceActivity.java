package com.gariskode.onewarehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.gariskode.onewarehouse.auth.Login;
import com.gariskode.onewarehouse.models.RegisterModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFinanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final Calendar myCalendar= Calendar.getInstance();
    EditText datefinances;

    Spinner kodeKursus;
    String noteFinances;
    private static final String[] paths = {"income", "outcome"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        datefinances = findViewById(R.id.date_finances);
        updateLabel();

        //Date Picker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        //Button Date Picker
        ImageButton datePickerButton = findViewById(R.id.date_picker);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddFinanceActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Spinner List
        kodeKursus = (Spinner)findViewById(R.id.finances_note);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddFinanceActivity.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kodeKursus.setAdapter(adapter);
        kodeKursus.setOnItemSelectedListener(this);

        //shared preferences - token login
        SharedPreferences sh = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        EditText financesName = findViewById(R.id.finances_name);
        EditText financesDescription = findViewById(R.id.finances_description);
        EditText financesAmount = findViewById(R.id.finances_amount);

        //Button add Finances
        Button addFinances = findViewById(R.id.btn_add_finances);
        addFinances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(financesName.getText())){
                    financesName.setError("Field Ini Harus diisi");
                }else if (TextUtils.isEmpty(financesDescription.getText())){
                    financesDescription.setError("Field Ini Harus diisi");
                }else if (TextUtils.isEmpty(financesAmount.getText())){
                    financesAmount.setError("Field Ini Harus diisi");
                }else{
                    ApiService.apiEndpoint().addFinanse(token, financesName.getText().toString(), financesDescription.getText().toString(), datefinances.getText().toString(), Integer.valueOf(financesAmount.getText().toString()) , noteFinances).enqueue(new Callback<RegisterModels>() {
                        @Override
                        public void onResponse(Call<RegisterModels> call, Response<RegisterModels> response) {
                            if (response.code() == 200){
                                if (response.body().getMessage().equals("success")){
                                    Toast.makeText(AddFinanceActivity.this, "Data Barhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                Toast.makeText(AddFinanceActivity.this, "Terjadi kesalahan (Code: " + response.code() + " )", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterModels> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        datefinances.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                noteFinances = "income";
                break;
            case 1:
                noteFinances = "outcome";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}