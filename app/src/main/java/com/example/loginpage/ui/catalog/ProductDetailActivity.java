package com.example.loginpage.ui.catalog;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;
import com.example.loginpage.ui.cart.CartManager;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.bumptech.glide.Glide;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String GUIDE_IMAGE_ONE =
            "https://yame.vn/cdn/shop/files/TheWeekend121Thongs.jpg?v=1794814139&width=1206";
    private static final String GUIDE_IMAGE_TWO =
            "https://yame.vn/cdn/shop/files/AT_LOOSE_FIT_F4_6385d308-d48a-4ab4-9b93-1ac656d30b69.jpg?v=1794814369&width=1206";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name == null) {
            finish();
            return;
        }
        Product product = new Product(intent.getStringExtra("id"), name,
                intent.getStringExtra("price"), intent.getStringExtra("description"),
                intent.getIntExtra("image", android.R.drawable.ic_menu_gallery),
                intent.getStringExtra("category"), intent.getStringExtra("image_url"));
        ((TextView) findViewById(R.id.tvDetailName)).setText(product.getName());
        ((TextView) findViewById(R.id.tvDetailPrice)).setText(product.getPrice());
        ProductDescriptionFormatter.render(this,
                findViewById(R.id.descriptionSections), product.getDescription());
        setupInfoTabs();
        setupImageGallery(product);
        findViewById(R.id.headerMenu).setOnClickListener(v -> {
            Intent menuIntent = new Intent(this, ProductActivity.class);
            menuIntent.putExtra("open_menu", true);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(menuIntent);
            finish();
        });
        findViewById(R.id.headerBrand).setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, ProductActivity.class)
                    .putExtra("reset_home", true).addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
        findViewById(R.id.headerCart).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, com.example.loginpage.ui.cart.CartActivity.class)));
        findViewById(R.id.headerSearch).setOnClickListener(v -> finish());
        findViewById(R.id.detailBack).setOnClickListener(v -> finish());
        ChipGroup sizes = findViewById(R.id.sizeChipGroup);
        findViewById(R.id.btnOrder).setOnClickListener(view -> {
            int id = sizes.getCheckedChipId();
            if (id == android.view.View.NO_ID) {
                Snackbar.make(view, "Vui lòng chọn size", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String size = ((com.google.android.material.chip.Chip) findViewById(id)).getText().toString();
            CartManager.addToCart(product, size);
            Snackbar.make(view, "Đã thêm vào giỏ hàng", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setupInfoTabs() {
        LinearLayout reviews = findViewById(R.id.reviewsPanel);
        LinearLayout faq = findViewById(R.id.faqPanel);
        View description = findViewById(R.id.descriptionSections);

        addReview(reviews, "MA", "Minh Anh", "12/11/2026", "★★★★★",
                "Mẫu nhìn gọn gàng và dễ phối đồ. Mình thích kiểu mặc thoải mái như thế này cho những ngày đi chơi.");
        addReview(reviews, "GH", "Gia Huy", "08/11/2026", "★★★★☆",
                "Màu sắc khá dễ kết hợp với đồ có sẵn. Mình thường xem bảng size trước để chọn phom vừa ý hơn.");

        String[][] questions = {
                {"1. Làm sao chọn size phù hợp?",
                        "Hãy đối chiếu số đo của bạn với ảnh hướng dẫn chọn size phía trên. Bảng size chỉ để tham khảo; mỗi kiểu sản phẩm có thể có phom khác nhau."},
                {"2. Nên giặt và bảo quản sản phẩm như thế nào?",
                        "Bạn nên đọc nhãn chăm sóc trên sản phẩm, lộn trái khi giặt và phơi ở nơi thoáng. Tránh dùng nhiệt độ quá cao để giữ màu và phom dáng."},
                {"3. Màu sắc ngoài thực tế có giống ảnh không?",
                        "Màu sản phẩm có thể chênh lệch nhẹ do ánh sáng lúc chụp và cách màn hình hiển thị. Hãy xem ảnh sản phẩm chính để tham khảo màu."}
        };
        for (String[] question : questions) {
            ProductDescriptionFormatter.addSection(this, faq, question[0], question[1]);
        }

        TabLayout tabs = findViewById(R.id.productInfoTabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                reviews.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                faq.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
                description.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
        TabLayout.Tab descriptionTab = tabs.getTabAt(2);
        if (descriptionTab != null) descriptionTab.select();
    }

    private void addReview(LinearLayout container, String initials, String name,
                           String date, String stars, String comment) {
        View review = LayoutInflater.from(this)
                .inflate(R.layout.item_product_review, container, false);
        ((TextView) review.findViewById(R.id.reviewAvatar)).setText(initials);
        ((TextView) review.findViewById(R.id.reviewName)).setText(name);
        ((TextView) review.findViewById(R.id.reviewDate)).setText(date);
        ((TextView) review.findViewById(R.id.reviewStars)).setText(stars);
        ((TextView) review.findViewById(R.id.reviewBody)).setText(comment);
        container.addView(review);
    }

    private void setupImageGallery(Product product) {
        ImageView mainImage = findViewById(R.id.imgDetailProduct);
        ImageView productThumbnail = findViewById(R.id.thumbProduct);
        ImageView guideOneThumbnail = findViewById(R.id.thumbGuideOne);
        ImageView guideTwoThumbnail = findViewById(R.id.thumbGuideTwo);
        MaterialCardView[] cards = {
                findViewById(R.id.thumbCardProduct),
                findViewById(R.id.thumbCardGuideOne),
                findViewById(R.id.thumbCardGuideTwo)
        };

        ProductImageLoader.load(productThumbnail, product);
        Glide.with(this).load(GUIDE_IMAGE_ONE)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(guideOneThumbnail);
        Glide.with(this).load(GUIDE_IMAGE_TWO)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(guideTwoThumbnail);

        for (int index = 0; index < cards.length; index++) {
            final int selected = index;
            cards[index].setOnClickListener(v -> showImage(selected, product, mainImage, cards));
        }
        showImage(0, product, mainImage, cards);
    }

    private void showImage(int selected, Product product, ImageView mainImage,
                           MaterialCardView[] cards) {
        for (int index = 0; index < cards.length; index++) {
            cards[index].setStrokeColor(getColor(index == selected
                    ? R.color.text_primary : R.color.outline_light));
        }

        if (selected == 0) {
            mainImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ProductImageLoader.load(mainImage, product);
            return;
        }

        mainImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        String url = selected == 1 ? GUIDE_IMAGE_ONE : GUIDE_IMAGE_TWO;
        Glide.with(this).load(url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(mainImage);
    }
}
