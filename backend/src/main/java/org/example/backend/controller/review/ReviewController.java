package org.example.backend.controller.review;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.Response;
import org.example.backend.dto.review.ReviewDto;
import org.example.backend.service.review.ReviewService;
import org.example.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll() {
        List<ReviewDto> list = reviewService.findAll();
        return ResponseEntity.ok(new Response<>("success", list, "Reviews retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ReviewDto dto = reviewService.getById(id);
        return ResponseEntity.ok(new Response<>("success", dto, "Review retrieved successfully"));
    }

    @PostMapping("/{menuId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> create(@PathVariable Long menuId,
                                    @RequestBody ReviewDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long userId = userService.getUserByEmail(email).getId();

        boolean alreadyReviewed = reviewService.existsByUserIdAndMenuId(userId, menuId);
        if (alreadyReviewed) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response<>("error", null, "Bạn chỉ được đánh giá món này 1 lần"));
        }

        ReviewDto saved = reviewService.save(dto, userId, menuId);
        return ResponseEntity.ok(new Response<>("success", saved, "Review created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReviewDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long currentUserId = userService.getUserByEmail(email).getId();

        ReviewDto updated = reviewService.updateById(id, dto, currentUserId);
        return ResponseEntity.ok(new Response<>("success", updated, "Review updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long currentUserId = userService.getUserByEmail(email).getId();

        reviewService.deleteById(id, currentUserId);
        return ResponseEntity.ok(new Response<>("success", null, "Review deleted successfully"));
    }

}
