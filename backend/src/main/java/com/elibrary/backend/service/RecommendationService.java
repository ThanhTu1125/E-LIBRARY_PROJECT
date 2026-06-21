package com.elibrary.backend.service;

import com.elibrary.backend.model.*;
import com.elibrary.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final ReadingHistoryRepository historyRepository;

    @Transactional
    public void generateRecommendationsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));

        // 1. Xóa danh sách gợi ý cũ của user này
        recommendationRepository.deleteByUserId(user.getId());

        // 2. Thu thập hồ sơ sở thích thể loại (Category Profiling)
        Map<Integer, Double> categoryScores = new HashMap<>();

        // 2.1 Phân tích từ Review
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        for (Review r : reviews) {
            Integer catId = r.getBook().getCategory().getId();
            // Thuật toán: 5 sao = +4đ, 4 sao = +2đ, 3 sao = 0đ, 2 sao = -2đ, 1 sao = -4đ
            double points = (r.getRating() - 3) * 2.0;
            categoryScores.put(catId, categoryScores.getOrDefault(catId, 0.0) + points);
        }

        // 2.2 Phân tích từ Lịch sử đọc (Cứ đọc là cộng 1 điểm cho thể loại đó)
        List<ReadingHistory> history = historyRepository.findByUserId(user.getId());
        for (ReadingHistory h : history) {
            Integer catId = h.getBook().getCategory().getId();
            categoryScores.put(catId, categoryScores.getOrDefault(catId, 0.0) + 1.0);
        }

        // 3. Chấm điểm toàn bộ sách trong thư viện
        List<Book> allBooks = bookRepository.findAll();
        List<Recommendation> newRecommendations = new ArrayList<>();

        for (Book book : allBooks) {
            // Bỏ qua những cuốn đã đọc (Chỉ gợi ý sách mới)
            boolean alreadyRead = history.stream().anyMatch(h -> h.getBook().getId().equals(book.getId()));
            if (alreadyRead)
                continue;

            // Lấy điểm thể loại của cuốn sách này
            double score = categoryScores.getOrDefault(book.getCategory().getId(), 0.0);

            // Yếu tố "Trending" (Thêm 0.1 điểm cho mỗi lượt view để ưu tiên sách hot)
            score += (book.getViewCount() != null ? book.getViewCount() : 0) * 0.1;

            // Nếu điểm > 0, đưa vào danh sách đề xuất
            if (score > 0) {
                Recommendation rec = new Recommendation();
                rec.setUser(user);
                rec.setBook(book);
                rec.setScore(score);
                newRecommendations.add(rec);
            }
        }

        // 4. Lưu xuống Database
        recommendationRepository.saveAll(newRecommendations);
    }

    // Hàm lấy danh sách gợi ý đã sắp xếp điểm từ cao xuống thấp
    public List<Recommendation> getMyRecommendations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));
        return recommendationRepository.findByUserIdOrderByScoreDesc(user.getId());
    }
}