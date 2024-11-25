package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.mapper.TagMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Tag;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import com.onlinelearning.online_learning_platform.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private TagRepository tagRepository;

    private CourseRepository courseRepository;

    private TagMapper tagMapper;

    public TagService(TagRepository tagRepository, CourseRepository courseRepository, TagMapper tagMapper){
        this.tagRepository = tagRepository;
        this.courseRepository = courseRepository;
        this.tagMapper = tagMapper;
    }

    public List<TagDto> getAll() throws Exception{

        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tag -> tagMapper.toDto(tag)).toList();
    }

    public List<String> searchTags(TagDto tagDto){

        return tagRepository.searchByTagName(tagDto.getTagName());
    }

    @Transactional
    public TagDto addTagToCourse(Integer courseId, TagDto tagDto) throws Exception{

        Course course = checkCourseExist(courseId);

        Tag tag = tagRepository
                .findByTagName(tagDto.getTagName())
                .orElseGet(() -> tagRepository.save(tagMapper.toEntity(tagDto)));

        if (tag.getCourses() == null) {
            tag.setCourses(new HashSet<>());
        }

        if(course.getTags().contains(tag)){
            throw new RuntimeException("Tag already exist");
        }

        course.getTags().add(tag);
        tag.getCourses().add(course);

        courseRepository.save(course);

        return tagDto;
    }

    public Course checkCourseExist(Integer courseId) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found");
        }
        return optionalCourse.get();
    }
}
