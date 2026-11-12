package com.example.loginpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    public ProfileFragment() { super(R.layout.fragment_profile); }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProductActivity activity = (ProductActivity) requireActivity();
        String username = activity.getUsername();
        ((TextView) view.findViewById(R.id.tvProfileName)).setText(username);
        ((TextView) view.findViewById(R.id.tvProfileInfoName)).setText("• Tên tài khoản: " + username);
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> activity.finish());
    }
}
