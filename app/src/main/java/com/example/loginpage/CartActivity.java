package com.example.loginpage;

import android.os.Bundle;
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
        CartAdapter adapter = new CartAdapter(this, products);
        ((ListView) findViewById(R.id.lvCart)).setAdapter(adapter);
        findViewById(R.id.btnBackCart).setOnClickListener(view -> finish());
        findViewById(R.id.btnSelectAll).setOnClickListener(view -> {
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
