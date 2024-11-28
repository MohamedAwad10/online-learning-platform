package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.model.review.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review review){

        return ReviewDto.builder()
                .id(review.getId())
                .rate(review.getRate())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt().toString())
                .student(review.getStudent().getFirstName()+" "+review.getStudent().getLastName())
                .build();
    }

    public Review toEntity(ReviewDto reviewDto){

        return Review.builder()
                .rate(reviewDto.getRate())
                .comment(reviewDto.getComment())
                .build();
    }
}
