package com.example.loginpage.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.example.loginpage.ui.catalog.ProductActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private final NumberFormat money = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"));
    private CartAdapter adapter;
    private TextView total;
    private Button checkout;

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
        total = findViewById(R.id.tvCartTotal);
        checkout = findViewById(R.id.btnCheckout);
        adapter = new CartAdapter(this, products, this::updateTotal);

        ListView list = findViewById(R.id.lvCart);
        list.setEmptyView(findViewById(R.id.tvEmptyCart));
        list.setAdapter(adapter);

        findViewById(R.id.headerMenu).setOnClickListener(v -> goToShop("open_menu"));
        findViewById(R.id.headerBrand).setOnClickListener(v -> goToShop("reset_home"));
        findViewById(R.id.headerSearch).setOnClickListener(v -> goToShop(null));
        findViewById(R.id.continueShopping).setOnClickListener(v -> finish());

        BottomNavigationView navigation = findViewById(R.id.cartBottomNavigation);
        navigation.setSelectedItemId(R.id.nav_cart);
        navigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_cart) return true;
            if (id == R.id.nav_home) goToShop("reset_home");
            else if (id == R.id.nav_category) goToShop("open_menu");
            else if (id == R.id.nav_profile) goToShop("open_profile");
            return false;
        });

        Button selectAll = findViewById(R.id.btnSelectAll);
        selectAll.setOnClickListener(v -> {
            boolean select = !CartManager.hasSelection();
            for (Product product : products) product.setChecked(select);
            adapter.notifyDataSetChanged();
            updateTotal();
        });

        checkout.setOnClickListener(v -> {
            if (CartManager.hasSelection()) {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });
        updateTotal();
    }

    private void goToShop(String action) {
        Intent intent = new Intent(this, ProductActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (action != null) intent.putExtra(action, true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            updateTotal();
        }
    }

    private void updateTotal() {
        total.setText("Tổng: " + money.format(CartManager.getSelectedTotal()) + " ₫");
        checkout.setEnabled(CartManager.hasSelection());
    }
}
