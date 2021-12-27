package com.example.bobfriend.controller;

import com.example.bobfriend.model.dto.CommentDto;
import com.example.bobfriend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recruitments/{recruitmentId}/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping()
    public ResponseEntity getAll(@PathVariable Long recruitmentId) {
        List<CommentDto.Response> allCommentByRecruitmentId = commentService
                .getAllCommentByRecruitmentId(recruitmentId);
        return ResponseEntity.ok(allCommentByRecruitmentId);
    }

    @PostMapping()
    public ResponseEntity create(
            @PathVariable Long recruitmentId,
            @Valid @RequestBody CommentDto.Request commentRequestDto) {
        CommentDto.Response comment =
                commentService.create(
                        commentRequestDto, recruitmentId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity delete(
            @PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}/report")
    public ResponseEntity report(@PathVariable Long commentId) {
        commentService.report(commentId);
        return ResponseEntity.ok().build();
    }

}
