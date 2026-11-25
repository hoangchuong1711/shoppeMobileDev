package com.example.loginpage.ui.cart;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.example.loginpage.ui.catalog.ProductImageLoader;

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
    private final Runnable onCartChanged;

    public CartAdapter(@NonNull Context context, @NonNull List<Product> products,
                       @NonNull Runnable onCartChanged) {
        super(context, R.layout.item_cart, products);
        this.onCartChanged = onCartChanged;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView != null ? convertView
                : LayoutInflater.from(getContext()).inflate(R.layout.item_cart, parent, false);
        Product product = Objects.requireNonNull(getItem(position));

        CheckBox checkBox = row.findViewById(R.id.cbCartItem);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(product.isChecked());
        checkBox.setOnCheckedChangeListener((button, checked) -> { product.setChecked(checked); onCartChanged.run(); });

        ProductImageLoader.load(row.findViewById(R.id.imgCartProduct), product);
        ((TextView) row.findViewById(R.id.tvCartName)).setText(product.getName());
        ((TextView) row.findViewById(R.id.tvCartDesc)).setText("Size " + product.getSize());
        ((TextView) row.findViewById(R.id.tvCartPrice)).setText(product.getPrice());
        ((TextView) row.findViewById(R.id.tvCartQuantity))
                .setText(String.valueOf(product.getQuantity()));

        View decreaseButton = row.findViewById(R.id.btnDecreaseQuantity);
        decreaseButton.setEnabled(product.getQuantity() > 1);
        decreaseButton.setAlpha(product.getQuantity() > 1 ? 1f : 0.4f);
        decreaseButton.setOnClickListener(view -> {
            product.decreaseQuantity();
            notifyDataSetChanged();
            onCartChanged.run();
        });

        View increaseButton = row.findViewById(R.id.btnIncreaseQuantity);
        increaseButton.setEnabled(product.getQuantity() < Integer.MAX_VALUE);
        increaseButton.setOnClickListener(view -> {
            product.increaseQuantity();
            notifyDataSetChanged();
            onCartChanged.run();
        });

        row.findViewById(R.id.btnRemoveCartItem).setOnClickListener(view -> {
            CartManager.removeFromCart(product);
            notifyDataSetChanged();
            onCartChanged.run();
        });
        return row;
    }
}
