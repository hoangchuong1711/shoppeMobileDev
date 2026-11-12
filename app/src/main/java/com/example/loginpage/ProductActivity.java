package com.example.loginpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductActivity extends AppCompatActivity {
    private static final String SELECTED_TAB = "selected_tab";
    private BottomNavigationView bottomNavigation;
    private EditText edtSearch;
    private TextView tvCartBadge;
    private String username = "Khách";

    // Khởi tạo khung chung, thanh tab và ô tìm kiếm.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        SharedPreferences userPrefs = getSharedPreferences("user_data", MODE_PRIVATE);
        username = userPrefs.getString("username", "Khách");
        edtSearch = findViewById(R.id.edtSearch);
        tvCartBadge = findViewById(R.id.tvCartBadge);
        findViewById(R.id.btnOpenCart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            showTab(item.getItemId());
            return true;
        });
        int selectedTab = savedInstanceState == null
                ? R.id.nav_home : savedInstanceState.getInt(SELECTED_TAB, R.id.nav_home);
        bottomNavigation.setSelectedItemId(selectedTab);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            // Mỗi lần gõ, chuyển về Home rồi lọc danh sách sản phẩm.
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bottomNavigation.getSelectedItemId() != R.id.nav_home) {
                    bottomNavigation.setSelectedItemId(R.id.nav_home);
                }
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("tab_" + R.id.nav_home);
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).filter(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // Tạo tab lần đầu; các lần sau chỉ ẩn/hiện Fragment để giữ trạng thái.
    private void showTab(int itemId) {
        Fragment target = getSupportFragmentManager().findFragmentByTag("tab_" + itemId);
        if (target == null) {
            if (itemId == R.id.nav_home) target = new HomeFragment();
            else if (itemId == R.id.nav_category) target = new CategoryFragment();
            else if (itemId == R.id.nav_notification) target = new NotificationFragment();
            else if (itemId == R.id.nav_profile) target = new ProfileFragment();
            else return;
        }

        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.isAdded() && fragment != target) transaction.hide(fragment);
        }

        if (target.isAdded()) transaction.show(target);
        else transaction.add(R.id.fragmentContainer, target, "tab_" + itemId);
        transaction.commit();
    }

    public String getUsername() { return username; }
    public String getSearchQuery() { return edtSearch.getText().toString(); }

    // Lưu sản phẩm đang chọn để màn chi tiết đọc lại từ SharedPreferences.
    public void openProductDetail(Product product) {
        getSharedPreferences("selected_product", MODE_PRIVATE).edit()
                .putString("name", product.getName())
                .putString("price", product.getPrice())
                .putString("description", product.getDescription())
                .putInt("image", product.getImageResId())
                .putString("category", product.getCategory())
                .putString("image_url", product.getImageUrl())
                .apply();
        startActivity(new Intent(this, ProductDetailActivity.class));
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_TAB, bottomNavigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    // Cập nhật số lượng giỏ khi quay về từ màn chi tiết hoặc giỏ hàng.
    @Override protected void onResume() {
        super.onResume();
        tvCartBadge.setText(String.valueOf(CartManager.getCartList().size()));
    }
}
