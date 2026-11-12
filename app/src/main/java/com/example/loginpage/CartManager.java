package com.example.loginpage;

import java.util.ArrayList;

public final class CartManager {
    private static final ArrayList<Product> PRODUCTS = new ArrayList<>();

    private CartManager() {}

    public static ArrayList<Product> getCartList() {
        return PRODUCTS;
    }

    // Tạo bản sao để trạng thái tích chọn trong giỏ không ảnh hưởng sản phẩm gốc.
    public static void addToCart(Product product) {
        PRODUCTS.add(new Product(product.getName(), product.getPrice(),
                product.getDescription(), product.getImageResId(),
                product.getCategory(), product.getImageUrl()));
    }
}
