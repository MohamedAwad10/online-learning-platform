package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagDto toDto(Tag tag){
        return TagDto.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .build();
    }

    public Tag toEntity(TagDto tagDto){
        return Tag.builder()
                .tagName(tagDto.getTagName())
                .build();
    }
}
