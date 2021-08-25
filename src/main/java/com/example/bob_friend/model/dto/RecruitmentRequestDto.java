package com.example.bob_friend.model.dto;

import com.example.bob_friend.model.entity.Member;
import com.example.bob_friend.model.entity.Recruitment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RecruitmentRequestDto {
    private String title;
    private String content;
    private Member author;
    private Integer totalNumberOfPeople;
    private String restaurantName;
    private Double latitude;
    private Double longitude;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public RecruitmentRequestDto(Recruitment recruitment) {
        this.title = recruitment.getTitle();
        this.content = recruitment.getContent();
        this.author = recruitment.getAuthor();
        this.totalNumberOfPeople = recruitment.getTotalNumberOfPeople();
        this.restaurantName = recruitment.getRestaurantName();
        this.latitude = recruitment.getLatitude();
        this.longitude = recruitment.getLongitude();
        this.startAt = recruitment.getStartAt();
        this.endAt = recruitment.getEndAt();
    }

    public Recruitment convertToDomain() {
        return Recruitment.builder()
                .title(this.title)
                .content(this.content)
                .author(this.author)
                .restaurantName(this.restaurantName)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .startAt(this.startAt)
                .endAt(this.endAt)
                .build();
    }
}
