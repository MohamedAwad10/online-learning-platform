package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.exception.TagException;
import com.onlinelearning.online_learning_platform.mapper.TagMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Tag;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import com.onlinelearning.online_learning_platform.repository.TagRepository;
import com.onlinelearning.online_learning_platform.service.course.CourseValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    private TagRepository tagRepository;

    private CourseRepository courseRepository;

    private TagMapper tagMapper;

    private CourseValidator courseValidator;

    public TagService(TagRepository tagRepository, CourseRepository courseRepository
            , TagMapper tagMapper, CourseValidator courseValidator) {
        this.tagRepository = tagRepository;
        this.courseRepository = courseRepository;
        this.tagMapper = tagMapper;
        this.courseValidator = courseValidator;
    }

    public List<TagDto> getAll() {

        List<Tag> tags = tagRepository.findAll();
        return toTagsListDto(tags);
    }

    public List<TagDto> searchTags(TagDto tagDto) {

        List<Tag> tags = tagRepository.searchByTagName(tagDto.getTagName());
        return toTagsListDto(tags);
    }

    @Transactional
    public TagDto addTagToCourse(Integer courseId, TagDto tagDto) {

        Course course = courseValidator.checkCourseExist(courseId);
        Tag tag = tagRepository
                .findByTagName(tagDto.getTagName())
                .orElseGet(() -> tagRepository.save(tagMapper.toEntity(tagDto)));

        if (tag.getCourses() == null) {
            tag.setCourses(new HashSet<>());
        }

        checkTagExistInCourse(courseId, tag.getId());

        course.getTags().add(tag);
        tag.getCourses().add(course);

        courseRepository.save(course);
        return tagMapper.toDto(tag);
    }

    @Transactional
    public String deleteTag(Integer tagId) {
        tagRepository.delete(checkTagExist(tagId));
        return "Tag deleted successfully with ID: "+ tagId;
    }

    @Transactional
    public Set<Tag> getOrCreateTagsDto(Set<TagDto> tagsDto){
        return tagsDto.stream()
                .map(tagDto -> tagRepository
                        .findByTagName(tagDto.getTagName())
                        .orElseGet(() -> tagMapper.toEntity(tagDto))).collect(Collectors.toSet());
    }

    public Set<TagDto> toTagsDto(Set<Tag> tags){
        return tags.stream().map(tag -> tagMapper.toDto(tag)).collect(Collectors.toSet());
    }

    public List<TagDto> toTagsListDto(List<Tag> tags){
        return tags.stream().map(tag -> tagMapper.toDto(tag)).toList();
    }

    public void checkTagExistInCourse(Integer courseId, Integer tagId){
        Optional<Tag> optionalTag = tagRepository.findTagInCourse(courseId, tagId);
        if(optionalTag.isPresent()){
            throw new TagException("Tag already exist");
        }
    }

    public Tag checkTagExist(Integer tagId){
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new TagException("Tag not found"));
    }
}
