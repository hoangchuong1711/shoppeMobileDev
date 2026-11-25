package com.example.loginpage.ui.cart;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    // Hiện giỏ hàng và xử lý nút chọn hoặc bỏ chọn tất cả.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        ArrayList<Product> products = CartManager.getCartList();
        Button selectAllButton = findViewById(R.id.btnSelectAll);
        CartAdapter adapter = new CartAdapter(this, products,
                () -> selectAllButton.setEnabled(!products.isEmpty()));
        ListView listView = findViewById(R.id.lvCart);
        listView.setEmptyView(findViewById(R.id.tvEmptyCart));
        listView.setAdapter(adapter);
        selectAllButton.setEnabled(!products.isEmpty());
        ((com.google.android.material.appbar.MaterialToolbar) findViewById(R.id.cartToolbar))
                .setNavigationOnClickListener(view -> finish());
        selectAllButton.setOnClickListener(view -> {
            boolean selectAll = false;
            for (Product product : products) {
                if (!product.isChecked()) {
                    selectAll = true;
                    break;
                }
            }
            for (Product product : products) product.setChecked(selectAll);
            adapter.notifyDataSetChanged();
        });
    }
}
