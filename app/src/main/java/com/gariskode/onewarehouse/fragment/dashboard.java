package com.gariskode.onewarehouse.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gariskode.onewarehouse.AddCategoryActivity;
import com.gariskode.onewarehouse.AddProductsActivity;
import com.gariskode.onewarehouse.CategoryActivity;
import com.gariskode.onewarehouse.InfoActivity;
import com.gariskode.onewarehouse.ProductsActivity;
import com.gariskode.onewarehouse.R;
import com.gariskode.onewarehouse.models.GetFinancesModels;
import com.gariskode.onewarehouse.models.ProfileModels;
import com.gariskode.onewarehouse.profileActivity;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dashboard extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Shared preferences
        SharedPreferences sh = getActivity().getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String token = sh.getString("token", "");

        TextView nameShop = view.findViewById(R.id.dashboard_NameShop);
        TextView address = view.findViewById(R.id.dashboard_address);

        CardView toProducts = view.findViewById(R.id.btn_toProducts);
        toProducts.setOnClickListener(this);

        CardView toAllCategory = view.findViewById(R.id.btn_toAllCategory);
        toAllCategory.setOnClickListener(this);

        CardView toAddCategory = view.findViewById(R.id.btn_toAddCategory);
        toAddCategory.setOnClickListener(this);

        CardView toAddProducts = view.findViewById(R.id.btn_toAddProducts);
        toAddProducts.setOnClickListener(this);

        CardView toMyWarehouse = view.findViewById(R.id.btn_toMyWarehouse);
        toMyWarehouse.setOnClickListener(this);

        CardView toInfo = view.findViewById(R.id.btn_to_info);
        toInfo.setOnClickListener(this);


        ApiService.apiEndpoint().profileApi(token).enqueue(new Callback<ProfileModels>() {
            @Override
            public void onResponse(Call<ProfileModels> call, Response<ProfileModels> response) {
                if (response.code() == 200) {
                    nameShop.setText(response.body().getResult().getName_shop());
                    address.setText(response.body().getResult().getAddress());
                }
            }

            @Override
            public void onFailure(Call<ProfileModels> call, Throwable t) {

            }
        });

        TextView noteKeuangan = view.findViewById(R.id.keuangan_note);
        TextView pemasukan = view.findViewById(R.id.pemasukan);
        pemasukan.setTextColor(Color.parseColor("#60DB68"));
        TextView pengeluaran = view.findViewById(R.id.pengeluaran);
        pengeluaran.setTextColor(Color.parseColor("#FF0000"));
        TextView selisih = view.findViewById(R.id.selisih);

           Integer year = LocalDate.now().getYear();
            Month month = LocalDate.now().getMonth();

            noteKeuangan.setText("Catatan Keuangan, "+ month.toString()+" "+year);


        ApiService.apiEndpoint().getFinanceSearch(token, year+"-"+month.getValue()).enqueue(new Callback<GetFinancesModels>() {
            @Override
            public void onResponse(Call<GetFinancesModels> call, Response<GetFinancesModels> response) {
                Integer incomeTemp = 0;
                Integer outcomeTemp = 0;
                for (int i = 0; i < response.body().getResult().size(); i++) {
                    if (response.body().getResult().get(i).getNote().equals("income")) {
                        incomeTemp = response.body().getResult().get(i).getAmount() + incomeTemp;
                    } else {
                        outcomeTemp = response.body().getResult().get(i).getAmount() + outcomeTemp;
                    }

                    //format Rupiah
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                    pemasukan.setText(formatRupiah.format(incomeTemp));
                    pengeluaran.setText(formatRupiah.format(outcomeTemp));

                    Integer selisihTemp = incomeTemp - outcomeTemp;
                    if (selisihTemp < 0){
                        selisih.setTextColor(Color.parseColor("#FF0000"));
                    }else{
                        selisih.setTextColor(Color.parseColor("#60DB68"));
                    }
                    selisih.setText("Selisih " + formatRupiah.format(selisihTemp));
                }

            }

            @Override
            public void onFailure(Call<GetFinancesModels> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_toProducts){
            Intent toProductsIntent = new Intent(getActivity(), ProductsActivity.class);
            startActivity(toProductsIntent);
        }
        if (view.getId() == R.id.btn_toAddCategory){
            Intent toAddCategoryIntent = new Intent(getActivity(), AddCategoryActivity.class);
            startActivity(toAddCategoryIntent);
        }
        if (view.getId() == R.id.btn_toAddProducts){
            Intent toAddProductsIntent = new Intent(getActivity(), AddProductsActivity.class);
            startActivity(toAddProductsIntent);
        }
        if (view.getId() == R.id.btn_toMyWarehouse){
            Intent toMyWarehouseIntent = new Intent(getActivity(), profileActivity.class);
            startActivity(toMyWarehouseIntent);
        }
        if (view.getId() == R.id.btn_toAllCategory){
            Intent toMyWarehouseIntent = new Intent(getActivity(), CategoryActivity.class);
            startActivity(toMyWarehouseIntent);
        }
        if (view.getId() == R.id.btn_to_info){
            Intent toMyWarehouseIntent = new Intent(getActivity(), InfoActivity.class);
            startActivity(toMyWarehouseIntent);
        }
    }
}