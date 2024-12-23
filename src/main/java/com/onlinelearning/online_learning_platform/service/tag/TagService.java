package com.onlinelearning.online_learning_platform.service.tag;

import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.mapper.TagMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Tag;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import com.onlinelearning.online_learning_platform.repository.TagRepository;
import com.onlinelearning.online_learning_platform.service.course.CourseHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class TagService {

    private TagRepository tagRepository;

    private CourseRepository courseRepository;

    private TagMapper tagMapper;

    private CourseHandlerService courseHandlerService;

    private TagHandlerService tagHandlerService;

    public TagService(TagRepository tagRepository, CourseRepository courseRepository
            , TagMapper tagMapper, CourseHandlerService courseHandlerService, TagHandlerService tagHandlerService) {
        this.tagRepository = tagRepository;
        this.courseRepository = courseRepository;
        this.tagMapper = tagMapper;
        this.courseHandlerService = courseHandlerService;
        this.tagHandlerService = tagHandlerService;
    }

    public List<TagDto> getAll() {
        List<Tag> tags = tagRepository.findAll();
        return tagHandlerService.toTagsListDto(tags);
    }

    public List<TagDto> searchTags(TagDto tagDto) {
        List<Tag> tags = tagRepository.searchByTagName(tagDto.getTagName());
        return tagHandlerService.toTagsListDto(tags);
    }

    @Transactional
    public TagDto addTagToCourse(Integer courseId, TagDto tagDto) {

        Course course = courseHandlerService.checkCourseExist(courseId);
        Tag tag = tagRepository
                .findByTagName(tagDto.getTagName())
                .orElseGet(() -> tagRepository.save(tagMapper.toEntity(tagDto)));

        if (tag.getCourses() == null) {
            tag.setCourses(new HashSet<>());
        }
        tagHandlerService.checkTagExistInCourse(courseId, tag.getId());

        course.getTags().add(tag);
        tag.getCourses().add(course);

        courseRepository.save(course);
        return tagMapper.toDto(tag);
    }

    @Transactional
    public String deleteTag(Integer tagId) {
        tagRepository.delete(tagHandlerService.checkTagExist(tagId));
        return "Tag deleted successfully with ID: "+ tagId;
    }
}
