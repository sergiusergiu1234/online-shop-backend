package com.StefanSergiu.Licenta.controller;


import com.StefanSergiu.Licenta.dto.brand.BrandDto;
import com.StefanSergiu.Licenta.dto.gender.EditGenderDto;
import com.StefanSergiu.Licenta.dto.gender.GenderDto;
import com.StefanSergiu.Licenta.entity.Brand;
import com.StefanSergiu.Licenta.entity.Gender;
import com.StefanSergiu.Licenta.repository.GenderRepository;
import com.StefanSergiu.Licenta.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genders")
@CrossOrigin(origins = "https://online-shop-frontend-nine.vercel.app")

public class GenderController {
    @Autowired
    GenderService genderService;

    //add gender
    @PostMapping("/admin/add")
    public ResponseEntity<GenderDto> addGender(@RequestBody GenderDto genderDto){
        Gender gender = genderService.addGender(Gender.from(genderDto));
        return new ResponseEntity<>(GenderDto.from(gender), HttpStatus.OK);
    }

    //get all genders
    @GetMapping ("/all")
    ResponseEntity<List<GenderDto>> getGender(){
        List<Gender> genders = genderService.getGenders();
        List<GenderDto> genderDtos = genders.stream().map(GenderDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(genderDtos,HttpStatus.OK);
    }

    //get gender by id
    @GetMapping(value = "{id}")
    public ResponseEntity<GenderDto> getGender(@PathVariable final Long id){
        Gender gender = genderService.getGender(id);
        return new ResponseEntity<>(GenderDto.from(gender),HttpStatus.OK);
    }


    //delete gender by id
    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<GenderDto> deleteGender(@PathVariable final Long id){
        Gender gender = genderService.deleteGender(id);
        return new ResponseEntity<>(GenderDto.from(gender),HttpStatus.OK);
    }


    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<GenderDto> editGender(@PathVariable final Long id, @RequestBody EditGenderDto editGenderDto){
        Gender gender = genderService.editGender(id,editGenderDto);
        return new ResponseEntity<>(GenderDto.from(gender),HttpStatus.OK);
    }
}
