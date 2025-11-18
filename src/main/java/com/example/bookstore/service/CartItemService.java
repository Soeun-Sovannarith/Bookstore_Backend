package com.example.bookstore.service;

import com.example.bookstore.model.Cart_Item;
import com.example.bookstore.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public Cart_Item createCartItem(Cart_Item cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public List<Cart_Item> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Cart_Item getCartItemById(Integer id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));
    }

    public List<Cart_Item> getCartItemsByUserId(Integer userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public Cart_Item updateCartItem(Integer id, Cart_Item cartItemDetails) {
        Cart_Item cartItem = getCartItemById(id);

        cartItem.setUserId(cartItemDetails.getUserId());
        cartItem.setBookId(cartItemDetails.getBookId());
        cartItem.setQuantity(cartItemDetails.getQuantity());
        cartItem.setCreatedAt(cartItemDetails.getCreatedAt());

        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Integer id) {
        cartItemRepository.deleteById(id);
    }
}

