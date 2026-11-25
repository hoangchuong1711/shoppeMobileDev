package com.example.loginpage.ui.cart;

import com.example.loginpage.model.Product;

import java.util.ArrayList;
import java.util.Objects;

public final class CartManager {
    private static final ArrayList<Product> PRODUCTS = new ArrayList<>();

    private CartManager() {}

    public static ArrayList<Product> getCartList() {
        return PRODUCTS;
    }

    public static void addToCart(Product product) {
        for (Product item : PRODUCTS) {
            if (item.getName().equals(product.getName())
                    && item.getCategory().equals(product.getCategory())
                    && item.getPrice().equals(product.getPrice())
                    && Objects.equals(item.getImageUrl(), product.getImageUrl())) {
                item.increaseQuantity();
                return;
            }
        }
        PRODUCTS.add(new Product(product.getName(), product.getPrice(),
                product.getDescription(), product.getImageResId(),
                product.getCategory(), product.getImageUrl()));
    }

    public static void removeFromCart(Product product) {
        PRODUCTS.remove(product);
    }

    public static int getItemCount() {
        int count = 0;
        for (Product product : PRODUCTS) count += product.getQuantity();
        return count;
    }
}
