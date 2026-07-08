package com.ecommerce.order.service;


import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;

    private final ProductServiceClient productServiceClient;
  //  private final UserServiceClient userServiceClient;
    int attempt = 0;

    //    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
   // @Retry(name = "retryBreaker", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(String userId, CartItemRequest request) {
//        System.out.println("ATTEMPT COUNT: " + ++attempt);
        // Look for product
        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
        if(productResponse == null)
            return false;

        if(productResponse.getStockQuantity() < request.getQuantity())
            return false;
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty())
//            return false;

//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem != null) {
            // Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
//        Optional<Product> productOpt = productRepository.findById(productId);
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if(cartItem!=null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
         return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
