package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.brand.BrandDto;
import com.StefanSergiu.Licenta.entity.Brand;
import com.StefanSergiu.Licenta.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brands")
@CrossOrigin(origins = "https://online-shop-frontend-nine.vercel.app")
public class BrandController {
    @Autowired
    BrandService brandService;

    //add brand
    @PostMapping("/admin/add")
    public ResponseEntity<BrandDto> addBrand(@RequestBody  BrandDto brandDto){
        Brand brand = brandService.addBrand(Brand.from(brandDto));
        return new ResponseEntity<>(BrandDto.from(brand), HttpStatus.OK);
    }

    //get all brands
    @GetMapping ("/all")
    ResponseEntity<List<BrandDto>> getBrands(){
        List<Brand> brands = brandService.getBrands();
        List<BrandDto> brandDtos = brands.stream().map(BrandDto::from).collect(Collectors.toList());
      //  System.out.println(brandDtos.stream().map(BrandDto::getName).collect(Collectors.toList()));
        return new ResponseEntity<>(brandDtos,HttpStatus.OK);
    }

    //get brand by id
    @GetMapping(value = "{id}")
    public ResponseEntity<BrandDto> getBrand(@PathVariable final Long id){
        Brand brand = brandService.getBrand(id);
        return new ResponseEntity<>(BrandDto.from(brand),HttpStatus.OK);
    }


    //delete brand by id
    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity<BrandDto> deleteBrand(@PathVariable final Long id){
        Brand Brand = brandService.deleteBrand(id);
        return new ResponseEntity<>(BrandDto.from(Brand),HttpStatus.OK);
    }



    @DeleteMapping("/admin/deleteAll")
    public void deleteAllBrands(){
        brandService.deleteAllBrands();
    }


    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<BrandDto> editBrand(@PathVariable final Long id,@RequestBody BrandDto brandDto){
        Brand brand = brandService.editBrand(id, brandDto);
        return new ResponseEntity<>(BrandDto.from(brand),HttpStatus.OK);
    }

}


