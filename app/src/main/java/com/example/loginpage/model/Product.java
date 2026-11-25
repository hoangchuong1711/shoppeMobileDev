package com.example.loginpage.model;

public class Product {
    private final String name;
    private final String price;
    private final String description;
    private final int imageResId;
    private final String category;
    private final String imageUrl;
    private boolean checked;
    private int quantity = 1;

    public Product(String name, String price, String description, int imageResId,
                   String category, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
    public int getQuantity() { return quantity; }
    public void increaseQuantity() { if (quantity < Integer.MAX_VALUE) quantity++; }
    public void decreaseQuantity() { if (quantity > 1) quantity--; }
}
