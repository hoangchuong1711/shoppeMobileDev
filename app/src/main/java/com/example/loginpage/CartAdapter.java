package com.example.loginpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class CartAdapter extends ArrayAdapter<Product> {
    public CartAdapter(@NonNull Context context, @NonNull List<Product> products) {
        super(context, R.layout.item_cart, products);
    }

    // Hiện một dòng giỏ hàng; gỡ listener trước khi gán lại CheckBox.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView != null ? convertView
                : LayoutInflater.from(getContext()).inflate(R.layout.item_cart, parent, false);
        Product product = Objects.requireNonNull(getItem(position));

        CheckBox checkBox = row.findViewById(R.id.cbCartItem);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(product.isChecked());
        checkBox.setOnCheckedChangeListener((button, checked) -> product.setChecked(checked));

        ProductImageLoader.load(row.findViewById(R.id.imgCartProduct), product);
        ((TextView) row.findViewById(R.id.tvCartName)).setText(product.getName());
        ((TextView) row.findViewById(R.id.tvCartDesc)).setText(product.getDescription());
        ((TextView) row.findViewById(R.id.tvCartPrice)).setText(product.getPrice());
        return row;
    }
}
