package com.client.thegrocers.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalCartDataSource implements CartDataSource {

    private CartDao cartDao;

    public LocalCartDataSource(CartDao cartDao){
        this.cartDao = cartDao;
    }

    @Override
    public Flowable<List<CartItem>> getAllCart(String uid) {
        return cartDao.getAllCart(uid);
    }

    @Override
    public Single<Integer> countItemInCart(String uid) {
        return cartDao.countItemInCart(uid);
    }

    @Override
    public Single<Double> sumPriceInCart(String uid) {
        return cartDao.sumPriceInCart(uid);
    }

    @Override
    public Single<Double> sumWeightInCart(String uid) {
        return cartDao.sumWeightInCart(uid);
    }

    @Override
    public Single<Double> sumHeightInCart(String uid) {
        return cartDao.sumHeightInCart(uid);
    }

    @Override
    public Single<CartItem> getItemInCart(String productId, String uid) {
        return cartDao.getItemInCart(productId,uid);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItems) {
        return cartDao.insertOrReplaceAll(cartItems);
    }

    @Override
    public Single<Integer> updateCartItems(CartItem cartItem) {
        return cartDao.updateCartItems(cartItem);
    }

    @Override
    public Single<Integer> deleteCartItem(CartItem cartItem) {
        return cartDao.deleteCartItem(cartItem);
    }

    @Override
    public Single<Integer> cleanCart(String uid) {
        return cartDao.cleanCart(uid);
    }

    @Override
    public Single<CartItem> getItemWithAllOptionsInCart(String uid, String productId) {
        return cartDao.getItemWithAllOptionsInCart(uid,productId);
    }
}
