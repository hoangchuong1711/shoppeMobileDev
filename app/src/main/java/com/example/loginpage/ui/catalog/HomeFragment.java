package com.example.loginpage.ui.catalog;

import com.example.loginpage.R;
import com.example.loginpage.data.ProductRepository;
import com.example.loginpage.model.Product;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private final List<Product> visibleProducts = new ArrayList<>();
    private final List<Product> allProducts = new ArrayList<>();
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

        new ProductRepository().getProducts(new ProductRepository.ProductsCallback() {
            @Override public void onSuccess(List<Product> products) {
                if (adapter == null || getView() != view) return;
                allProducts.clear();
                allProducts.addAll(products);
                filter(activity.getSearchQuery());
            }

            @Override public void onError(Exception error) {
                if (adapter == null || getView() != view) return;
                Toast.makeText(requireContext(),
                        "Không tải được sản phẩm: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
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
}
