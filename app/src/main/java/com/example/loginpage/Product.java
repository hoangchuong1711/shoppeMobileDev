package com.example.loginpage;

public class Product {
    private final String name;
    private final String price;
    private final String description;
    private final int imageResId;
    private final String category;
    private final String imageUrl;
    private boolean checked;

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
}
