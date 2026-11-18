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

                        products.add(new Product(
                                name,
                                priceFormat.format(price) + " đ",
                                description,
                                android.R.drawable.ic_menu_gallery,
                                category,
                                imageUrl));
                    }
                    callback.onSuccess(products);
                })
                .addOnFailureListener(callback::onError);
    }
}
