package com.elibrary.backend.service;

import com.elibrary.backend.dto.BorrowingRequest;
import com.elibrary.backend.model.BookCopy;
import com.elibrary.backend.model.Borrowing;
import com.elibrary.backend.model.Fine;
import com.elibrary.backend.model.User;
import com.elibrary.backend.repository.BookCopyRepository;
import com.elibrary.backend.repository.BorrowingRepository;
import com.elibrary.backend.repository.UserRepository;
import com.elibrary.backend.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;

    @Transactional
    public Borrowing borrowBook(BorrowingRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Thẻ thư viện không tồn tại!"));

        if (!user.isStatus()) {
            throw new RuntimeException("Tài khoản của bro đã bị khóa, không thể mượn sách!");
        }

        BookCopy bookCopy = bookCopyRepository.findById(request.bookCopyId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyển sách vật lý này trong kho!"));

        if (!"AVAILABLE".equals(bookCopy.getStatus())) {
            throw new RuntimeException("Rất tiếc! Cuốn sách này đã có người mượn hoặc đang thất lạc.");
        }

        bookCopy.setStatus("BORROWED");
        bookCopyRepository.save(bookCopy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBookCopy(bookCopy);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(request.durationDays()));
        borrowing.setStatus("BORROWING");

        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public Borrowing returnBook(@NonNull Integer borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn sách này!"));

        if ("RETURNED".equals(borrowing.getStatus())) {
            throw new RuntimeException("Phiếu này đã được hoàn tất trả sách từ trước rồi!");
        }

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus("RETURNED");

        BookCopy bookCopy = borrowing.getBookCopy();
        bookCopy.setStatus("AVAILABLE");
        bookCopyRepository.save(bookCopy);

        long daysLate = ChronoUnit.DAYS.between(borrowing.getDueDate(), borrowing.getReturnDate());

        if (daysLate > 0) {
            Fine fine = new Fine();
            fine.setBorrowing(borrowing);
            fine.setUser(borrowing.getUser());
            fine.setFineAmount(java.math.BigDecimal.valueOf(daysLate * 5000));
            fine.setReason("Trả trễ " + daysLate + " ngày");
            fine.setStatus("UNPAID");
            fineRepository.save(fine);
        }

        return borrowingRepository.save(borrowing);
    }
}