package com.example.loginpage;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public final class ProductImageLoader {
    private ProductImageLoader() {}

    // Tải ảnh qua Glide, dùng icon dự phòng nếu thiếu URL hoặc tải lỗi.
    public static void load(ImageView imageView, Product product) {
        String url = product.getImageUrl();
        int fallback = product.getImageResId();
        if (url == null || url.isEmpty()) {
            Glide.with(imageView).clear(imageView);
            imageView.setImageResource(fallback);
        } else {
            Glide.with(imageView)
                    .load(url)
                    .placeholder(fallback)
                    .error(fallback)
                    .into(imageView);
        }
    }
}
