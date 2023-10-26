package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.attribute.AttributeDto;
import com.StefanSergiu.Licenta.dto.attribute.CreateNewAttributeDto;
import com.StefanSergiu.Licenta.dto.attribute.EditAttributeDto;
import com.StefanSergiu.Licenta.dto.category.CategoryDto;
import com.StefanSergiu.Licenta.dto.product.CreateNewProductModel;
import com.StefanSergiu.Licenta.entity.Attribute;
import com.StefanSergiu.Licenta.entity.Brand;
import com.StefanSergiu.Licenta.service.AttributeService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/attributes")
@CrossOrigin(origins = "https://slope-emporium.vercel.app/")
public class AttributeController {
    @Autowired
    AttributeService attributeService;

    //get all attributes
    @GetMapping("/all")
    public ResponseEntity<List<AttributeDto>> getAttributes(){
        List<Attribute> attributes = attributeService.getAttributes();
        List<AttributeDto> attributeDtos = attributes.stream().map(AttributeDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(attributeDtos, HttpStatus.OK);
    }
    //get attribute by id
    @GetMapping(value = "{id}")
    public ResponseEntity<AttributeDto> getAttribute(@PathVariable final long id){
        Attribute attribute = attributeService.getAttribute(id);
        return new ResponseEntity<>(AttributeDto.from(attribute), HttpStatus.OK);
    }

    //create new attribute
    @PostMapping("/admin/add")
    public ResponseEntity<AttributeDto> createAttribute(@RequestBody final CreateNewAttributeDto createNewAttributeDto){
      Attribute attribute =   attributeService.addAttribute(createNewAttributeDto);
        return new ResponseEntity<>(AttributeDto.from(attribute),HttpStatus.OK);
    }

    //delete attribute by id
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<AttributeDto> deleteAttribute(@PathVariable final Long id){
        Attribute attribute = attributeService.deleteAttribute(id);
        return new ResponseEntity<>(AttributeDto.from(attribute),HttpStatus.OK);
    }

    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<AttributeDto> editAttribute(@PathVariable final Long id, @RequestBody EditAttributeDto editAttributeDto){
        Attribute attribute = attributeService.editAttribute(id,editAttributeDto);
        return new ResponseEntity<>(AttributeDto.from(attribute),HttpStatus.OK);
    }
}
