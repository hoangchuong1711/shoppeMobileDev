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
    private String parentCategory = "";
    private String productType = "";
    private String searchQuery = "";

    public HomeFragment() { super(R.layout.fragment_home); }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProductActivity activity = (ProductActivity) requireActivity();
        ((TextView) view.findViewById(R.id.tvWelcomeUser))
                .setText("Xin chào, " + activity.getUsername() + "!");
        view.findViewById(R.id.breadcrumbHome).setOnClickListener(v -> activity.showAllProducts());
        GridView grid = view.findViewById(R.id.gvProducts);
        adapter = new ProductAdapter(requireContext(), visibleProducts);
        grid.setEmptyView(view.findViewById(R.id.emptyProducts));
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent, item, position, id) ->
                activity.openProductDetail(visibleProducts.get(position)));
        updateHeader();
        new ProductRepository().getProducts(new ProductRepository.ProductsCallback() {
            @Override public void onSuccess(List<Product> products) {
                if (adapter == null || getView() != view) return;
                allProducts.clear();
                allProducts.addAll(products);
                applyFilter();
            }
            @Override public void onError(Exception error) {
                if (adapter == null || getView() != view) return;
                Toast.makeText(requireContext(),
                        "Không tải được sản phẩm: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showCategory(String category, String type) {
        parentCategory = category;
        productType = type;
        searchQuery = "";
        updateHeader();
        applyFilter();
    }

    public void showAll() {
        parentCategory = "";
        productType = "";
        searchQuery = "";
        updateHeader();
        applyFilter();
    }

    public void filter(String query) {
        searchQuery = query;
        applyFilter();
    }

    private void updateHeader() {
        View root = getView();
        if (root == null) return;
        boolean categoryMode = !productType.isEmpty();
        root.findViewById(R.id.homeIntro).setVisibility(categoryMode ? View.GONE : View.VISIBLE);
        root.findViewById(R.id.categoryIntro).setVisibility(categoryMode ? View.VISIBLE : View.GONE);
        if (categoryMode) {
            ((TextView) root.findViewById(R.id.breadcrumbCategory)).setText("  ›  " + productType);
            ((TextView) root.findViewById(R.id.categoryTitle)).setText(productType);
        }
    }

    private void applyFilter() {
        if (adapter == null) return;
        String keyword = searchQuery.trim().toLowerCase(Locale.ROOT);
        String type = productType.toLowerCase(Locale.ROOT);
        String category = parentCategory.toLowerCase(Locale.ROOT);
        visibleProducts.clear();
        for (Product product : allProducts) {
            String name = product.getName().toLowerCase(Locale.ROOT);
            String productCategory = product.getCategory().toLowerCase(Locale.ROOT);
            boolean matchesSearch = keyword.isEmpty() || name.contains(keyword)
                    || productCategory.contains(keyword);
            boolean matchesType = type.isEmpty() || name.contains(type)
                    || productCategory.contains(type);
            // Danh mục rộng trên Firebase vẫn được dùng khi chưa có tên loại chi tiết.
            if (!matchesType && !category.isEmpty() && type.equals(category))
                matchesType = productCategory.contains(category);
            if (matchesSearch && matchesType) visibleProducts.add(product);
        }
        adapter.notifyDataSetChanged();
        View root = getView();
        if (root != null && !productType.isEmpty()) {
            ((TextView) root.findViewById(R.id.resultCount))
                    .setText(visibleProducts.size() + " sản phẩm");
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }
}
