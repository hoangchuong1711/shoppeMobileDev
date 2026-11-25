package com.example.loginpage.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.example.loginpage.ui.catalog.ProductImageLoader;
import com.example.loginpage.ui.profile.EditProfileActivity;
import com.example.loginpage.ui.profile.ProfileStore;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private final NumberFormat money = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"));

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.editAddress).setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class)));
        findViewById(R.id.confirm).setOnClickListener(v -> placeOrder());
    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    private void render() {
        if (!CartManager.hasSelection()) {
            finish();
            return;
        }
        String name = ProfileStore.get(this, "name");
        String phone = ProfileStore.get(this, "phone");
        String address = ProfileStore.get(this, "address");
        ((TextView) findViewById(R.id.recipient)).setText(
                name.isEmpty() ? "Chưa có thông tin nhận hàng" : name + "  ·  " + phone + "\n" + address);
        LinearLayout container = findViewById(R.id.checkoutItems);
        container.removeAllViews();
        int count = 0;
        for (Product product : CartManager.getCartList()) {
            if (!product.isChecked()) {
                continue;
            }
            count += product.getQuantity();
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(0, dp(14), 0, dp(14));
            ImageView image = new ImageView(this);
            image.setLayoutParams(new LinearLayout.LayoutParams(dp(64), dp(80)));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ProductImageLoader.load(image, product);
            row.addView(image);
            TextView info = new TextView(this);
            info.setText(product.getName() + "\nSize " + product.getSize() + "  ·  SL " + product.getQuantity()
                    + "\n" + money.format(product.getPriceValue() * product.getQuantity()) + " ₫");
            info.setTextSize(14);
            info.setTextColor(getColor(R.color.text_primary));
            info.setMaxLines(4);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2, 1);
            params.leftMargin = dp(12);
            row.addView(info, params);
            container.addView(row);
        }
        ((TextView) findViewById(R.id.productCount)).setText("SẢN PHẨM · " + count);
        ((TextView) findViewById(R.id.total)).setText("Tổng cộng  " + money.format(CartManager.getSelectedTotal()) + " ₫");
    }

    private JSONObject createOrder(String name, String phone, String address) {
        JSONArray products = new JSONArray();
        StringBuilder lines = new StringBuilder();
        for (Product product : CartManager.getCartList()) {
            if (!product.isChecked()) {
                continue;
            }
            JSONObject entry = new JSONObject();
            try {
                entry.put("id", product.getId());
                entry.put("name", product.getName());
                entry.put("size", product.getSize());
                entry.put("quantity", product.getQuantity());
                entry.put("price", product.getPriceValue());
                entry.put("image", product.getImageResId());
                entry.put("image_url", product.getImageUrl());
                products.put(entry);
            } catch (org.json.JSONException exception) {
                throw new IllegalStateException(exception);
            }
            lines.append(product.getName()).append(" · Size ").append(product.getSize())
                    .append(" × ").append(product.getQuantity()).append("\n");
        }
        String id = String.valueOf(System.currentTimeMillis());
        JSONObject order = new JSONObject();
        try {
            order.put("id", id);
            order.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("vi-VN")).format(new Date()));
            order.put("name", name);
            order.put("phone", phone);
            order.put("address", address);
            order.put("items", lines.toString().trim());
            order.put("products", products);
            order.put("total", money.format(CartManager.getSelectedTotal()));
        } catch (JSONException exception) {
            throw new IllegalStateException(exception);
        }
        return order;
    }

    private void placeOrder() {
        String name = ProfileStore.get(this, "name");
        String phone = ProfileStore.get(this, "phone");
        String address = ProfileStore.get(this, "address");
        if (name.isEmpty() || !phone.matches("0[0-9]{9}") || address.isEmpty()) {
            new MaterialAlertDialogBuilder(this).setMessage("Vui lòng lưu thông tin nhận hàng trước khi đặt.")
                    .setPositiveButton("Chỉnh sửa", (d, w) -> startActivity(new Intent(this, EditProfileActivity.class)))
                    .setNegativeButton("Để sau", null).show();
            return;
        }
        if (!CartManager.hasSelection()) {
            return;
        }
        JSONObject order = createOrder(name, phone, address);
        String id = order.optString("id");
        ProfileStore.addOrder(this, order);
        CartManager.removeSelected();
        startActivity(new Intent(this, OrderSuccessActivity.class)
                .putExtra("order_id", id).putExtra("total", order.optString("total")));
        finish();
    }
}
