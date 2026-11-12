package com.example.loginpage;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class ProductCatalog {
    private ProductCatalog() {}

    // Sản phẩm mẫu chỉ được thêm khi AppDB tạo database lần đầu.
    static void seedProducts(SQLiteDatabase db) {
        insertProduct(db, "Áo Thun Polo Nam", 189000, "Chất vải cá sấu thoáng mát, chuẩn form", android.R.drawable.ic_menu_gallery, "Thời trang", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVCUwQlCrG_dYlPHzCT7A5PijYXgzfrYPBLqsCxDakPeAfDSvd0PdNf_Hl&s=10");
        insertProduct(db, "Giày Sneaker Trắng", 350000, "Đế êm chống trượt, phong cách Hàn Quốc", android.R.drawable.ic_menu_compass, "Thời trang", "https://www.jousenshoes.com/cdn/shop/files/7_5c6cfdce-26b5-4652-b5e2-86ad2bb6fae5_2048x.jpg?v=1756452913");
        insertProduct(db, "Túi Đeo Chéo Nữ", 245000, "Da PU mềm cao cấp, nhiều ngăn tiện lợi", android.R.drawable.ic_menu_agenda, "Thời trang", "https://kroymela.com/storage/16-Mar-2024/images/1710597125-jjj.jpg");
        insertProduct(db, "Nước Giặt OMO Matic", 165000, "Túi 3.6kg hương tinh dầu thơm lâu", android.R.drawable.ic_menu_manage, "Tiêu dùng", "https://bizweb.dktcdn.net/100/553/841/products/6-2b658f64-7275-41c3-b9e8-9c2f00835d07.png?v=1744444029273");
        insertProduct(db, "Thùng 24 Lon Cà Phê", 210000, "Cà phê sữa đá đậm vị nguyên chất", android.R.drawable.ic_menu_sort_by_size, "Tiêu dùng", "https://cdn.tgdd.vn/Products/Images/8966/195970/bhx/24-lon-ca-phe-sua-georgia-max-coffee-235ml-202103270956586672.jpg");
        insertProduct(db, "Vitamin C DHC Nhật", 125000, "Tăng đề kháng, sáng da gói 60 ngày", android.R.drawable.ic_menu_info_details, "Sức khỏe", "https://product.hstatic.net/200000960342/product/a2bdb0026f7344a997c3cf50dbe4b592_2a21c1d1f6984b86b469255f3422d13b_master.png");
        insertProduct(db, "Máy Đo Huyết Áp", 680000, "Đo bắp tay tự động, màn hình LCD rõ nét", android.R.drawable.ic_menu_recent_history, "Sức khỏe", "https://www.adwdiabetes.com/images/omron-5-series-upper-arm-blood-pressure-monitor-bp7200.jpg");
        insertProduct(db, "iPhone 15 Pro Max", 29990000, "Chip A17 Pro, bộ nhớ 256GB chính hãng", android.R.drawable.ic_menu_call, "Công nghệ", "https://media.ldlc.com/r1600/ld/products/00/06/06/44/LD0006064496.jpg");
        insertProduct(db, "Samsung Galaxy S24", 22990000, "Màn hình Dynamic AMOLED 2X 120Hz", android.R.drawable.ic_menu_camera, "Công nghệ", "https://images.samsung.com/is/image/samsung/p6pim/il/2401/gallery/il-galaxy-s24-sm-s921bzvbmec-539295514");
        insertProduct(db, "Tai nghe AirPods Pro 2", 5490000, "Chống ồn chủ động, sạc MagSafe USB-C", android.R.drawable.ic_menu_compass, "Công nghệ", "https://maccenter.vn/Accessories/Airpods-Pro2-USBC.jpg");
    }

    private static void insertProduct(SQLiteDatabase db, String name, long price, String description,
                                      int imageResId, String category, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", description);
        values.put("image_res_id", imageResId);
        values.put("category", category);
        values.put("image_url", imageUrl);
        db.insertOrThrow(AppDB.TABLE_PRODUCTS, null, values);
    }
}
