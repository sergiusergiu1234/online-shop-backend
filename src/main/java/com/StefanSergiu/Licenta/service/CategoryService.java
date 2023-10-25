package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.category.CategoryDto;
import com.StefanSergiu.Licenta.dto.category.CreateNewCategoryModel;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.CategoryRepository;
import com.StefanSergiu.Licenta.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TypeRepository typeRepository;
    public Category addCategory(CreateNewCategoryModel createNewCategoryModel){
        Category category = new Category();

        Type type = typeRepository.findById(createNewCategoryModel.getTypeId()).orElseThrow(
                ()->new EntityNotFoundException("Type with id "+ createNewCategoryModel.getTypeId()+ " not found.")
        );
        category.setType(type);
        category.setName(createNewCategoryModel.getName());
        type.addCategory(category);
        return categoryRepository.save(category);}



    public List<Category> getCategories(){
        return StreamSupport
                .stream(categoryRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public Category getCategory(Long id){
        return categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
    }

    @Transactional
    public Category deleteCategory(Long id){
        Category category = getCategory(id);
        category.getType().getCategories().remove(category);    //detach category from type
        categoryRepository.delete(category);
        return category;
    }

    @Transactional
    public Category editCategory(Long id, CategoryDto categoryDto){
        Category categoryToEdit = categoryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category with id "+ id +" not found"));
        categoryToEdit.setName(categoryDto.getName());
        return categoryToEdit;
    }
}
