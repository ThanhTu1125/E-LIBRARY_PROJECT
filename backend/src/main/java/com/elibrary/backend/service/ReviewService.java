package com.elibrary.backend.service;

import com.elibrary.backend.dto.ReviewRequest;
import com.elibrary.backend.model.Book;
import com.elibrary.backend.model.Review;
import com.elibrary.backend.model.User;
import com.elibrary.backend.repository.BookRepository;
import com.elibrary.backend.repository.ReviewRepository;
import com.elibrary.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Review addReview(ReviewRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách!"));

        if (request.rating() < 1 || request.rating() > 5) {
            throw new RuntimeException("Điểm đánh giá phải nằm trong khoảng từ 1 đến 5 sao!");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.rating());
        review.setComment(request.comment());

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByBook(Integer bookId) {
        return reviewRepository.findByBookId(bookId);
    }
}