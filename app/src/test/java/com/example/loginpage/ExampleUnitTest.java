package com.example.loginpage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.loginpage.model.Product;
import com.example.loginpage.ui.cart.CartManager;

import org.junit.After;
import org.junit.Test;

public class ExampleUnitTest {
    @After
    public void clearCart() {
        CartManager.clear();
    }

    @Test
    public void cartGroupsSameProductAndSize() {
        Product shirt = new Product("shirt-1", "Áo thun", "199.000 đ", "",
                0, "Áo thun", null);

        CartManager.addToCart(shirt, "M");
        CartManager.addToCart(shirt, "M");
        CartManager.addToCart(shirt, "L");

        assertEquals(2, CartManager.getCartList().size());
        assertEquals(2, CartManager.getCartList().get(0).getQuantity());
        assertEquals(597000, CartManager.getSelectedTotal());

        CartManager.getCartList().get(0).setChecked(false);
        assertTrue(CartManager.hasSelection());
        assertEquals(199000, CartManager.getSelectedTotal());

        CartManager.removeSelected();
        assertEquals(1, CartManager.getCartList().size());
        assertFalse(CartManager.hasSelection());
    }
}
