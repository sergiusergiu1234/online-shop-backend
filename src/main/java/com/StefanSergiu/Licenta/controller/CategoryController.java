package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.category.CategoryDto;
import com.StefanSergiu.Licenta.dto.category.CreateNewCategoryModel;
import com.StefanSergiu.Licenta.entity.Category;
import com.StefanSergiu.Licenta.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "https://online-shop-frontend-nine.vercel.app")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //add Category
    @PostMapping("/admin/add")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CreateNewCategoryModel createNewCategoryModel){
        Category category = categoryService.addCategory(createNewCategoryModel);
        return new ResponseEntity<>(CategoryDto.from(category), HttpStatus.OK);
    }

    //get all Categories
    @GetMapping("/all")
    ResponseEntity<List<CategoryDto>> getCategories(){
        List<Category> categories = categoryService.getCategories();
        List<CategoryDto> categoryDtos = categories.stream().map(CategoryDto::from).collect(Collectors.toList());

        return new ResponseEntity<>(categoryDtos,HttpStatus.OK);
    }

    //get Category by id
    @GetMapping(value = "{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable final Long id){
        Category Category = categoryService.getCategory(id);
        return new ResponseEntity<>(CategoryDto.from(Category),HttpStatus.OK);
    }


    //delete Category by id
    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable final Long id){
        Category category = categoryService.deleteCategory(id);
        return new ResponseEntity<>(CategoryDto.from(category),HttpStatus.OK);
    }

    @PutMapping(value = "/admin/edit/{id}")
    public ResponseEntity<CategoryDto> editCategory(@PathVariable final Long id,@RequestBody CategoryDto categoryDto){
        Category category = categoryService.editCategory(id, categoryDto);
        return new ResponseEntity<>(CategoryDto.from(category),HttpStatus.OK);
    }
}
