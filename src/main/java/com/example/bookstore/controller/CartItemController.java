package com.example.bookstore.controller;

import com.example.bookstore.model.Cart_Item;
import com.example.bookstore.service.CartItemService;
import com.example.bookstore.security.ApiKeyRequired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @ApiKeyRequired
    @PostMapping
    public Cart_Item createCartItem(@RequestBody Cart_Item cartItem) {
        return cartItemService.createCartItem(cartItem);
    }

    @GetMapping
    public List<Cart_Item> getAllCartItems() {
        return cartItemService.getAllCartItems();
    }

    @GetMapping("/{id}")
    public Cart_Item getCartItemById(@PathVariable Integer id) {
        return cartItemService.getCartItemById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Cart_Item> getCartItemsByUserId(@PathVariable Integer userId) {
        return cartItemService.getCartItemsByUserId(userId);
    }

    @ApiKeyRequired
    @PutMapping("/{id}")
    public Cart_Item updateCartItem(@PathVariable Integer id, @RequestBody Cart_Item cartItem) {
        return cartItemService.updateCartItem(id, cartItem);
    }

    @ApiKeyRequired
    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Integer id) {
        cartItemService.deleteCartItem(id);
    }
}
