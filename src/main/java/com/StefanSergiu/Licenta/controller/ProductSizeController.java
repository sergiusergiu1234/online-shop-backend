package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.productAttribute.ProductAttributeDto;
import com.StefanSergiu.Licenta.dto.productSize.NewProductSizeModel;
import com.StefanSergiu.Licenta.dto.productSize.ProductSizeDto;
import com.StefanSergiu.Licenta.entity.Product;
import com.StefanSergiu.Licenta.entity.ProductAttribute;
import com.StefanSergiu.Licenta.entity.ProductSize;
import com.StefanSergiu.Licenta.service.ProductSizeService;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productSizes")
@CrossOrigin(origins = {"http://localhost:3000","https://slope-emporium.vercel.app"})
public class ProductSizeController {

    @Autowired
    ProductSizeService productSizeService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductSizeDto>> getProductSizes(){
        List<ProductSize> productSizes = productSizeService.getAll();

        List<ProductSizeDto> productSizeDtoList = productSizes.stream().map(ProductSizeDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(productSizeDtoList, HttpStatus.OK);
    }

    @PostMapping("/newProductSize")
    public ResponseEntity<ProductSizeDto> addNewProductSize(@RequestBody NewProductSizeModel newProductSizeModel){
       ProductSize productSize = productSizeService.createNewProductSize(newProductSizeModel);
       return new ResponseEntity<>(ProductSizeDto.from(productSize), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productSizeId}")
    public ResponseEntity<ProductSizeDto> deleteProductSize(@PathVariable Long productSizeId){
        ProductSize productSize = productSizeService.deleteProductSize(productSizeId);
        return new ResponseEntity<>(ProductSizeDto.from(productSize),HttpStatus.OK);
    }
}
