package com.example.loginpage.ui.cart;

import com.example.loginpage.R;
import com.example.loginpage.ui.catalog.ProductActivity;
import com.example.loginpage.ui.profile.OrdersActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderSuccessActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_order_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        String id = getIntent().getStringExtra("order_id");
        String total = getIntent().getStringExtra("total");
        ((TextView) findViewById(R.id.successInfo)).setText("Mã đơn #" + id + "\nTổng cộng " + total + " ₫");
        findViewById(R.id.viewOrder).setOnClickListener(v -> {
            startActivity(new Intent(this, OrdersActivity.class));
            finish();
        });
        findViewById(R.id.continueShopping).setOnClickListener(v -> {
            startActivity(new Intent(this, ProductActivity.class)
                    .putExtra("reset_home", true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
    }
}
