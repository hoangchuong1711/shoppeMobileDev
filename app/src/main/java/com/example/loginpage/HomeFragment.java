package com.example.loginpage;

import android.content.Context;
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
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private final List<Product> visibleProducts = new ArrayList<>();
    private final List<Product> allProducts = new ArrayList<>();
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private ProductAdapter adapter;

    public HomeFragment() { super(R.layout.fragment_home); }

    // Gắn danh sách vào GridView và mở chi tiết khi chọn sản phẩm.
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProductActivity activity = (ProductActivity) requireActivity();
        ((TextView) view.findViewById(R.id.tvWelcomeUser))
                .setText("Xin chào, " + activity.getUsername() + "!");

        GridView grid = view.findViewById(R.id.gvProducts);
        adapter = new ProductAdapter(requireContext(), visibleProducts);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent, item, position, id) ->
                activity.openProductDetail(visibleProducts.get(position)));
        Context appContext = requireContext().getApplicationContext();
        databaseExecutor.execute(() -> {
            List<Product> products = new ProductRepository(appContext).getProducts();
            new Handler(Looper.getMainLooper()).post(() -> {
                if (adapter == null || getView() != view) return;
                allProducts.clear();
                allProducts.addAll(products);
                filter(activity.getSearchQuery());
            });
        });
    }

    // Lọc theo tên hoặc danh mục, rồi báo Adapter vẽ lại.
    public void filter(String query) {
        if (adapter == null) return;
        String keyword = query.trim().toLowerCase(Locale.ROOT);
        visibleProducts.clear();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase(Locale.ROOT).contains(keyword)
                    || product.getCategory().toLowerCase(Locale.ROOT).contains(keyword)) {
                visibleProducts.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }

    @Override public void onDestroy() {
        databaseExecutor.shutdown();
        super.onDestroy();
    }
}
