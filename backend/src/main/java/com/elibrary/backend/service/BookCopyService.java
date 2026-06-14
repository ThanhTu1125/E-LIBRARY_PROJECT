package com.elibrary.backend.service;

import com.elibrary.backend.model.Book;
import com.elibrary.backend.model.BookCopy;
import com.elibrary.backend.model.Branch;
import com.elibrary.backend.repository.BookCopyRepository;
import com.elibrary.backend.repository.BookRepository;
import com.elibrary.backend.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;
    private final BranchRepository branchRepository;

    // Hàm nhập sách hàng loạt vào kho
    @Transactional
    public List<BookCopy> importBookCopies(@NonNull Integer bookId, @NonNull Integer branchId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy đầu sách!"));

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy chi nhánh thư viện!"));

        List<BookCopy> newCopies = new ArrayList<>();

        // Vòng lặp sinh ra n cuốn sách với mã Barcode riêng biệt
        for (int i = 0; i < quantity; i++) {
            BookCopy copy = new BookCopy();
            copy.setBook(book);
            copy.setBranch(branch);

            // Công thức sinh mã vạch: LIB-MãSách-ChuỗiNgẫuNhiên
            String uniqueBarcode = "LIB-B" + bookId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            copy.setBarcode(uniqueBarcode);
            copy.setStatus("AVAILABLE");
            newCopies.add(copy);
        }

        return bookCopyRepository.saveAll(newCopies);
    }
}