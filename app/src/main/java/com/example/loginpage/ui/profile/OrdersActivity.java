package com.example.loginpage.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
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
    }

    private void render() {
        detailBackCallback.setEnabled(false);
        LinearLayout list = findViewById(R.id.ordersContainer);
        list.removeAllViews();
        JSONArray orders = ProfileStore.orders(this);
        findViewById(R.id.orderDetail).setVisibility(View.GONE);
        findViewById(R.id.ordersEmpty).setVisibility(
                orders.length() == 0 ? View.VISIBLE : View.GONE);
        if (orders.length() == 0) return;
        for (int i = 0; i < orders.length(); i++) {
            JSONObject order = orders.optJSONObject(i);
            if (order != null) addOrderCard(list, order);
        }
    }

    private void addOrderCard(LinearLayout list, JSONObject order) {
        View card = LayoutInflater.from(this).inflate(R.layout.item_order, list, false);
        list.addView(card);
        ((TextView) card.findViewById(R.id.orderTitle))
                .setText("Đơn #" + order.optString("id") + "  ·  Đã đặt");
        ((TextView) card.findViewById(R.id.orderDate)).setText(order.optString("date"));
        JSONArray products = order.optJSONArray("products");
        JSONObject first = products == null ? null : products.optJSONObject(0);
        String preview = first == null ? order.optString("items") :
                first.optString("name") + " · Size " + first.optString("size")
                + " × " + first.optInt("quantity")
                + (products.length() > 1 ? "\n+" + (products.length() - 1) + " sản phẩm khác" : "");
        ImageView image = card.findViewById(R.id.orderImage);
        if (first == null) image.setVisibility(View.GONE);
        else loadProductImage(image, first);
        ((TextView) card.findViewById(R.id.orderPreview)).setText(preview);
        ((TextView) card.findViewById(R.id.orderTotal))
                .setText("Tổng cộng  " + order.optString("total") + " ₫");
        card.setOnClickListener(v -> showDetail(order));
    }

    private void showList() {
        ((TextView) findViewById(R.id.back)).setText("‹  Đơn hàng của tôi");
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.ordersContainer).setVisibility(View.VISIBLE);
        render();
    }

    private void loadProductImage(ImageView image, JSONObject item) {
        Product product = new Product(item.optString("id"), item.optString("name"), "", "",
                item.optInt("image", android.R.drawable.ic_menu_gallery), "",
                item.optString("image_url"));
        ProductImageLoader.load(image, product);
    }

    private void showDetail(JSONObject order) {
        detailBackCallback.setEnabled(true);
        findViewById(R.id.ordersContainer).setVisibility(View.GONE);
        findViewById(R.id.ordersEmpty).setVisibility(View.GONE);
        View detail = findViewById(R.id.orderDetail);
        detail.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.back)).setText("‹  Chi tiết đơn hàng");
        findViewById(R.id.back).setOnClickListener(v -> showList());
        ((TextView) detail.findViewById(R.id.detailTitle)).setText("Đơn #" + order.optString("id"));
        ((TextView) detail.findViewById(R.id.detailDate))
                .setText("Đã đặt  ·  " + order.optString("date"));
        LinearLayout productList = detail.findViewById(R.id.detailProducts);
        productList.removeViews(1, productList.getChildCount() - 1);
        TextView legacyItems = detail.findViewById(R.id.legacyItems);
        legacyItems.setVisibility(View.GONE);
        JSONArray products = order.optJSONArray("products");
        if (products == null) {
            TextView items = detail.findViewById(R.id.legacyItems);
            items.setVisibility(View.VISIBLE);
            items.setText(order.optString("items"));
        } else {
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.optJSONObject(i);
                if (product == null) continue;
                View row = LayoutInflater.from(this)
                        .inflate(R.layout.item_order_product, productList, false);
                productList.addView(row);
                loadProductImage(row.findViewById(R.id.productImage), product);
                ((TextView) row.findViewById(R.id.productInfo))
                        .setText(product.optString("name") + "\nSize " + product.optString("size")
                                + "  ·  SL " + product.optInt("quantity"));
            }
        }
        ((TextView) detail.findViewById(R.id.detailDelivery))
                .setText(order.optString("name") + "  ·  " + order.optString("phone")
                        + "\n" + order.optString("address"));
        ((TextView) detail.findViewById(R.id.detailTotal))
                .setText("Tổng cộng  " + order.optString("total") + " ₫");
    }
}
