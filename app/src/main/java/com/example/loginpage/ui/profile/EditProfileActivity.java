package com.example.loginpage.ui.profile;

import com.example.loginpage.R;
import com.google.android.material.textfield.TextInputLayout;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        EditText name = findViewById(R.id.name);
        EditText phone = findViewById(R.id.phone);
        EditText address = findViewById(R.id.address);
        name.setText(ProfileStore.get(this, "name"));
        phone.setText(ProfileStore.get(this, "phone"));
        address.setText(ProfileStore.get(this, "address"));
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.save).setOnClickListener(v -> {
            String fullName = name.getText().toString().trim();
            String phoneNumber = phone.getText().toString().trim();
            String deliveryAddress = address.getText().toString().trim();
            ((TextInputLayout) findViewById(R.id.nameLayout)).setError(fullName.isEmpty() ? "Nhập họ và tên" : null);
            ((TextInputLayout) findViewById(R.id.phoneLayout)).setError(phoneNumber.matches("0[0-9]{9}") ? null : "Nhập số điện thoại 10 số");
            ((TextInputLayout) findViewById(R.id.addressLayout)).setError(deliveryAddress.isEmpty() ? "Nhập địa chỉ" : null);
            if (fullName.isEmpty() || !phoneNumber.matches("0[0-9]{9}") || deliveryAddress.isEmpty()) return;
            ProfileStore.save(this, fullName, phoneNumber, deliveryAddress);
            Toast.makeText(this, "Đã cập nhật thông tin nhận hàng", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
