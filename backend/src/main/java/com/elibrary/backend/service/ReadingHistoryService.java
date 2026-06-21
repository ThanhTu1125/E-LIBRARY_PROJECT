package com.elibrary.backend.service;

import com.elibrary.backend.dto.ReadingHistoryRequest;
import com.elibrary.backend.model.Book;
import com.elibrary.backend.model.ReadingHistory;
import com.elibrary.backend.model.User;
import com.elibrary.backend.repository.BookRepository;
import com.elibrary.backend.repository.ReadingHistoryRepository;
import com.elibrary.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingHistoryService {

    private final ReadingHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ReadingHistory updateHistory(ReadingHistoryRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách!"));

        ReadingHistory history = historyRepository.findByUserIdAndBookId(user.getId(), book.getId())
                .orElse(new ReadingHistory());

        history.setUser(user);
        history.setBook(book);
        history.setLastReadPage(request.lastReadPage());

        return historyRepository.save(history);
    }

    public List<ReadingHistory> getMyHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
        return historyRepository.findByUserId(user.getId());
    }
}