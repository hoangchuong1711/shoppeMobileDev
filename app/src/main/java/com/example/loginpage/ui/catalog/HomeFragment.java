package com.example.loginpage.ui.catalog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpage.R;
import com.example.loginpage.data.ProductRepository;
import com.example.loginpage.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private final List<Product> allProducts = new ArrayList<>();
    private final List<Product> visibleProducts = new ArrayList<>();
    private ProductAdapter adapter;
    private String productType = "";
    private String searchQuery = "";
    private boolean productsLoaded;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProductActivity activity = (ProductActivity) requireActivity();
        RecyclerView productList = view.findViewById(R.id.productList);

        adapter = new ProductAdapter(visibleProducts, activity.getUsername(),
                activity::openProductDetail, v -> activity.showAllProducts());
        GridLayoutManager layout = new GridLayoutManager(requireContext(), 2);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == 1 ? 1 : 2;
            }
        });
        productList.setLayoutManager(layout);
        productList.setAdapter(adapter);
        updateHeader();

        new ProductRepository().getProducts(new ProductRepository.ProductsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                if (adapter == null || getView() != view) return;
                allProducts.clear();
                allProducts.addAll(products);
                productsLoaded = true;
                applyFilter();
            }

            @Override
            public void onError(Exception error) {
                if (adapter == null || getView() != view) return;
                productsLoaded = true;
                applyFilter();
                Toast.makeText(requireContext(),
                        "Không tải được sản phẩm: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showCategory(String type) {
        productType = type;
        searchQuery = "";
        updateHeader();
        applyFilter();
        scrollToTop();
    }

    public void showAll() {
        productType = "";
        searchQuery = "";
        updateHeader();
        applyFilter();
        scrollToTop();
    }

    public void filter(String query) {
        searchQuery = query;
        applyFilter();
    }

    private void updateHeader() {
        if (adapter != null) adapter.setHeader(productType);
    }

    private void scrollToTop() {
        View view = getView();
        if (view != null) {
            ((RecyclerView) view.findViewById(R.id.productList)).scrollToPosition(0);
        }
    }

    private void applyFilter() {
        if (adapter == null) return;

        String keyword = searchQuery.trim().toLowerCase(Locale.ROOT);
        String type = productType.toLowerCase(Locale.ROOT);
        visibleProducts.clear();

        for (Product product : allProducts) {
            String name = product.getName().toLowerCase(Locale.ROOT);
            String productCategory = product.getCategory().toLowerCase(Locale.ROOT);
            boolean matchesSearch = keyword.isEmpty() || name.contains(keyword)
                    || productCategory.contains(keyword);
            boolean matchesType = type.isEmpty() || name.contains(type)
                    || productCategory.contains(type);

            if (matchesSearch && matchesType) {
                visibleProducts.add(product);
            }
        }

        adapter.setLoaded(productsLoaded);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }
}
