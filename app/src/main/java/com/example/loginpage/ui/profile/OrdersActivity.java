package com.example.loginpage.ui.profile;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.example.loginpage.ui.catalog.ProductImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrdersActivity extends AppCompatActivity {
    private OnBackPressedCallback detailBackCallback;
    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        findViewById(R.id.back).setOnClickListener(v -> finish());
        detailBackCallback = new OnBackPressedCallback(false) {
            @Override public void handleOnBackPressed() {
                showList();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, detailBackCallback);
        render();
        String selected = getIntent().getStringExtra("order_id");
        if (selected != null) {
            JSONArray orders = ProfileStore.orders(this);
            for (int i = 0; i < orders.length(); i++) {
                JSONObject order = orders.optJSONObject(i);
                if (order != null && selected.equals(order.optString("id"))) {
                    showDetail(order);
                    break;
                }
            }
        }
    }
    private TextView text(String value, int size, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(getColor(R.color.text_primary));
        if (bold) {
            view.setTypeface(null, android.graphics.Typeface.BOLD);
        }
        return view;
    }
    private void render() {
        detailBackCallback.setEnabled(false);
        LinearLayout list = findViewById(R.id.ordersContainer);
        list.removeAllViews();
        JSONArray orders = ProfileStore.orders(this);
        if (orders.length() == 0) {
            TextView empty = text("Chưa có đơn hàng\nCác đơn đã đặt sẽ xuất hiện tại đây.", 16, false);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(dp(16), dp(80), dp(16), dp(40));
            list.addView(empty);
            return;
        }
        for (int i = 0; i < orders.length(); i++) {
            JSONObject order = orders.optJSONObject(i);
            if (order == null) {
                continue;
            }
            addOrderCard(list, order);
        }
    }
    private void addOrderCard(LinearLayout list, JSONObject order) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(getColor(R.color.white));
        card.setPadding(dp(18), dp(18), dp(18), dp(18));
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(-1, -2);
        cardParams.bottomMargin = dp(12);
        list.addView(card, cardParams);
        card.addView(text("Đơn #" + order.optString("id") + "  ·  Đã đặt", 15, true));
        TextView date = text(order.optString("date"), 13, false);
        date.setTextColor(getColor(R.color.text_secondary));
        date.setPadding(0, dp(6), 0, dp(14));
        card.addView(date);
        JSONArray products = order.optJSONArray("products");
        JSONObject first = products == null ? null : products.optJSONObject(0);
        String preview = first == null ? order.optString("items") :
                first.optString("name") + " · Size " + first.optString("size")
                + " × " + first.optInt("quantity")
                + (products.length() > 1 ? "\n+" + (products.length() - 1) + " sản phẩm khác" : "");
        LinearLayout previewRow = new LinearLayout(this);
        previewRow.setGravity(Gravity.CENTER_VERTICAL);
        if (first != null) {
            previewRow.addView(productImage(first));
        }
        TextView item = text(preview, 14, false);
        item.setMaxLines(3);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, -2, 1);
        infoParams.leftMargin = dp(12);
        previewRow.addView(item, infoParams);
        card.addView(previewRow);
        TextView total = text("Tổng cộng  " + order.optString("total") + " ₫", 16, true);
        total.setGravity(Gravity.END);
        total.setPadding(0, dp(16), 0, dp(8));
        card.addView(total);
        TextView action = text("Xem chi tiết  ›", 14, true);
        action.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        action.setMinHeight(dp(48));
        card.addView(action);
        card.setOnClickListener(v -> showDetail(order));
    }

    private void showList() {
        ((TextView) findViewById(R.id.back)).setText("‹  Đơn hàng của tôi");
        findViewById(R.id.back).setOnClickListener(v -> finish());
        render();
    }

    private ImageView productImage(JSONObject item) {
        ImageView image = new ImageView(this);
        image.setLayoutParams(new LinearLayout.LayoutParams(dp(64), dp(80)));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Product product = new Product(item.optString("id"), item.optString("name"), "", "",
                item.optInt("image", android.R.drawable.ic_menu_gallery), "",
                item.optString("image_url"));
        ProductImageLoader.load(image, product);
        return image;
    }

    private void showDetail(JSONObject order) {
        detailBackCallback.setEnabled(true);
        LinearLayout list = findViewById(R.id.ordersContainer);
        list.removeAllViews();
        ((TextView) findViewById(R.id.back)).setText("‹  Chi tiết đơn hàng");
        findViewById(R.id.back).setOnClickListener(v -> showList());
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20), dp(20), dp(20), dp(20));
        card.setBackgroundColor(getColor(R.color.white));
        list.addView(card);
        card.addView(text("Đơn #" + order.optString("id"), 20, true));
        card.addView(text("Đã đặt  ·  " + order.optString("date"), 14, false));
        TextView heading = text("SẢN PHẨM", 15, true);
        heading.setPadding(0, dp(28), 0, dp(12));
        card.addView(heading);
        JSONArray products = order.optJSONArray("products");
        if (products == null) card.addView(text(order.optString("items"), 14, false));
        else for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.optJSONObject(i);
            if (product != null) {
                LinearLayout row = new LinearLayout(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(0, dp(8), 0, dp(12));
                row.addView(productImage(product));
                TextView info = text(product.optString("name") + "\nSize " + product.optString("size")
                        + "  ·  SL " + product.optInt("quantity"), 14, false);
                LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, -2, 1);
                infoParams.leftMargin = dp(12);
                row.addView(info, infoParams);
                card.addView(row);
            }
        }
        TextView delivery = text("GIAO ĐẾN", 15, true);
        delivery.setPadding(0, dp(24), 0, dp(12));
        card.addView(delivery);
        card.addView(text(order.optString("name") + "  ·  " + order.optString("phone")
                + "\n" + order.optString("address"), 14, false));
        TextView payment = text("THANH TOÁN", 15, true);
        payment.setPadding(0, dp(24), 0, dp(12));
        card.addView(payment);
        card.addView(text("Thanh toán khi nhận hàng (COD)", 14, false));
        TextView total = text("Tổng cộng  " + order.optString("total") + " ₫", 18, true);
        total.setPadding(0, dp(24), 0, 0);
        card.addView(total);
    }
}
