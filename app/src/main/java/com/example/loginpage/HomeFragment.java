package com.example.loginpage;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private final List<Product> visibleProducts = new ArrayList<>();
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
        filter(activity.getSearchQuery());
    }

    // Lọc theo tên hoặc danh mục, rồi báo Adapter vẽ lại.
    public void filter(String query) {
        if (adapter == null) return;
        String keyword = query.trim().toLowerCase(Locale.ROOT);
        visibleProducts.clear();
        for (Product product : ProductCatalog.getProducts()) {
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
}
