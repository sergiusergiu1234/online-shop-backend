package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.attribute.CreateNewAttributeDto;
import com.StefanSergiu.Licenta.dto.attribute.EditAttributeDto;
import com.StefanSergiu.Licenta.entity.Attribute;
import com.StefanSergiu.Licenta.entity.Type;
import com.StefanSergiu.Licenta.repository.AttributeRepository;
import com.StefanSergiu.Licenta.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeService {
    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    TypeRepository typeRepository;

    public Attribute getAttribute(Long attributeId){
        return attributeRepository.findById(attributeId)
                .orElseThrow(()->new EntityNotFoundException("Attribute with id "+ attributeId + " not found!"));
    }

    @Transactional
    public Attribute addAttribute(CreateNewAttributeDto createNewAttributeDto){
        Attribute attribute = new Attribute();

        Type type = typeRepository.findById(createNewAttributeDto.getTypeId())
                .orElseThrow(()->new EntityNotFoundException("Type with id "+ " not found"));
        attribute.setType(type);
        attribute.setName(createNewAttributeDto.getName());

        //add attribute to the other side of the relationship
        type.addAttribute(attribute);
        return attributeRepository.save(attribute);
    }

    public List<Attribute> getAttributes(){
        return attributeRepository.findAll();       //posibil sa fie nevoie de stream
    }

    @Transactional
    public Attribute deleteAttribute(Long id){
        Attribute attribute = getAttribute(id);
        attribute.getType().getCategories().remove(attribute); //detach from type
        attributeRepository.delete(attribute);
        return attribute;
    }

    @Transactional
    public Attribute editAttribute(Long id, EditAttributeDto editAttributeDto){
        Attribute attribute = getAttribute(id);
        attribute.setName(editAttributeDto.getName());
        return attribute;
    }
}
