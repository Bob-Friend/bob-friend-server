package com.example.bob_friend.service;

import com.example.bob_friend.model.entity.Member;
import com.example.bob_friend.model.entity.Report;
import com.example.bob_friend.model.entity.Writing;
import com.example.bob_friend.model.exception.AlreadyReportedExeption;
import com.example.bob_friend.repository.WritingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final WritingReportRepository reportRepository;

    @Transactional
    public void reportWriting(Member member, Writing writing) {
        if (reportRepository.existsByMemberAndWriting(member , writing))
            throw new AlreadyReportedExeption(writing);
        else {
            writing.report();
            Report report = Report.builder()
                    .member(member)
                    .writing(writing)
                    .build();
            reportRepository.save(report);
        }
    }
}
