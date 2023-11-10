package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.type.PlainTypeDto;
import com.StefanSergiu.Licenta.dto.type.TypeDto;
import com.StefanSergiu.Licenta.entity.Type;
import com.StefanSergiu.Licenta.repository.AttributeRepository;
import com.StefanSergiu.Licenta.service.AttributeService;
import com.StefanSergiu.Licenta.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/types")
@CrossOrigin(origins = {"http://localhost:3000","https://slope-emporium.vercel.app"})
public class TypeController {

    @Autowired
    TypeService typeService;

    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    AttributeService attributeService;

    //add Type
    @PostMapping("/admin/add")
    public ResponseEntity<TypeDto> addType(@RequestBody TypeDto typeDto){
        Type type = typeService.addType(typeDto);
        return new ResponseEntity<>(TypeDto.from(type), HttpStatus.OK);
    }

    //get all Types
    @GetMapping("/all")
    ResponseEntity<List<TypeDto>> getTypes(){
        List<Type> Types = typeService.getTypes();
        List<TypeDto> typeDtos = Types.stream().map(TypeDto::from).collect(Collectors.toList());


        return new ResponseEntity<>(typeDtos,HttpStatus.OK);
    }




    //delete Type by id
    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity<TypeDto> deleteType(@PathVariable final Long id){
        Type type = typeService.deleteType(id);
        return new ResponseEntity<>(TypeDto.from(type),HttpStatus.OK);
    }

    @PutMapping(value = "/admin/edit/{id}")
    public ResponseEntity<TypeDto> editType(@PathVariable final Long id, @RequestBody PlainTypeDto plainTypeDto){
        Type type = typeService.editType(id,plainTypeDto);
        return new ResponseEntity<>(TypeDto.from(type), HttpStatus.OK);
    }

    @GetMapping(value = "/admin/get/c={categoryName}")
    public ResponseEntity<TypeDto> getByCategory(@PathVariable final String categoryName){
        Type type = typeService.getTypeByCategory(categoryName);
        return new ResponseEntity<>(TypeDto.from(type),HttpStatus.OK);
    }
}
