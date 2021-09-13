package com.example.bob_friend.repository;

import com.example.bob_friend.model.entity.Member;
import com.example.bob_friend.model.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    List<Recruitment> findAllByRestaurantNameOrRestaurantAddress(String restaurantName, String restaurantAddress);

    List<Recruitment> findAllByAuthor(Member member);
}