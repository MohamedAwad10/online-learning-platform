package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(
            name = "category_name",
            nullable = false,
            unique = true
    )
    private String categoryName;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY
    )
    private Set<Course> courses;
}
