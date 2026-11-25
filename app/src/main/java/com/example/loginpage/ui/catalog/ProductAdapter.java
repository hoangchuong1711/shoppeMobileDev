package com.example.loginpage.ui.catalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpage.R;
import com.example.loginpage.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER = 0;
    private static final int PRODUCT = 1;
    private static final int EMPTY = 2;

    public interface ProductClickListener {
        void onProductClick(Product product);
    }

    private final List<Product> products;
    private final String username;
    private final ProductClickListener productClickListener;
    private final View.OnClickListener homeClickListener;
    private String productType = "";
    private boolean loaded;

    public ProductAdapter(List<Product> products, String username,
                          ProductClickListener productClickListener,
                          View.OnClickListener homeClickListener) {
        this.products = products;
        this.username = username;
        this.productClickListener = productClickListener;
        this.homeClickListener = homeClickListener;
    }

    public void setHeader(String productType) {
        this.productType = productType;
        notifyItemChanged(0);
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return HEADER;
        if (products.isEmpty()) return EMPTY;
        return PRODUCT;
    }

    @Override
    public int getItemCount() {
        return 1 + products.size() + (loaded && products.isEmpty() ? 1 : 0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == HEADER) {
            View view = inflater.inflate(R.layout.home_list_header, parent, false);
            return new HeaderHolder(view);
        }
        if (viewType == EMPTY) {
            TextView view = new TextView(parent.getContext());
            view.setText("Chưa có sản phẩm trong loại này");
            view.setTextSize(15);
            view.setGravity(android.view.Gravity.CENTER);
            view.setMinHeight((int) (160 * parent.getResources().getDisplayMetrics().density));
            return new EmptyHolder(view);
        }
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            View view = holder.itemView;
            boolean categoryMode = !productType.isEmpty();
            view.findViewById(R.id.homeIntro).setVisibility(categoryMode ? View.GONE : View.VISIBLE);
            view.findViewById(R.id.categoryIntro).setVisibility(categoryMode ? View.VISIBLE : View.GONE);
            ((TextView) view.findViewById(R.id.tvWelcomeUser)).setText("Xin chào, " + username + "!");
            ((TextView) view.findViewById(R.id.breadcrumbCategory)).setText("  ›  " + productType);
            ((TextView) view.findViewById(R.id.categoryTitle)).setText(productType);
            ((TextView) view.findViewById(R.id.resultCount)).setText(products.size() + " sản phẩm");
            view.findViewById(R.id.breadcrumbHome).setOnClickListener(homeClickListener);
            return;
        }
        if (!(holder instanceof ProductHolder)) return;
        Product product = products.get(position - 1);
        View view = holder.itemView;
        ProductImageLoader.load(view.findViewById(R.id.imgProduct), product);
        ((TextView) view.findViewById(R.id.tvProductName)).setText(product.getName());
        ((TextView) view.findViewById(R.id.tvProductPrice)).setText(product.getPrice());
        ((TextView) view.findViewById(R.id.tvProductDesc)).setText(product.getDescription());
        view.setOnClickListener(v -> productClickListener.onProductClick(product));
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {
        HeaderHolder(View view) { super(view); }
    }

    private static class ProductHolder extends RecyclerView.ViewHolder {
        ProductHolder(View view) { super(view); }
    }

    private static class EmptyHolder extends RecyclerView.ViewHolder {
        EmptyHolder(View view) { super(view); }
    }
}
