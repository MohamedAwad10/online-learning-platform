package com.onlinelearning.online_learning_platform.service.lesson;

import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.exception.LessonNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.LessonMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.lesson.Lesson;
import com.onlinelearning.online_learning_platform.model.lesson.LessonID;
import com.onlinelearning.online_learning_platform.repository.LessonRepository;
import com.onlinelearning.online_learning_platform.service.course.CourseHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private LessonRepository lessonRepository;

    private LessonMapper lessonMapper;

    private CourseHandlerService courseHandlerService;

    private LessonHandlerService lessonHandlerService;

    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper
            , CourseHandlerService courseHandlerService, LessonHandlerService lessonHandlerService){
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.courseHandlerService = courseHandlerService;
        this.lessonHandlerService = lessonHandlerService;
    }

    public List<LessonDto> getAll(Integer courseId) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        return lessonHandlerService.getLessonsDto(course.getLessons());
    }

    public LessonDto getById(Integer courseId, Integer lessonId) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        LessonID lessonKey = new LessonID(lessonId, course);
        Lesson lesson = lessonHandlerService.checkLessonExist(lessonKey);
        return lessonMapper.toDto(lesson);
    }

    @Transactional
    public LessonDto insert(Integer courseId, LessonDto lessonDto) {

        Course course = courseHandlerService.checkCourseExist(courseId);
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson.setCourse(course);
        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(savedLesson);
    }

    @Transactional
    public LessonDto update(Integer courseId, Integer lessonId, LessonDto lessonDto) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        LessonID lessonKey = new LessonID(lessonId, course);

        Lesson lesson = lessonHandlerService.checkLessonExist(lessonKey);
        lesson.setTitle(lessonDto.getTitle());
        lesson.setUrl(lessonDto.getUrl());
        lesson.setDuration(lessonDto.getDuration());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(updatedLesson);
    }

    @Transactional
    public String delete(Integer courseId, Integer lessonId) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        LessonID lessonKey = new LessonID(lessonId, course);
        Lesson lesson = lessonHandlerService.checkLessonExist(lessonKey);
        lessonRepository.delete(lesson);
        return "Lesson deleted successfully with ID: "+ lesson.getId();
    }

}
