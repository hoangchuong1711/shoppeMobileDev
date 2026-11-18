package com.example.loginpage.ui.cart;

import com.example.loginpage.model.Product;

import java.util.ArrayList;

public final class CartManager {
    private static final ArrayList<Product> PRODUCTS = new ArrayList<>();

    private CartManager() {}

    public static ArrayList<Product> getCartList() {
        return PRODUCTS;
    }

    public static void addToCart(Product product) {
        PRODUCTS.add(new Product(product.getName(), product.getPrice(),
                product.getDescription(), product.getImageResId(),
                product.getCategory(), product.getImageUrl()));
    }
}
