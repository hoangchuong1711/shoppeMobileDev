package com.example.loginpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(@NonNull Context context, @NonNull List<Product> products) {
        super(context, R.layout.item_product, products);
    }

    // Tái sử dụng ô cũ khi cuộn và đổ dữ liệu của một sản phẩm vào ô.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView != null ? convertView
                : LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        Product product = Objects.requireNonNull(getItem(position));

        ProductImageLoader.load(row.findViewById(R.id.imgProduct), product);
        ((TextView) row.findViewById(R.id.tvProductName)).setText(product.getName());
        ((TextView) row.findViewById(R.id.tvProductPrice)).setText(product.getPrice());
        ((TextView) row.findViewById(R.id.tvProductDesc)).setText(product.getDescription());
        return row;
    }
}
