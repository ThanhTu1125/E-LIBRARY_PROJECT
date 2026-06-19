package com.elibrary.backend.service;

import com.elibrary.backend.model.Fine;
import com.elibrary.backend.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;

    // Xem danh sách biên lai phạt của 1 sinh viên
    public List<Fine> getFinesByUser(@NonNull Integer userId) {
        return fineRepository.findByUserId(userId);
    }

    // Xử lý nộp tiền phạt
    @Transactional
    public Fine payFine(@NonNull Integer fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biên lai phạt này!"));

        if ("PAID".equals(fine.getStatus())) {
            throw new RuntimeException("Biên lai này đã được thanh toán rồi!");
        }

        fine.setStatus("PAID");
        return fineRepository.save(fine);
    }
}