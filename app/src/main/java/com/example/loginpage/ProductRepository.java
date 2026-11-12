package com.example.loginpage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ProductRepository {
    private final Context appContext;

    public ProductRepository(Context context) {
        appContext = context.getApplicationContext();
    }

    // Gọi từ luồng nền vì mở database và đọc dữ liệu có thể mất thời gian.
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        NumberFormat priceFormat = NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN"));
        AppDB helper = new AppDB(appContext);
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try (Cursor cursor = db.query(AppDB.TABLE_PRODUCTS,
                    new String[]{"name", "price", "description", "image_res_id", "category", "image_url"},
                    null, null, null, null, "id ASC")) {
                while (cursor.moveToNext()) {
                    products.add(new Product(
                            cursor.getString(0),
                            priceFormat.format(cursor.getLong(1)) + " đ",
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5)));
                }
            }
        } finally {
            helper.close();
        }
        return products;
    }
}
