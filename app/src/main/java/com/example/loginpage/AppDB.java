package com.example.loginpage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class AppDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_PRODUCTS = "products";

    public AppDB(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "price INTEGER NOT NULL, "
                + "description TEXT NOT NULL, "
                + "image_res_id INTEGER NOT NULL, "
                + "category TEXT NOT NULL, "
                + "image_url TEXT NOT NULL)");
        ProductCatalog.seedProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Khi thêm bảng hoặc đổi schema, tăng DATABASE_VERSION và migration tại đây.
    }
}
