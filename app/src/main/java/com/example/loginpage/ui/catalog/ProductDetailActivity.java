package com.example.loginpage.ui.catalog;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.example.loginpage.ui.cart.CartManager;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        SharedPreferences prefs = getSharedPreferences("selected_product", MODE_PRIVATE);
        if (!prefs.contains("name")) { finish(); return; }
        Product product = new Product(prefs.getString("id", prefs.getString("name", "")),
                prefs.getString("name", ""), prefs.getString("price", ""),
                prefs.getString("description", ""), prefs.getInt("image", android.R.drawable.ic_menu_gallery),
                prefs.getString("category", ""), prefs.getString("image_url", null));
        ((TextView) findViewById(R.id.tvDetailName)).setText(product.getName());
        ((TextView) findViewById(R.id.tvDetailPrice)).setText(product.getPrice());
        ((TextView) findViewById(R.id.tvDetailDesc)).setText(product.getDescription());
        ProductImageLoader.load(findViewById(R.id.imgDetailProduct), product);
        findViewById(R.id.headerMenu).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, ProductActivity.class);
            intent.putExtra("open_menu", true);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.headerBrand).setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, ProductActivity.class)
                    .putExtra("reset_home", true).addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
        findViewById(R.id.headerCart).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, com.example.loginpage.ui.cart.CartActivity.class)));
        findViewById(R.id.headerSearch).setOnClickListener(v -> finish());
        ChipGroup sizes = findViewById(R.id.sizeChipGroup);
        findViewById(R.id.btnOrder).setOnClickListener(view -> {
            int id = sizes.getCheckedChipId();
            if (id == android.view.View.NO_ID) {
                Snackbar.make(view, "Vui lòng chọn size", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String size = ((com.google.android.material.chip.Chip) findViewById(id)).getText().toString();
            CartManager.addToCart(product, size);
            Snackbar.make(view, "Đã thêm vào giỏ hàng", Snackbar.LENGTH_SHORT).show();
        });
    }
}
