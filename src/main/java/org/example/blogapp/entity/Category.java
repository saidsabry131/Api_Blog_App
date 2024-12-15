package org.example.blogapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.blogapp.dto.CategoryDto;

import java.util.List;

@Entity
@Table(name = "categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @Column(name = "name",nullable = false)
    private String categoryName;

    @Column(name = "description")
    private String  categoryDescription;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)

    private List<Post> postList;

    public Category(CategoryDto dto)
    {
        this.categoryId=dto.getCategoryId();
        this.categoryName=dto.getCategoryName();
        this.categoryDescription=dto.getCategoryDescription();
    }

}
