package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.model.lesson.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public LessonDto toDto(Lesson lesson){
        return LessonDto.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .url(lesson.getUrl())
                .duration(lesson.getDuration())
                .build();
    }

    public Lesson toEntity(LessonDto lessonDto){
        return Lesson.builder()
                .title(lessonDto.getTitle())
                .url(lessonDto.getUrl())
                .duration(lessonDto.getDuration())
                .build();
    }
}
