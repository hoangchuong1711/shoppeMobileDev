package com.example.loginpage.ui.catalog;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.example.loginpage.ui.cart.CartActivity;
import com.example.loginpage.ui.profile.ProfileFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private View menuOverlay;
    private String username = "Khách";
    private String expandedCategory = "";
    private boolean suppressSearchChange;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        username = getSharedPreferences("user_data", MODE_PRIVATE).getString("username", "Khách");
        edtSearch = findViewById(R.id.edtSearch);
        menuOverlay = findViewById(R.id.menuOverlay);
        findViewById(R.id.headerMenu).setOnClickListener(v -> openMenu());
        findViewById(R.id.headerBrand).setOnClickListener(v -> showAllProducts());
        findViewById(R.id.drawerBrand).setOnClickListener(v -> showAllProducts());
        findViewById(R.id.headerSearch).setOnClickListener(v -> {
            View search = findViewById(R.id.searchBox);
            search.setVisibility(search.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            if (search.getVisibility() == View.VISIBLE) edtSearch.requestFocus();
        });
        findViewById(R.id.headerCart).setOnClickListener(v -> openCart());
        findViewById(R.id.closeMenu).setOnClickListener(v -> closeMenu());
        findViewById(R.id.menuScrim).setOnClickListener(v -> closeMenu());
        setupMenu();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_category) { openMenu(); return false; }
            if (item.getItemId() == R.id.nav_cart) { openCart(); return false; }
            if (item.getItemId() == R.id.nav_home) resetHomeContent();
            else showTab(item.getItemId());
            return true;
        });
        bottomNavigation.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) showAllProducts();
        });
        int selectedTab = savedInstanceState == null ? R.id.nav_home
                : savedInstanceState.getInt(SELECTED_TAB, R.id.nav_home);
        bottomNavigation.setSelectedItemId(selectedTab);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (suppressSearchChange) return;
                if (bottomNavigation.getSelectedItemId() != R.id.nav_home)
                    bottomNavigation.setSelectedItemId(R.id.nav_home);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("tab_" + R.id.nav_home);
                if (fragment instanceof HomeFragment) ((HomeFragment) fragment).filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
        if (getIntent().getBooleanExtra("open_menu", false)) openMenu();
        if (getIntent().getBooleanExtra("open_profile", false)) bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.getBooleanExtra("open_menu", false)) openMenu();
        else if (intent.getBooleanExtra("open_profile", false)) bottomNavigation.setSelectedItemId(R.id.nav_profile);
        else if (intent.getBooleanExtra("reset_home", false)) showAllProducts();
    }

    private void setupMenu() {
        LinearLayout items = findViewById(R.id.menuItems);
        items.removeAllViews();
        String[][] groups = {
                {"Áo thun", "Áo Thun Cổ Tròn", "Áo Thun Cổ Polo & Henley", "Áo Thun Tank Top", "Áo Thun Dệt Kim"},
                {"Áo sơ mi", "Áo Sơ Mi Tay Ngắn", "Áo Sơ Mi Tay Dài"},
                {"Áo khoác", "Áo Khoác Dù", "Áo Khoác Thun", "Áo Khoác Jean", "Áo Khoác Kaki"},
                {"Quần jeans", "Quần Jeans Nguyên Bản", "Quần Jeans Ống Rộng"},
                {"Quần dài", "Quần Dài Lưng Gài", "Quần Dài Lưng Thun"},
                {"Quần tây", "Quần Tây Nam"},
                {"Quần short", "Quần Short Lưng Thun", "Quần Short Jeans"}
        };
        for (String[] group : groups) {
            String category = group[0];
            TextView heading = new TextView(this);
            heading.setText(category.toUpperCase(java.util.Locale.forLanguageTag("vi-VN"))
                    + (category.equals(expandedCategory) ? "    ⌃" : "    ⌄"));
            heading.setTextColor(getColor(R.color.text_primary));
            heading.setTextSize(16);
            heading.setGravity(android.view.Gravity.CENTER_VERTICAL);
            heading.setPadding(dp(18), 0, dp(18), 0);
            heading.setMinHeight(dp(58));
            heading.setBackgroundResource(android.R.drawable.list_selector_background);
            heading.setOnClickListener(v -> {
                expandedCategory = category.equals(expandedCategory) ? "" : category;
                setupMenu();
            });
            items.addView(heading);
            if (!category.equals(expandedCategory)) continue;
            TextView section = new TextView(this);
            section.setText("THEO KIỂU SẢN PHẨM");
            section.setTextSize(11);
            section.setTextColor(getColor(R.color.text_secondary));
            section.setPadding(dp(28), dp(14), 0, dp(6));
            items.addView(section);
            for (int i = 1; i < group.length; i++) {
                String type = group[i];
                TextView child = new TextView(this);
                child.setText(type);
                child.setTextColor(getColor(R.color.text_body));
                child.setTextSize(14);
                child.setGravity(android.view.Gravity.CENTER_VERTICAL);
                child.setPadding(dp(28), 0, dp(12), 0);
                child.setMinHeight(dp(48));
                child.setBackgroundResource(android.R.drawable.list_selector_background);
                child.setOnClickListener(v -> showCategory(category, type));
                items.addView(child);
            }
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private void showCategory(String category, String type) {
        closeMenu();
        clearSearch();
        if (bottomNavigation.getSelectedItemId() != R.id.nav_home)
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        else showTab(R.id.nav_home);
        getSupportFragmentManager().executePendingTransactions();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("tab_" + R.id.nav_home);
        if (fragment instanceof HomeFragment) ((HomeFragment) fragment).showCategory(category, type);
    }

    public void showAllProducts() {
        if (bottomNavigation.getSelectedItemId() != R.id.nav_home)
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        else resetHomeContent();
    }

    private void resetHomeContent() {
        closeMenu();
        clearSearch();
        showTab(R.id.nav_home);
        getSupportFragmentManager().executePendingTransactions();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("tab_" + R.id.nav_home);
        if (fragment instanceof HomeFragment) ((HomeFragment) fragment).showAll();
    }

    private void clearSearch() {
        suppressSearchChange = true;
        edtSearch.setText("");
        suppressSearchChange = false;
    }

    private void openMenu() { menuOverlay.setVisibility(View.VISIBLE); }
    private void closeMenu() { menuOverlay.setVisibility(View.GONE); }
    private void openCart() { startActivity(new Intent(this, CartActivity.class)); }

    private void showTab(int itemId) {
        Fragment target = getSupportFragmentManager().findFragmentByTag("tab_" + itemId);
        if (target == null) {
            if (itemId == R.id.nav_home) target = new HomeFragment();
            else if (itemId == R.id.nav_profile) target = new ProfileFragment();
            else return;
        }
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if (fragment.isAdded() && fragment != target) transaction.hide(fragment);
        if (target.isAdded()) transaction.show(target);
        else transaction.add(R.id.fragmentContainer, target, "tab_" + itemId);
        transaction.commit();
    }

    public String getUsername() { return username; }
    public String getSearchQuery() { return edtSearch.getText().toString(); }
    public void openProductDetail(Product product) {
        getSharedPreferences("selected_product", MODE_PRIVATE).edit()
                .putString("id", product.getId()).putString("name", product.getName())
                .putString("price", product.getPrice())
                .putString("description", product.getDescription())
                .putInt("image", product.getImageResId())
                .putString("category", product.getCategory())
                .putString("image_url", product.getImageUrl()).apply();
        startActivity(new Intent(this, ProductDetailActivity.class));
    }
    @Override public void onBackPressed() {
        if (menuOverlay.getVisibility() == View.VISIBLE) closeMenu();
        else super.onBackPressed();
    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_TAB, bottomNavigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }
}
