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
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
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
    private View menuPanel;
    private View menuScrim;
    private boolean menuOpen;
    private String username = "Khách";
    private boolean suppressSearchChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContent();
        username = getSharedPreferences("user_data", MODE_PRIVATE)
                .getString("username", "Khách");
        findViews();
        setupHeader();
        setupMenu();
        setupBottomNavigation(savedInstanceState);
        setupSearch();
        setupBackButton();
        handleInitialIntent();
    }

    private void setupContent() {
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    private void findViews() {
        edtSearch = findViewById(R.id.edtSearch);
        menuOverlay = findViewById(R.id.menuOverlay);
        menuPanel = findViewById(R.id.menuPanel);
        menuScrim = findViewById(R.id.menuScrim);
    }

    private void setupHeader() {
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
    }

    private void setupBottomNavigation(Bundle savedInstanceState) {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_category) {
                openMenu();
                return false;
            }
            if (item.getItemId() == R.id.nav_cart) {
                openCart();
                return false;
            }
            if (item.getItemId() == R.id.nav_home) {
                resetHomeContent();
            } else {
                showTab(item.getItemId());
            }
            return true;
        });
        bottomNavigation.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) showAllProducts();
        });
        int selectedTab = savedInstanceState == null ? R.id.nav_home
                : savedInstanceState.getInt(SELECTED_TAB, R.id.nav_home);
        bottomNavigation.setSelectedItemId(selectedTab);
    }

    private void setupSearch() {
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
    }

    private void setupBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() {
                if (menuOpen) {
                    closeMenu();
                } else if (bottomNavigation.getSelectedItemId() != R.id.nav_home) {
                    showAllProducts();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void handleInitialIntent() {
        if (getIntent().getBooleanExtra("open_menu", false)) openMenu();
        if (getIntent().getBooleanExtra("open_profile", false))
            bottomNavigation.setSelectedItemId(R.id.nav_profile);
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
        new CategoryMenu(this, items, this::showCategory).render();
    }

    private void showCategory(String type) {
        closeMenu();
        clearSearch();
        if (bottomNavigation.getSelectedItemId() != R.id.nav_home)
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        else showTab(R.id.nav_home);
        getSupportFragmentManager().executePendingTransactions();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("tab_" + R.id.nav_home);
        if (fragment instanceof HomeFragment) ((HomeFragment) fragment).showCategory(type);
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

    private void openMenu() {
        if (menuOpen) return;
        menuOpen = true;
        menuPanel.animate().cancel();
        menuScrim.animate().cancel();
        menuOverlay.setVisibility(View.VISIBLE);
        menuPanel.post(() -> {
            if (!menuOpen) return;
            menuPanel.setTranslationX(-menuPanel.getWidth());
            menuScrim.setAlpha(0f);
            menuPanel.animate().translationX(0f).setDuration(200)
                    .setInterpolator(new DecelerateInterpolator()).start();
            menuScrim.animate().alpha(1f).setDuration(200).start();
        });
    }

    private void closeMenu() {
        if (!menuOpen) return;
        menuOpen = false;
        menuPanel.animate().cancel();
        menuScrim.animate().cancel();
        menuPanel.animate().translationX(-menuPanel.getWidth()).setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
                    if (!menuOpen) menuOverlay.setVisibility(View.GONE);
                }).start();
        menuScrim.animate().alpha(0f).setDuration(200).start();
    }
    private void openCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

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

    public String getUsername() {
        return username;
    }

    public void openProductDetail(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("id", product.getId());
        intent.putExtra("name", product.getName());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("description", product.getDescription());
        intent.putExtra("image", product.getImageResId());
        intent.putExtra("category", product.getCategory());
        intent.putExtra("image_url", product.getImageUrl());
        startActivity(intent);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_TAB, bottomNavigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }
}
