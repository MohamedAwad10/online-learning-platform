package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@ToString
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Category name must not be null")
    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 5, message = "Category name must be more than 4 character")
    @Column(
            name = "category_name",
            nullable = false,
            unique = true
    )
    private String categoryName;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE // test
    )
    private Set<Course> courses;
}
