package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.exception.LessonNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.LessonMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.lesson.Lesson;
import com.onlinelearning.online_learning_platform.model.lesson.LessonID;
import com.onlinelearning.online_learning_platform.repository.LessonRepository;
import com.onlinelearning.online_learning_platform.service.course.CourseValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private LessonRepository lessonRepository;

    private LessonMapper lessonMapper;

    private CourseValidator courseValidator;

    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper, CourseValidator courseValidator){
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.courseValidator = courseValidator;
    }

    public List<LessonDto> getAll(Integer courseId) {
        Course course = courseValidator.checkCourseExist(courseId);
        return getLessonsDto(course.getLessons());
    }

    public LessonDto getById(Integer courseId, Integer lessonId) {

        Course course = courseValidator.checkCourseExist(courseId);
        LessonID lessonKey = new LessonID(lessonId, course);
        Lesson lesson = checkLessonExist(lessonKey);
        return lessonMapper.toDto(lesson);
    }

    @Transactional
    public LessonDto insert(Integer courseId, LessonDto lessonDto) {

        Course course = courseValidator.checkCourseExist(courseId);
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson.setCourse(course);
        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(savedLesson);
    }

    @Transactional
    public LessonDto update(Integer courseId, Integer lessonId, LessonDto lessonDto) {

        Course course = courseValidator.checkCourseExist(courseId);
        LessonID lessonKey = new LessonID(lessonId, course);

        Lesson lesson = checkLessonExist(lessonKey);
        lesson.setTitle(lessonDto.getTitle());
        lesson.setUrl(lessonDto.getUrl());
        lesson.setDuration(lessonDto.getDuration());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(updatedLesson);
    }

    @Transactional
    public String delete(Integer courseId, Integer lessonId) {

        Course course = courseValidator.checkCourseExist(courseId);
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

    public List<LessonDto> getLessonsDto(List<Lesson> lessons){
        return lessons.stream()
                .map(lesson -> lessonMapper.toDto(lesson)).toList();
    }

}
