package com.LHQ_Backend.LHQ_Backend.review.mappers;

import com.LHQ_Backend.LHQ_Backend.review.DTOs.Response.ReviewResponse;
import com.LHQ_Backend.LHQ_Backend.review.entity.Review;

public final class ReviewMapper {

    private ReviewMapper() {}

    public static ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder().id(review.getId()).userId(review.getUser().getId())
                .userFullName(
                        review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .lawyerId(review.getLawyer().getId())
                .lawyerFullName(review.getLawyer().getUser().getFirstName() + " "
                        + review.getLawyer().getUser().getLastName())
                .bookingId(review.getBooking().getId()).rating(review.getRating())
                .comment(review.getComment()).sentiment(review.getSentiment())
                .createdAt(review.getCreatedAt()).build();
    }
}
