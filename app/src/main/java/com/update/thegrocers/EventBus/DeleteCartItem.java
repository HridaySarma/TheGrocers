package com.update.thegrocers.EventBus;


import com.update.thegrocers.Database.CartItem;

public class DeleteCartItem {
    boolean success;
    CartItem cartItem;

    public DeleteCartItem(boolean success, CartItem cartItem) {
        this.success = success;
        this.cartItem = cartItem;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}
