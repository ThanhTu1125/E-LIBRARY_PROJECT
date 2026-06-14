package com.elibrary.backend.service;

import com.elibrary.backend.dto.BorrowingRequest;
import com.elibrary.backend.model.BookCopy;
import com.elibrary.backend.model.Borrowing;
import com.elibrary.backend.model.User;
import com.elibrary.backend.repository.BookCopyRepository;
import com.elibrary.backend.repository.BorrowingRepository;
import com.elibrary.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;

    @Transactional
    public Borrowing borrowBook(BorrowingRequest request) {

        // 1. Kiểm tra thẻ sinh viên (User)
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Thẻ thư viện không tồn tại!"));

        if (!user.isStatus()) {
            throw new RuntimeException("Tài khoản của bro đã bị khóa, không thể mượn sách!");
        }

        // 2. Tìm cuốn sách trên kệ dựa vào mã Barcode (BookCopy ID)
        BookCopy bookCopy = bookCopyRepository.findById(request.bookCopyId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyển sách vật lý này trong kho!"));

        if (!"AVAILABLE".equals(bookCopy.getStatus())) {
            throw new RuntimeException("Rất tiếc! Cuốn sách này đã có người mượn hoặc đang thất lạc.");
        }

        // 3. Đánh dấu sách đã bị lấy đi khỏi kệ
        bookCopy.setStatus("BORROWED");
        bookCopyRepository.save(bookCopy);

        // 4. In phiếu mượn sách (Tạo Record Borrowing)
        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBookCopy(bookCopy);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(request.durationDays()));
        borrowing.setStatus("BORROWING");

        return borrowingRepository.save(borrowing);
    }
}