package org.example.backend.controller.cart;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.cart.CartDeleteDTO;
import org.example.backend.dto.cart.CartItemDto;
import org.example.backend.dto.Response;
import org.example.backend.service.cart.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll() {
        List<CartItemDto> items = cartItemService.findAll();
        return ResponseEntity.ok(new Response<>("success", items, "Cart items retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        CartItemDto item = cartItemService.findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
        return ResponseEntity.ok(new Response<>("success", item, "Cart item retrieved successfully"));
    }

    @PostMapping("/{cartId}/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> create(@PathVariable Long cartId, @RequestBody CartItemDto dto) {
        CartItemDto saved = cartItemService.save(cartId,dto);
        return ResponseEntity.ok(new Response<>("success", saved, "Cart item created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CartItemDto dto) {
        CartItemDto updated = cartItemService.updateById(id, dto);
        return ResponseEntity.ok(new Response<>("success", updated, "Cart item updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        cartItemService.deleteById(id);
        return ResponseEntity.ok(new Response<>("success", null, "Cart item deleted successfully"));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteItems(@RequestBody CartDeleteDTO request) {
        if (request.getItemIds() != null && !request.getItemIds().isEmpty()) {
            // xóa nhiều item
            cartItemService.deleteByIds(request.getItemIds());
            return ResponseEntity.ok(new Response<>("success", null, "Xóa nhiều item thành công"));
        } else if (request.getCartId() != null) {
            // xóa toàn bộ cart
            cartItemService.clearCart(request.getCartId());
            return ResponseEntity.ok(new Response<>("success", null, "Đã xóa toàn bộ giỏ hàng"));
        }
        return ResponseEntity.badRequest()
                .body(new Response<>("error", null, "Thiếu thông tin để xóa"));
    }
}
