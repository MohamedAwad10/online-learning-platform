package com.onlinelearning.online_learning_platform.service.lesson;

import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.exception.LessonNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.LessonMapper;
import com.onlinelearning.online_learning_platform.model.lesson.Lesson;
import com.onlinelearning.online_learning_platform.model.lesson.LessonID;
import com.onlinelearning.online_learning_platform.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonHandlerService {

    private LessonRepository lessonRepository;

    private LessonMapper lessonMapper;

    public LessonHandlerService(LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    public Lesson checkLessonExist(LessonID lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if(optionalLesson.isEmpty()){
            throw new LessonNotFoundException("Lesson not found");
        }
        return optionalLesson.get();
    }

    public List<LessonDto> getLessonsDto(List<Lesson> lessons){
        return lessons.stream()
                .map(lesson -> lessonMapper.toDto(lesson)).toList();
    }
}
