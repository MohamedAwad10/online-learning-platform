package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.commons.Commons;
import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.exception.TagException;
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

    private Commons commons;

    public TagService(TagRepository tagRepository, CourseRepository courseRepository
            , TagMapper tagMapper, Commons commons) {
        this.tagRepository = tagRepository;
        this.courseRepository = courseRepository;
        this.tagMapper = tagMapper;
        this.commons = commons;
    }

    public List<TagDto> getAll() {

        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tag -> tagMapper.toDto(tag)).toList();
    }

    public List<TagDto> searchTags(TagDto tagDto) {

        List<Tag> tags = tagRepository.searchByTagName(tagDto.getTagName());
        return tags.stream().map(tag -> tagMapper.toDto(tag)).toList();
    }

    @Transactional
    public TagDto addTagToCourse(Integer courseId, TagDto tagDto) {

        Course course = commons.checkCourseExist(courseId);

        Tag tag = tagRepository
                .findByTagName(tagDto.getTagName())
                .orElseGet(() -> tagRepository.save(tagMapper.toEntity(tagDto)));

        if (tag.getCourses() == null) {
            tag.setCourses(new HashSet<>());
        }

        Optional<Tag> optionalTag = tagRepository.findTagInCourse(courseId, tag.getId());
        if(optionalTag.isPresent()){
            throw new TagException("Tag already exist");
        }

        course.getTags().add(tag);
        tag.getCourses().add(course);

        courseRepository.save(course);

        return tagMapper.toDto(tag);
    }

    @Transactional
    public String deleteTag(Integer tagId) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagException("Tag not found"));
        tagRepository.delete(tag);
        return "Tag deleted successfully with ID: "+ tagId;
    }
}
