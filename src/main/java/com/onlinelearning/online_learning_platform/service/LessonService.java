package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.commons.Commons;
import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.exception.LessonNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.LessonMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.lesson.Lesson;
import com.onlinelearning.online_learning_platform.model.lesson.LessonID;
import com.onlinelearning.online_learning_platform.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private LessonRepository lessonRepository;

    private LessonMapper lessonMapper;

    private Commons commons;

    @Autowired
    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper, Commons commons){
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.commons = commons;
    }

    public List<LessonDto> getAll(Integer courseId) {

        Course course = commons.checkCourseExist(courseId);
        List<LessonDto> lessons = course.getLessons().stream()
                .map(lesson -> lessonMapper.toDto(lesson)).toList();

        return lessons;
    }

    public LessonDto getById(Integer courseId, Integer lessonId) {

        Course course = commons.checkCourseExist(courseId);
        LessonID lessonKey = new LessonID(lessonId, course);
        Lesson lesson = checkLessonExist(lessonKey);

        return lessonMapper.toDto(lesson);
    }

    @Transactional
    public LessonDto insert(Integer courseId, LessonDto lessonDto) {

        Course course = commons.checkCourseExist(courseId);

        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson.setCourse(course);
        Lesson savedLesson = lessonRepository.save(lesson);

        return lessonMapper.toDto(savedLesson);
    }

    public LessonDto update(Integer courseId, Integer lessonId, LessonDto lessonDto) {

        Course course = commons.checkCourseExist(courseId);
        LessonID lessonKey = new LessonID(lessonId, course);

        Lesson lesson = checkLessonExist(lessonKey);

        lesson.setTitle(lessonDto.getTitle());
        lesson.setUrl(lessonDto.getUrl());
        lesson.setDuration(lessonDto.getDuration());

        Lesson updatedLesson = lessonRepository.save(lesson);

        return lessonMapper.toDto(updatedLesson);
    }

    public String delete(Integer courseId, Integer lessonId) {

        Course course = commons.checkCourseExist(courseId);

        LessonID lessonKey = new LessonID(lessonId, course);
        Lesson lesson = checkLessonExist(lessonKey);

        lessonRepository.delete(lesson);

        return "Lesson deleted successfully with ID: "+ lesson.getId();
    }

    public Lesson checkLessonExist(LessonID lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if(optionalLesson.isEmpty()){
            throw new LessonNotFoundException("Lesson not found");
        }

        return optionalLesson.get();
    }

}
