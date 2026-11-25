package com.example.loginpage.ui.catalog;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loginpage.R;

import java.util.Locale;
import java.util.function.Consumer;

final class CategoryMenu {
    private static final String[][] GROUPS = {
            {"Áo thun", "Áo Thun Cổ Tròn", "Áo Thun Cổ Polo & Henley", "Áo Thun Tank Top", "Áo Thun Dệt Kim"},
            {"Áo sơ mi", "Áo Sơ Mi Tay Ngắn", "Áo Sơ Mi Tay Dài"},
            {"Áo khoác", "Áo Khoác Dù", "Áo Khoác Thun", "Áo Khoác Jean", "Áo Khoác Kaki"},
            {"Quần jeans", "Quần Jeans Nguyên Bản", "Quần Jeans Ống Rộng"},
            {"Quần dài", "Quần Dài Lưng Gài", "Quần Dài Lưng Thun"},
            {"Quần tây", "Quần Tây Nam"},
            {"Quần short", "Quần Short Lưng Thun", "Quần Short Jeans"}
    };

    private final Context context;
    private final LinearLayout items;
    private final Consumer<String> onCategorySelected;
    private String expandedCategory = "";

    CategoryMenu(Context context, LinearLayout items, Consumer<String> onCategorySelected) {
        this.context = context;
        this.items = items;
        this.onCategorySelected = onCategorySelected;
    }

    void render() {
        items.removeAllViews();
        for (String[] group : GROUPS) {
            String category = group[0];
            TextView heading = new TextView(context);
            heading.setText(category.toUpperCase(Locale.forLanguageTag("vi-VN"))
                    + (category.equals(expandedCategory) ? "    ⌃" : "    ⌄"));
            heading.setTextColor(context.getColor(R.color.text_primary));
            heading.setTextSize(16);
            heading.setGravity(Gravity.CENTER_VERTICAL);
            heading.setPadding(dp(18), 0, dp(18), 0);
            heading.setMinHeight(dp(58));
            heading.setBackgroundResource(android.R.drawable.list_selector_background);
            heading.setOnClickListener(v -> {
                expandedCategory = category.equals(expandedCategory) ? "" : category;
                render();
            });
            items.addView(heading);
            if (!category.equals(expandedCategory)) continue;

            TextView section = new TextView(context);
            section.setText("THEO KIỂU SẢN PHẨM");
            section.setTextSize(11);
            section.setTextColor(context.getColor(R.color.text_secondary));
            section.setPadding(dp(28), dp(14), 0, dp(6));
            items.addView(section);

            for (int i = 1; i < group.length; i++) {
                String type = group[i];
                TextView child = new TextView(context);
                child.setText(type);
                child.setTextColor(context.getColor(R.color.text_body));
                child.setTextSize(14);
                child.setGravity(Gravity.CENTER_VERTICAL);
                child.setPadding(dp(28), 0, dp(12), 0);
                child.setMinHeight(dp(48));
                child.setBackgroundResource(android.R.drawable.list_selector_background);
                child.setOnClickListener(v -> onCategorySelected.accept(type));
                items.addView(child);
            }
        }
    }

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density);
    }
}
