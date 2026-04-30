package com.LHQ_Backend.LHQ_Backend.review.DTOs.Response;

import com.LHQ_Backend.LHQ_Backend.review.enums.Sentiment;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReviewResponse(String id, String userId, String userFullName, // denormalized
        String lawyerId, String lawyerFullName, // denormalized
        String bookingId, Integer rating, String comment, Sentiment sentiment, Instant createdAt) {
}
