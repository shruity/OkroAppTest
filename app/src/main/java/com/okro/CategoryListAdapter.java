package com.okro;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>{

    private Context context;

    List<CategoryData> categoryData = new ArrayList<>();

    public CategoryListAdapter(Context context, ArrayList<CategoryData> categoryData) {
        this.context = context;
        this.categoryData = categoryData;
        this.setHasStableIds(true);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final List<CategoryData> categoryDataList = categoryData;

        Typeface nexaLight = FontManager.getNexaLight(context);
        Typeface nexaBold  = FontManager.getNexaBold(context);

        holder.tvCat.setTypeface(nexaLight);
        holder.tvQty.setTypeface(nexaBold);
        holder.tvQuantity.setTypeface(nexaLight);
        holder.tvPrice.setTypeface(nexaLight);

        if (categoryDataList.get(position).getCatName() != null && !categoryDataList.get(position).getCatName().isEmpty()
                && !categoryDataList.get(position).getCatName().equals("")){
            holder.tvCat.setText(categoryDataList.get(position).getCatName());
        }

        if (categoryDataList.get(position).getQuantity() != null && !categoryDataList.get(position).getQuantity().isEmpty()
                && !categoryDataList.get(position).getQuantity().equals("")){
            holder.tvQuantity.setText(categoryDataList.get(position).getQuantity());
        }

        if (categoryDataList.get(position).getPrice() != null && !categoryDataList.get(position).getPrice().isEmpty()
                && !categoryDataList.get(position).getPrice().equals("")){
            holder.tvPrice.setText(categoryDataList.get(position).getPrice());
        }

        Picasso.with(context).load(categoryDataList.get(position).getImage()).
                error(R.drawable.ic_launcher_foreground).into(holder.ivIcon);

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.tvQty.getText().toString().trim());
                if (quantity < 20) {
                    quantity = quantity + 1;
                    holder.tvQty.setText(String.valueOf(quantity));
                }
                else {
                    Toast.makeText(context, "Cannot add more than 20!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.tvQty.getText().toString().trim());
                if (quantity == 0){
                    Toast.makeText(context, "Add some products", Toast.LENGTH_SHORT).show();
                }
                else {
                    quantity = quantity - 1;
                    holder.tvQty.setText(String.valueOf(quantity));
                }
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCat, tvQuantity, tvPrice, tvQty;
        private final ImageView ivIcon, ivMinus, ivPlus;

        MyViewHolder(View itemView) {
            super(itemView);

            ivIcon     = itemView.findViewById(R.id.ivIcon);
            ivMinus    = itemView.findViewById(R.id.ivMinus);
            ivPlus     = itemView.findViewById(R.id.ivPlus);
            tvCat      = itemView.findViewById(R.id.tvCat);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice    = itemView.findViewById(R.id.tvPrice);
            tvQty      = itemView.findViewById(R.id.tvQty);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //return store_datas.size();
        return categoryData.size();
    }


    public void updateList(List<CategoryData> list) {
        categoryData = list;
        notifyDataSetChanged();
    }
}
