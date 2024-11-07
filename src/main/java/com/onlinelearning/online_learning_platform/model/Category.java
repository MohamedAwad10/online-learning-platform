package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @Column(
            name = "category_name",
            nullable = false,
            unique = true
    )
    private String categoryName;
}
