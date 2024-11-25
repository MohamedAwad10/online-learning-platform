package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-learning/courses/tag")
public class TagController {

    private TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTags(){
        try {
            List<TagDto> tags = tagService.getAll();
            if(tags.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
            }

            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> addTagToCourse(@PathVariable Integer courseId,@Valid @RequestBody TagDto tagDto){
        try {
            TagDto createdTag = tagService.addTagToCourse(courseId, tagDto);
            return ResponseEntity.ok(createdTag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTagsByTagName(@Valid @RequestBody TagDto tagDto){
        try {
            List<String> tags = tagService.searchTags(tagDto);
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
