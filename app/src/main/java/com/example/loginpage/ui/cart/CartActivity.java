package com.example.loginpage.ui.cart;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import android.os.Bundle;
import android.content.Intent;
import com.example.loginpage.ui.catalog.ProductActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private CartAdapter adapter;
    private TextView total;
    private Button checkout;
    private final NumberFormat money = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"));
    @Override protected void onCreate(Bundle savedInstanceState) {
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
        Button selectAll = findViewById(R.id.btnSelectAll);
        adapter = new CartAdapter(this, products, this::updateTotal);
        ListView list = findViewById(R.id.lvCart);
        list.setEmptyView(findViewById(R.id.tvEmptyCart));
        list.setAdapter(adapter);
        findViewById(R.id.headerMenu).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra("open_menu", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.headerCart).setOnClickListener(v -> {});
        findViewById(R.id.headerBrand).setOnClickListener(v -> {
            startActivity(new Intent(this, ProductActivity.class).putExtra("reset_home", true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
        findViewById(R.id.headerSearch).setOnClickListener(v -> {
            startActivity(new Intent(this, ProductActivity.class));
            finish();
        });
        findViewById(R.id.continueShopping).setOnClickListener(v -> finish());
        BottomNavigationView nav = findViewById(R.id.cartBottomNavigation);
        nav.setSelectedItemId(R.id.nav_cart);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_cart) return true;
            Intent intent = new Intent(this, ProductActivity.class);
            if (item.getItemId() == R.id.nav_home) intent.putExtra("reset_home", true);
            if (item.getItemId() == R.id.nav_category) intent.putExtra("open_menu", true);
            if (item.getItemId() == R.id.nav_profile) intent.putExtra("open_profile", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return false;
        });
        selectAll.setOnClickListener(v -> {
            boolean select = !CartManager.hasSelection();
            for (Product product : products) product.setChecked(select);
            adapter.notifyDataSetChanged();
            updateTotal();
        });
        checkout.setOnClickListener(v -> showCheckout());
        updateTotal();
    }
    private void updateTotal() {
        total.setText("Tổng: " + money.format(CartManager.getSelectedTotal()) + " ₫");
        checkout.setEnabled(CartManager.hasSelection());
    }
    private void showCheckout() {
        View form = LayoutInflater.from(this).inflate(R.layout.dialog_checkout, null);
        TextInputLayout name = form.findViewById(R.id.orderName);
        TextInputLayout phone = form.findViewById(R.id.orderPhone);
        TextInputLayout address = form.findViewById(R.id.orderAddress);
        TextView summary = form.findViewById(R.id.orderSummary);
        StringBuilder lines = new StringBuilder();
        for (Product item : CartManager.getCartList()) if (item.isChecked()) {
            lines.append(item.getName()).append(" • ").append(item.getSize())
                    .append(" × ").append(item.getQuantity()).append("\n");
        }
        summary.setText(lines + "\nTổng: " + money.format(CartManager.getSelectedTotal()) + " ₫\nThanh toán khi nhận hàng");
        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Xác nhận đặt hàng").setView(form)
                .setNegativeButton("Hủy", null).setPositiveButton("Đặt hàng", null).create();
        dialog.setOnShowListener(unused -> dialog.getButton(-1).setOnClickListener(v -> {
            String n = name.getEditText().getText().toString().trim();
            String p = phone.getEditText().getText().toString().trim();
            String a = address.getEditText().getText().toString().trim();
            name.setError(n.isEmpty() ? "Nhập tên người nhận" : null);
            phone.setError(p.matches("0[0-9]{9}") ? null : "Nhập số điện thoại 10 số");
            address.setError(a.isEmpty() ? "Nhập địa chỉ" : null);
            if (n.isEmpty() || !p.matches("0[0-9]{9}") || a.isEmpty()) return;
            CartManager.removeSelected();
            adapter.notifyDataSetChanged();
            updateTotal();
            dialog.dismiss();
            new MaterialAlertDialogBuilder(this).setTitle("Đặt hàng thành công")
                    .setMessage("Đơn hàng mô phỏng đã được xác nhận.")
                    .setPositiveButton("Tiếp tục mua sắm", (d, which) -> finish()).show();
        }));
        dialog.show();
    }
}
