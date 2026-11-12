package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDetailActivity extends AppCompatActivity {
    // Nhận sản phẩm từ Intent, hiện chi tiết và xử lý thêm vào giỏ.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Product product = new Product(
                intent.getStringExtra("product_name"),
                intent.getStringExtra("product_price"),
                intent.getStringExtra("product_desc"),
                intent.getIntExtra("product_image", R.mipmap.ic_launcher),
                intent.getStringExtra("product_category"),
                intent.getStringExtra("product_image_url"));

        ((TextView) findViewById(R.id.tvDetailName)).setText(product.getName());
        ((TextView) findViewById(R.id.tvDetailPrice)).setText(product.getPrice());
        ((TextView) findViewById(R.id.tvDetailDesc)).setText(product.getDescription());
        ((TextView) findViewById(R.id.tvDetailUser))
                .setText("Tài khoản: " + intent.getStringExtra("username_key"));
        ProductImageLoader.load(findViewById(R.id.imgDetailProduct), product);

        findViewById(R.id.btnOrder).setOnClickListener(view -> {
            CartManager.addToCart(product);
            Toast.makeText(this, "Đã thêm " + product.getName() + " vào giỏ hàng!",
                    Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
    }
}
