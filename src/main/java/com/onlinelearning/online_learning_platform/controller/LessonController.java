package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-learning/course/{courseId}/lessons")
public class LessonController {

    private LessonService lessonService;

    public LessonController(LessonService lessonService){
        this.lessonService = lessonService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllLessons(@PathVariable Integer courseId){

        List<LessonDto> allLessons = lessonService.getAll(courseId);
        if(allLessons.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
        }
        return ResponseEntity.ok(allLessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<?> getLessonById(@PathVariable Integer courseId, @PathVariable Integer lessonId){

        LessonDto lesson = lessonService.getById(courseId, lessonId);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLesson(@PathVariable Integer courseId
            , @Valid @RequestBody LessonDto lessonDto){

        LessonDto lesson = lessonService.insert(courseId, lessonDto);
        return ResponseEntity.ok(lesson);
    }

    @PutMapping("/update/{lessonId}")
    public ResponseEntity<?> updateLesson(@PathVariable Integer courseId, @PathVariable Integer lessonId
            , @Valid @RequestBody LessonDto lessonDto){

        LessonDto lesson = lessonService.update(courseId, lessonId, lessonDto);
        return ResponseEntity.ok(lesson);
    }

    @DeleteMapping("/delete/{lessonId}")
    public ResponseEntity<String> deleteLesson(@PathVariable Integer courseId, @PathVariable Integer lessonId){

        String message = lessonService.delete(courseId, lessonId);
        return ResponseEntity.ok(message);
    }
}
