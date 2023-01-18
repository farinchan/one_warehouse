package com.gariskode.onewarehouse.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gariskode.onewarehouse.R;
import com.gariskode.onewarehouse.models.FinancesModels;
import com.gariskode.onewarehouse.models.GetFinancesModels;
import com.gariskode.onewarehouse.retrofit.ApiService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancesAdapter extends RecyclerView.Adapter<FinancesAdapter.ListViewHolder> {

    private final ArrayList<GetFinancesModels.Result> listFinances;

    public FinancesAdapter(ArrayList<GetFinancesModels.Result> listFinances) {
        this.listFinances = listFinances;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvDate, tvAmount;
        CardView cardFinance;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDescription = itemView.findViewById(R.id.tv_deskripsi_finance);
            tvDate = itemView.findViewById(R.id.tv_date_finance);
            tvAmount = itemView.findViewById(R.id.tv_amount_finance);

            cardFinance = itemView.findViewById(R.id.card_finance);
        }
    }

    @NonNull
    @Override
    public FinancesAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finances, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancesAdapter.ListViewHolder holder, int position) {
        GetFinancesModels.Result Finances = listFinances.get(position);
        holder.tvName.setText(Finances.getName());
        holder.tvDescription.setText(Finances.getDescription());
        holder.tvDate.setText(Finances.getDate());

        //format Rupiah
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String amount = formatRupiah.format(Finances.getAmount());

        if (Finances.getNote().equals("income")) {
            holder.tvAmount.setTextColor(Color.parseColor("#60DB68"));
            holder.tvAmount.setText("+ " + amount);
        } else {
            holder.tvAmount.setTextColor(Color.parseColor("#FF0000"));
            holder.tvAmount.setText("- " + amount);
        }

        holder.cardFinance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.delete_bottom_sheet);

                //Membuat dialog agar berukuran responsive
                DisplayMetrics metrics = view.getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout delete = dialog.findViewById(R.id.delete);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //shared preferences - token login
                        SharedPreferences sh = view.getContext().getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
                        String token = sh.getString("token", "");

                        ApiService.apiEndpoint().deleteFinance(token, Finances.getId()).enqueue(new Callback<FinancesModels>() {
                            @Override
                            public void onResponse(Call<FinancesModels> call, Response<FinancesModels> response) {
                                if (response.code() == 200){
                                    listFinances.remove(holder.getLayoutPosition());
                                    notifyItemRemoved(holder.getLayoutPosition());
                                    Toast.makeText(view.getContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(view.getContext(), "Terjadi Kesalahan (Code: "+ response.code() +")", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<FinancesModels> call, Throwable t) {

                            }
                        });
                    }
                });

                dialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFinances.size();
    }


}
