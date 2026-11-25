package com.example.loginpage.model;

public class Product {
    private final String id, name, price, description, category, imageUrl;
    private final int imageResId;
    private String size = "";
    private boolean checked = true;
    private int quantity = 1;

    public Product(String id, String name, String price, String description, int imageResId,
                   String category, String imageUrl) {
        this.id = id; this.name = name; this.price = price; this.description = description;
        this.imageResId = imageResId; this.category = category; this.imageUrl = imageUrl;
    }
    public Product(String name, String price, String description, int imageResId,
                   String category, String imageUrl) {
        this(name, name, price, description, imageResId, category, imageUrl);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public long getPriceValue() {
        try { return Long.parseLong(price.replaceAll("[^0-9]", "")); }
        catch (NumberFormatException ex) { return 0; }
    }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
    public int getQuantity() { return quantity; }
    public void increaseQuantity() { quantity++; }
    public void decreaseQuantity() { if (quantity > 1) quantity--; }
}
