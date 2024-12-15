package org.example.blogapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.blogapp.entity.Category;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private int categoryId;
    @NotBlank
    @NotNull
    @NotEmpty(message = "must not empty")

    private String categoryName;

    @NotBlank
    @NotNull
    @NotEmpty
    private String  categoryDescription;

    public CategoryDto(Category category)
    {
        this.categoryId=category.getCategoryId();
        this.categoryDescription=category.getCategoryDescription();
        this.categoryName=category.getCategoryName();
    }
}
