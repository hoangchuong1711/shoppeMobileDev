package com.example.loginpage.data;

import com.example.loginpage.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ProductRepository {
    public interface ProductsCallback {
        void onSuccess(List<Product> products);
        void onError(Exception error);
    }

    public void getProducts(ProductsCallback callback) {
        FirebaseFirestore.getInstance()
                .collection("products")
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Product> products = new ArrayList<>();
                    NumberFormat priceFormat =
                            NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN"));

                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        String name = document.getString("name");
                        Long price = document.getLong("price");
                        String description = document.getString("description");
                        String category = document.getString("category");
                        String imageUrl = document.getString("image_url");

                        if (name == null || price == null || description == null
                                || category == null || imageUrl == null) {
                            callback.onError(new IllegalStateException(
                                    "Sản phẩm " + document.getId() + " thiếu field hoặc sai kiểu dữ liệu"));
                            return;
                        }

                        if (!isClothing(category, name)) continue;
                        products.add(new Product(
                                document.getId(), name,
                                priceFormat.format(price) + " đ",
                                description,
                                android.R.drawable.ic_menu_gallery,
                                category,
                                imageUrl));
                    }
                    if (products.isEmpty()) products.addAll(sampleProducts());
                    callback.onSuccess(products);
                })
                .addOnFailureListener(callback::onError);
    }
    private boolean isClothing(String category, String name) {
        String value = (category + " " + name).toLowerCase(Locale.ROOT);
        return value.contains("áo") || value.contains("quần") || value.contains("thời trang")
                || value.contains("khoác") || value.contains("shirt") || value.contains("jean");
    }

    private List<Product> sampleProducts() {
        List<Product> sample = new ArrayList<>();
        String[][] rows = {
                {"Áo Thun Cổ Tròn Basic", "Áo thun", "Cotton mềm, dễ phối đồ", "199000"},
                {"Áo Thun Cổ Polo & Henley", "Áo thun", "Phom rộng thoải mái", "249000"},
                {"Áo Sơ Mi Tay Ngắn", "Áo sơ mi", "Phù hợp đi học và đi chơi", "299000"},
                {"Áo Sơ Mi Tay Dài", "Áo sơ mi", "Vải nhẹ, ít nhăn", "329000"},
                {"Quần Jeans Nguyên Bản", "Quần", "Chất denim bền đẹp", "399000"},
                {"Quần Dài Lưng Gài", "Quần", "Dễ mặc hằng ngày", "359000"},
                {"Áo Khoác Jean", "Áo khoác", "Lớp ngoài năng động", "459000"},
                {"Áo Khoác Dù", "Áo khoác", "Gọn nhẹ, tiện dụng", "429000"}
        };
        for (int i = 0; i < rows.length; i++) {
            sample.add(new Product("sample-" + i, rows[i][0],
                    NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN")).format(Long.parseLong(rows[i][3])) + " ₫",
                    rows[i][2], android.R.drawable.ic_menu_gallery, rows[i][1], null));
        }
        return sample;
    }
}

