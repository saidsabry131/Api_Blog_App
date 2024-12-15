package org.example.blogapp.service;

import org.example.blogapp.dto.CategoryDto;
import org.example.blogapp.entity.Category;
import org.example.blogapp.exception.ResourceNotFound;
import org.example.blogapp.repository.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public CategoryDto createCategory(CategoryDto dto)
    {
        Category category=new Category(dto);
        return new CategoryDto(categoryRepo.save(category)) ;

    }

    public List<CategoryDto> getAllCategories()
    {
        List<Category> categories=categoryRepo.findAll();

        List<CategoryDto> categoryDtos=new ArrayList<>();

        for (Category  category: categories)
        {
            CategoryDto dto=new CategoryDto(category);
            categoryDtos.add(dto);
        }

        return categoryDtos;
    }

    public CategoryDto updateCategory(int id, CategoryDto dto) {
       Category category=categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFound("Category", "id", id));

       category.setCategoryName(dto.getCategoryName());
       category.setCategoryDescription(dto.getCategoryDescription());

        return new CategoryDto(categoryRepo.save(category));
    }

    public void deleteCategory(int id) {

        Category category =categoryRepo.findById(id)
                .orElseThrow(()->new ResourceNotFound("Category", "id", id));

        categoryRepo.delete(category);
    }

    public CategoryDto getCategoryById(int id)
    {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category", "id", id));
        return new CategoryDto(category);
       // return new CategoryDto(categoryRepo.findById(id).orElseThrow(()->new ResourceNotFound("Category","id",id)));
    }
}
