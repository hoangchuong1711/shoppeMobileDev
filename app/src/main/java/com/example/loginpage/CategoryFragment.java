package com.example.loginpage;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragment extends Fragment {
    private static final String SELECTED_CATEGORY = "selected_category";
    private static final int[] CATEGORY_IDS = {
            R.id.catAll, R.id.catFashion, R.id.catConsumer, R.id.catHealth, R.id.catTech
    };

    private final List<Product> visibleProducts = new ArrayList<>();
    private final List<Product> allProducts = new ArrayList<>();
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private String category = "Tất cả";
    private ProductAdapter adapter;

    public CategoryFragment() { super(R.layout.fragment_category); }

    // Gắn sản phẩm và sự kiện bấm cho từng nút danh mục.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            category = savedInstanceState.getString(SELECTED_CATEGORY, "Tất cả");
        }
        ProductActivity activity = (ProductActivity) requireActivity();
        GridView grid = view.findViewById(R.id.gvCategoryProducts);
        adapter = new ProductAdapter(requireContext(), visibleProducts);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent, item, position, id) ->
                activity.openProductDetail(visibleProducts.get(position)));

        for (int id : CATEGORY_IDS) {
            TextView tab = view.findViewById(id);
            tab.setOnClickListener(v -> selectCategory(view, tab.getText().toString()));
        }
        selectCategory(view, category);
        Context appContext = requireContext().getApplicationContext();
        databaseExecutor.execute(() -> {
            List<Product> products = new ProductRepository(appContext).getProducts();
            new Handler(Looper.getMainLooper()).post(() -> {
                if (adapter == null || getView() != view) return;
                allProducts.clear();
                allProducts.addAll(products);
                selectCategory(view, category);
            });
        });
    }

    // Đổi màu danh mục đang chọn và chỉ hiện sản phẩm thuộc danh mục đó.
    private void selectCategory(View view, String selected) {
        category = selected;
        for (int id : CATEGORY_IDS) {
            TextView tab = view.findViewById(id);
            boolean active = selected.contentEquals(tab.getText());
            tab.setBackgroundResource(active ? R.drawable.bg_category_selected : R.drawable.bg_category_unselected);
            tab.setTextColor(active ? Color.WHITE : Color.parseColor("#222222"));
        }
        ((TextView) view.findViewById(R.id.tvCategoryHeader)).setText(
                selected.equals("Tất cả") ? "Danh mục: Tất cả sản phẩm" : "Danh mục: " + selected);
        visibleProducts.clear();
        for (Product product : allProducts) {
            if (selected.equals("Tất cả") || product.getCategory().equalsIgnoreCase(selected)) {
                visibleProducts.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SELECTED_CATEGORY, category);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }

    @Override
    public void onDestroy() {
        databaseExecutor.shutdown();
        super.onDestroy();
    }
}
