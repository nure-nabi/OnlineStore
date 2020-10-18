package com.store.acmestore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.acmestore.MainActivity;
import com.store.acmestore.R;
import com.store.acmestore.model.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Poductclass> {
    MainActivity context;
    List<ProductModel> listProduct;

    public ProductAdapter(MainActivity context, List<ProductModel> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ProductAdapter.Poductclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new Poductclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.Poductclass holder, int position) {

        ProductModel info = listProduct.get(position);
        holder.productNameTxt.setText(info.getProductName());
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class Poductclass extends RecyclerView.ViewHolder {
        TextView productNameTxt;
        public Poductclass(@NonNull View itemView) {
            super(itemView);
            productNameTxt = itemView.findViewById(R.id.productNameTxt);
        }
    }
}
