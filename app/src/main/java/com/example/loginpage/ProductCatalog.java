package com.example.loginpage;

import java.util.ArrayList;
import java.util.List;

public final class ProductCatalog {
    private ProductCatalog() {}

    // Danh sách sản phẩm mẫu dùng chung cho Home và Danh mục.
    public static List<Product> getProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();
        // Danh mục: Thời trang
        allProducts.add(new Product("Áo Thun Polo Nam", "189.000 đ", "Chất vải cá sấu thoáng mát, chuẩn form", android.R.drawable.ic_menu_gallery, "Thời trang", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVCUwQlCrG_dYlPHzCT7A5PijYXgzfrYPBLqsCxDakPeAfDSvd0PdNf_Hl&s=10"));
        allProducts.add(new Product("Giày Sneaker Trắng", "350.000 đ", "Đế êm chống trượt, phong cách Hàn Quốc", android.R.drawable.ic_menu_compass, "Thời trang", "https://www.jousenshoes.com/cdn/shop/files/7_5c6cfdce-26b5-4652-b5e2-86ad2bb6fae5_2048x.jpg?v=1756452913"));
        allProducts.add(new Product("Túi Đeo Chéo Nữ", "245.000 đ", "Da PU mềm cao cấp, nhiều ngăn tiện lợi", android.R.drawable.ic_menu_agenda, "Thời trang", "https://kroymela.com/storage/16-Mar-2024/images/1710597125-jjj.jpg"));

        // Danh mục: Tiêu dùng
        allProducts.add(new Product("Nước Giặt OMO Matic", "165.000 đ", "Túi 3.6kg hương tinh dầu thơm lâu", android.R.drawable.ic_menu_manage, "Tiêu dùng", "https://bizweb.dktcdn.net/100/553/841/products/6-2b658f64-7275-41c3-b9e8-9c2f00835d07.png?v=1744444029273"));
        allProducts.add(new Product("Thùng 24 Lon Cà Phê", "210.000 đ", "Cà phê sữa đá đậm vị nguyên chất", android.R.drawable.ic_menu_sort_by_size, "Tiêu dùng", "https://cdn.tgdd.vn/Products/Images/8966/195970/bhx/24-lon-ca-phe-sua-georgia-max-coffee-235ml-202103270956586672.jpg"));

        // Danh mục: Sức khỏe
        allProducts.add(new Product("Vitamin C DHC Nhật", "125.000 đ", "Tăng đề kháng, sáng da gói 60 ngày", android.R.drawable.ic_menu_info_details, "Sức khỏe", "https://product.hstatic.net/200000960342/product/a2bdb0026f7344a997c3cf50dbe4b592_2a21c1d1f6984b86b469255f3422d13b_master.png"));
        allProducts.add(new Product("Máy Đo Huyết Áp", "680.000 đ", "Đo bắp tay tự động, màn hình LCD rõ nét", android.R.drawable.ic_menu_recent_history, "Sức khỏe", "https://www.adwdiabetes.com/images/omron-5-series-upper-arm-blood-pressure-monitor-bp7200.jpg"));

        // Danh mục: Công nghệ
        allProducts.add(new Product("iPhone 15 Pro Max", "29.990.000 đ", "Chip A17 Pro, bộ nhớ 256GB chính hãng", android.R.drawable.ic_menu_call, "Công nghệ", "https://media.ldlc.com/r1600/ld/products/00/06/06/44/LD0006064496.jpg"));
        allProducts.add(new Product("Samsung Galaxy S24", "22.990.000 đ", "Màn hình Dynamic AMOLED 2X 120Hz", android.R.drawable.ic_menu_camera, "Công nghệ", "https://images.samsung.com/is/image/samsung/p6pim/il/2401/gallery/il-galaxy-s24-sm-s921bzvbmec-539295514"));
        allProducts.add(new Product("Tai nghe AirPods Pro 2", "5.490.000 đ", "Chống ồn chủ động, sạc MagSafe USB-C", android.R.drawable.ic_menu_compass, "Công nghệ", "https://maccenter.vn/Accessories/Airpods-Pro2-USBC.jpg"));
        return allProducts;
    }
}
