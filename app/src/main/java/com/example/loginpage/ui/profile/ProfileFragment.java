package com.example.loginpage.ui.profile;

import com.example.loginpage.R;
import com.example.loginpage.ui.cart.CartManager;
import com.example.loginpage.ui.catalog.ProductActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    public ProfileFragment() { super(R.layout.fragment_profile); }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        ProductActivity activity = (ProductActivity) requireActivity();

        view.findViewById(R.id.editProfile).setOnClickListener(v ->
                startActivity(new Intent(activity, EditProfileActivity.class)));
        view.findViewById(R.id.allOrders).setOnClickListener(v ->
                startActivity(new Intent(activity, OrdersActivity.class)));
        view.findViewById(R.id.recentOrder).setOnClickListener(v ->
                startActivity(new Intent(activity, OrdersActivity.class)));
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            CartManager.clear();
            activity.finish();
        });
        refresh();
    }

    @Override public void onResume() { super.onResume(); refresh(); }
    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) refresh();
    }

    private void refresh() {
        View view = getView();
        if (view == null) return;

        //tài khoản
        String username = ((ProductActivity) requireActivity()).getUsername();
        String name = ProfileStore.get(requireContext(), "name");
        ((TextView) view.findViewById(R.id.tvProfileName))
                .setText(name.isEmpty() ? username : name);
        ((TextView) view.findViewById(R.id.profileUsername)).setText("@" + username);
        ((TextView) view.findViewById(R.id.profileAvatar)).setText(
                (name.isEmpty() ? username : name).substring(0, 1).toUpperCase(Locale.getDefault()));

        //thông tin nhận hàng
        String phone = ProfileStore.get(requireContext(), "phone");
        String address = ProfileStore.get(requireContext(), "address");
        ((TextView) view.findViewById(R.id.profileInfo)).setText(
                name.isEmpty() || phone.isEmpty() || address.isEmpty()
                        ? "Thêm thông tin nhận hàng để đặt đơn nhanh hơn."
                        : name + "  ·  " + phone + "\n" + address);

        //đơn hàng
        JSONArray orders = ProfileStore.orders(requireContext());
        JSONObject latest = orders.optJSONObject(0);
        ((TextView) view.findViewById(R.id.recentOrder)).setText(latest == null
                ? "Chưa có đơn hàng. Các đơn đã đặt sẽ xuất hiện tại đây."
                : orders.length() + " đơn hàng\nGần nhất: #" + latest.optString("id")
                + "  ·  " + latest.optString("date") + "\n" + latest.optString("total") + " ₫");
    }
}
