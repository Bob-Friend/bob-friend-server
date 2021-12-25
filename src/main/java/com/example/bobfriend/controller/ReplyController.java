package com.example.bobfriend.controller;

import com.example.bobfriend.model.dto.ReplyDto;
import com.example.bobfriend.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recruitments/{recruitmentId}/comments/{commentId}/replies")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping()
    public ResponseEntity create(
            @PathVariable Long commentId,
            @Valid @RequestBody ReplyDto.Request replyDto) {
        ReplyDto.Response reply =
                replyService.create(commentId, replyDto);

        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity delete(
            @PathVariable Long replyId) {
        replyService.delete(replyId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{replyId}/report")
    public ResponseEntity report(@PathVariable Long replyId) {
        replyService.report(replyId);
        return ResponseEntity.ok().build();
    }
}
