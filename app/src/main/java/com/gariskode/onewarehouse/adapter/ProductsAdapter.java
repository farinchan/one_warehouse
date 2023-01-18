package com.gariskode.onewarehouse.adapter;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gariskode.onewarehouse.EditProductsActivity;
import com.gariskode.onewarehouse.R;
import com.gariskode.onewarehouse.models.GetFinancesModels;
import com.gariskode.onewarehouse.models.GetProductsModels;
import com.gariskode.onewarehouse.models.ProductModels;
import com.gariskode.onewarehouse.retrofit.ApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ListViewHolder> {

    private ArrayList<GetProductsModels.Result> listProducts;

    public ProductsAdapter(ArrayList<GetProductsModels.Result> list) {
        this.listProducts = list;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        GetProductsModels.Result products = listProducts.get(position);
        holder.tvName.setText(products.getName());
        holder.tvDetail.setText(products.getDescription());
        holder.tvBarcode.setText(products.getBarcode());
        holder.tvSellingPrice.setText(products.getSelling_price().toString());
        holder.tvCapitalPrice.setText(products.getCapital_price().toString());
        holder.tvCategory.setText(products.getCategory_name());
        holder.cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(R.layout.delete_edit_bottom_sheet);
                LinearLayout edit = bottomSheetDialog.findViewById(R.id.edit);
                LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();

                        Intent toEdit = new Intent(view.getContext(), EditProductsActivity.class);
                        toEdit.putExtra(EditProductsActivity.EXTRA_PRODUCTID, products.getId());
                        toEdit.putExtra(EditProductsActivity.EXTRA_NAME, products.getName());
                        toEdit.putExtra(EditProductsActivity.EXTRA_DESCRIPTION, products.getDescription());
                        toEdit.putExtra(EditProductsActivity.EXTRA_BARCODE, products.getBarcode());
                        toEdit.putExtra(EditProductsActivity.EXTRA_STOCK, products.getStock());
                        toEdit.putExtra(EditProductsActivity.EXTRA_SELLINGPRICE, products.getSelling_price());
                        toEdit.putExtra(EditProductsActivity.EXTRA_CAPITALPRICE, products.getCapital_price());
                        toEdit.putExtra(EditProductsActivity.EXTRA_CATEGORYID, products.getCategory_id());
                        view.getContext().startActivity(toEdit);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View view) {
                        //shared preferences - token login
                        SharedPreferences sh = view.getContext().getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
                        String token = sh.getString("token", "");
                        ApiService.apiEndpoint().deleteProduct(token, products.getId()).enqueue(new Callback<ProductModels>() {
                            @Override
                            public void onResponse(Call<ProductModels> call, Response<ProductModels> response) {
                                if (response.code() == 200){
                                    Toast.makeText(view.getContext(), "Barang Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    listProducts.remove(holder.getLayoutPosition());
                                    notifyItemRemoved(holder.getLayoutPosition());
                                }
                            }

                            @Override
                            public void onFailure(Call<ProductModels> call, Throwable t) {

                            }
                        });
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetail, tvBarcode, tvSellingPrice, tvCapitalPrice, tvCategory;
        CardView cardProducts;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            cardProducts = itemView.findViewById(R.id.card_products);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDetail = itemView.findViewById(R.id.tv_item_detail);
            tvBarcode = itemView.findViewById(R.id.tv_item_barcode);
            tvSellingPrice = itemView.findViewById(R.id.tv_item_selling_price);
            tvCapitalPrice = itemView.findViewById(R.id.tv_item_capital_price);
            tvCategory = itemView.findViewById(R.id.tv_item_category);
        }
    }
}
