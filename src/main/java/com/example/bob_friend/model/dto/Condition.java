package com.example.bob_friend.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Condition {
    public enum SearchCategory {
        title, content, place, time, all
    }

    public enum SearchType {
        available, all, joined, owned
    }

    @Setter
    @Getter
    public static class Search {
        private String restaurantName;
        private String restaurantAddress;
        private String keyword;
        private LocalDateTime start, end;
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        public void setStart(String start) {
            this.start = LocalDateTime.parse(start, formatter);
        }

        public void setEnd(String end) {
            this.end = LocalDateTime.parse(end, formatter);
        }
    }
}
