package com.example.loginpage.ui.catalog;

import com.example.loginpage.R;
import com.example.loginpage.data.ProductRepository;
import com.example.loginpage.model.Product;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

public class CategoryFragment extends Fragment {
    private static final String SELECTED_CATEGORY = "selected_category";
    private static final int[] CATEGORY_IDS = {
            R.id.catAll, R.id.catFashion, R.id.catConsumer, R.id.catHealth, R.id.catTech
    };

    private final List<Product> visibleProducts = new ArrayList<>();
    private final List<Product> allProducts = new ArrayList<>();
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

        ChipGroup chipGroup = view.findViewById(R.id.categoryChipGroup);
        for (int id : CATEGORY_IDS) {
            Chip chip = view.findViewById(id);
            if (category.contentEquals(chip.getText())) {
                chipGroup.check(id);
                break;
            }
        }
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                selectCategory(view, chip.getText().toString());
            }
        });
        selectCategory(view, category);

        new ProductRepository().getProducts(new ProductRepository.ProductsCallback() {
            @Override public void onSuccess(List<Product> products) {
                if (adapter == null || getView() != view) return;
                allProducts.clear();
                allProducts.addAll(products);
                selectCategory(view, category);
            }

            @Override public void onError(Exception error) {
                if (adapter == null || getView() != view) return;
                Toast.makeText(requireContext(),
                        "Không tải được sản phẩm: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Lọc sản phẩm theo chip đang chọn; ChipGroup tự quản lý trạng thái chọn.
    private void selectCategory(View view, String selected) {
        category = selected;
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
}
