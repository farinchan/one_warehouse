package com.gariskode.onewarehouse.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gariskode.onewarehouse.AddFinanceActivity;
import com.gariskode.onewarehouse.R;
import com.gariskode.onewarehouse.adapter.FinancesAdapter;
import com.gariskode.onewarehouse.models.GetFinancesModels;
import com.gariskode.onewarehouse.retrofit.ApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Money extends Fragment {
    private RecyclerView rvFinances;
    private ArrayList<GetFinancesModels.Result> list = new ArrayList<>();
    String dateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView toAddMoney = view.findViewById(R.id.toAddMoneyPage);
        toAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginPage = new Intent(getActivity(), AddFinanceActivity.class);
                startActivity(loginPage);
            }
        });

        TextView datetimeName = view.findViewById(R.id.date_time_name);
        Integer year = LocalDate.now().getYear();
        Integer month = LocalDate.now().getMonth().getValue();
        Integer date = LocalDate.now().getEra().getValue();

        //shared preferences - token login
        SharedPreferences sh = getActivity().getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        dateTime = year+"-"+month;
        api(token);

        LinearLayout selectDate = view.findViewById(R.id.select_date);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(R.layout.finances_bottom_sheet);
                TextView hariIni = bottomSheetDialog.findViewById(R.id.hari_ini);
                TextView kemarin = bottomSheetDialog.findViewById(R.id.kemarin);
                TextView bulanIni = bottomSheetDialog.findViewById(R.id.bulan_ini);
                TextView semua = bottomSheetDialog.findViewById(R.id.semua);
                TextView pilihTanggal = bottomSheetDialog.findViewById(R.id.pilih_tanggal);

                hariIni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dateTime = String.valueOf(LocalDate.now());
                        api(token);
                        datetimeName.setText("Hari Ini");
                        bottomSheetDialog.dismiss();
                    }
                });
                kemarin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dateTime = year+"-"+month+"-"+(date-1);
                        api(token);
                        datetimeName.setText("Kemarin");
                        bottomSheetDialog.dismiss();
                    }
                });
                bulanIni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dateTime = year+"-"+month;
                        api(token);
                        datetimeName.setText("Bulan Ini");
                        bottomSheetDialog.dismiss();
                    }
                });

                semua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dateTime = "";
                        api(token);
                        datetimeName.setText("Semua");
                        bottomSheetDialog.dismiss();
                    }
                });

                pilihTanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });




        rvFinances = view.findViewById(R.id.rv_finances);
        rvFinances.setHasFixedSize(true);


    }
    private void api(String token){
        list.clear();
        ApiService.apiEndpoint().getFinanceSearch(token, dateTime).enqueue(new Callback<GetFinancesModels>() {
            @Override
            public void onResponse(Call<GetFinancesModels> call, Response<GetFinancesModels> response) {
                // Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.code() == 200){
                    list.addAll(response.body().getResult());
                    Collections.reverse(list);
                    showRecyclerList();
                }

            }

            @Override
            public void onFailure(Call<GetFinancesModels> call, Throwable t) {

            }
        });
    }

    private void showRecyclerList(){
        rvFinances.setLayoutManager(new LinearLayoutManager(getContext()));
        FinancesAdapter financesAdapter = new FinancesAdapter(list);
        rvFinances.setAdapter(financesAdapter);
    }

}