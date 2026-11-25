package com.example.loginpage.ui.cart;

import com.example.loginpage.model.Product;
import java.util.ArrayList;

public final class CartManager {
    private static final ArrayList<Product> PRODUCTS = new ArrayList<>();
    private CartManager() {}
    public static ArrayList<Product> getCartList() { return PRODUCTS; }
    public static void addToCart(Product product, String size) {
        for (Product item : PRODUCTS) {
            if (item.getId().equals(product.getId()) && item.getSize().equals(size)) {
                item.increaseQuantity();
                return;
            }
        }
        Product item = new Product(product.getId(), product.getName(), product.getPrice(),
                product.getDescription(), product.getImageResId(), product.getCategory(), product.getImageUrl());
        item.setSize(size);
        PRODUCTS.add(item);
    }
    public static void removeFromCart(Product product) { PRODUCTS.remove(product); }
    public static int getItemCount() {
        int count = 0;
        for (Product product : PRODUCTS) count += product.getQuantity();
        return count;
    }
    public static long getSelectedTotal() {
        long total = 0;
        for (Product product : PRODUCTS) if (product.isChecked()) total += product.getPriceValue() * product.getQuantity();
        return total;
    }
    public static boolean hasSelection() {
        for (Product product : PRODUCTS) if (product.isChecked()) return true;
        return false;
    }
    public static void removeSelected() { PRODUCTS.removeIf(Product::isChecked); }
}
