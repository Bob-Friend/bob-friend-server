package com.example.bobfriend.model.dto.Recruitment;

import com.example.bobfriend.model.entity.Recruitment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Response extends BaseResponse {
    private List<com.example.bobfriend.model.dto.comment.Response> comments;

    public Response(Recruitment recruitment) {
        super(recruitment);
        this.comments = recruitment.getComments().stream()
                .map(com.example.bobfriend.model.dto.comment.Response::new)
                .sorted(Comparator.comparing(com.example.bobfriend.model.dto.comment.Response::getCreatedAt))
                .collect(Collectors.toList());
    }
}
