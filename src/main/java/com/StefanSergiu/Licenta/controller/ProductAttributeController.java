package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.productAttribute.CreateProductAttributeModel;
import com.StefanSergiu.Licenta.dto.productAttribute.ProductAttributeDto;
//import com.StefanSergiu.Licenta.entity.ProductAttributeKey;
import com.StefanSergiu.Licenta.entity.ProductAttribute;
import com.StefanSergiu.Licenta.service.ProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productAttributes")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductAttributeController {
    @Autowired
    ProductAttributeService productAttributeService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductAttributeDto>> getProductAttributes(){
        List<ProductAttribute> productAttributes = productAttributeService.getProductAttributes();

        List<ProductAttributeDto> productAttributeDtoList = productAttributes.stream().map(ProductAttributeDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(productAttributeDtoList, HttpStatus.OK);
    }

    @PostMapping("admin/add")
    public ResponseEntity<ProductAttributeDto> newProductAttribute(@RequestBody final CreateProductAttributeModel createProductAttributeModel){
        ProductAttribute productAttribute = productAttributeService.createProductAttribute(createProductAttributeModel);
        return new ResponseEntity<>(ProductAttributeDto.from(productAttribute),HttpStatus.OK);
    }

    @PutMapping("admin/edit")
    public ResponseEntity<ProductAttributeDto> editProductAttribute(@RequestBody final CreateProductAttributeModel createProductAttributeModel){
        ProductAttribute productAttribute = productAttributeService.editProductAttribute(createProductAttributeModel);
        return new ResponseEntity<>(ProductAttributeDto.from(productAttribute), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("admin/delete/{productId}&{attributeId}")
    public ResponseEntity<ProductAttributeDto> deleteProductAttribute(@PathVariable final Long productId, @PathVariable final Long attributeId){
        ProductAttribute productAttribute = productAttributeService.deleteProductAttribute(productId,attributeId);
        return new ResponseEntity<>(ProductAttributeDto.from(productAttribute),HttpStatus.OK);
    }
}
