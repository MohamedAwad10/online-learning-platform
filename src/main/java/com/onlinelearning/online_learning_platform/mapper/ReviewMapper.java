package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.model.review.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toReviewDto(Review review){

        return ReviewDto.builder()
                .rate(review.getRate())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt().toString())
                .student(review.getStudent().getFirstName()+" "+review.getStudent().getLastName())
                .build();
    }
}
