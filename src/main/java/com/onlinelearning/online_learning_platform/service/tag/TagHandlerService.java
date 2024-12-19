package com.onlinelearning.online_learning_platform.service.tag;

import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.exception.TagException;
import com.onlinelearning.online_learning_platform.mapper.TagMapper;
import com.onlinelearning.online_learning_platform.model.Tag;
import com.onlinelearning.online_learning_platform.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagHandlerService {

    private TagRepository tagRepository;

    private TagMapper tagMapper;

    public TagHandlerService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    public Set<Tag> getTags(Set<TagDto> tagsDto){
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
