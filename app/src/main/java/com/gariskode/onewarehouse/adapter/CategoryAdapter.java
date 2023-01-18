package com.gariskode.onewarehouse.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gariskode.onewarehouse.ProductsActivity;
import com.gariskode.onewarehouse.R;
import com.gariskode.onewarehouse.models.GetCategory;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ListViewHolder> {

    private ArrayList<GetCategory.Result> listCategory;
    public CategoryAdapter(ArrayList<GetCategory.Result> list) {
        this.listCategory = list;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        GetCategory.Result Category = listCategory.get(position);
        holder.tvName.setText(Category.getName() + " - ");
        holder.tvDetail.setText(Category.getId().toString());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveWithDataIntent = new Intent(view.getContext(), ProductsActivity.class);
                moveWithDataIntent.putExtra(ProductsActivity.EXTRA_CATEGORYID, Integer.valueOf(holder.tvDetail.getText().toString()));
                view.getContext().startActivity(moveWithDataIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class ListViewHolder  extends RecyclerView.ViewHolder {
        TextView tvName, tvDetail;
        CardView card;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDetail = itemView.findViewById(R.id.tv_item_detail);

            card = itemView.findViewById(R.id.card_to_products);
        }
    }
}
