package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.type.PlainTypeDto;
import com.StefanSergiu.Licenta.dto.type.TypeDto;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.AttributeRepository;
import com.StefanSergiu.Licenta.repository.CategoryRepository;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TypeService {

    @Autowired
    TypeRepository typeRepository;
    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    public Type addType(Type type){return typeRepository.save(type);}
    public List<Type> getTypes(){
        return StreamSupport
                .stream(typeRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public Type getType(Long id){
        return typeRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
    }


    @Transactional
    public Type deleteType(Long id) {
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Type not found with id " + id));

        typeRepository.delete(type);
        return type;
    }

    @Transactional
    public Type editType(Long id, PlainTypeDto plainTypeDto){
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Type not found with id " + id));
        type.setName(plainTypeDto.getName());
        return type;
    }
    public Type getTypeByCategory(String categoryName){
        Type type = typeRepository.findByCategories_Name(categoryName);
        return type;
    }
}
