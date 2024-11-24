package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Member;
import java.util.Set;

@Setter
@Getter
@ToString
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Tag is required")
    @NotBlank(message = "Tag cannot be blank")
    @Column(
            name = "tag_name",
            nullable = false
    )
    private String tagName;

    @ManyToMany(
            mappedBy = "tags",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY // join fetch
    )
    private Set<Course> courses;
}
