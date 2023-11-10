package com.StefanSergiu.Licenta.dto.product;

import com.StefanSergiu.Licenta.config.BucketName;
import com.StefanSergiu.Licenta.dto.brand.PlainBrandDto;
import com.StefanSergiu.Licenta.dto.category.PlainCategoryDto;
import com.StefanSergiu.Licenta.dto.gender.PlainGenderDto;
import com.StefanSergiu.Licenta.dto.productAttribute.PlainProductAttributeDto;
import com.StefanSergiu.Licenta.dto.productAttribute.ProductAttributeDto;
import com.StefanSergiu.Licenta.entity.Attribute;
import com.StefanSergiu.Licenta.entity.Product;
import com.StefanSergiu.Licenta.entity.ProductAttribute;
import com.StefanSergiu.Licenta.service.FileStore;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private Float price;
    private PlainBrandDto brand;
    private PlainGenderDto gender;
    private PlainCategoryDto category;
    private String description;
    private Boolean isFavorite;
    private byte[] image;

    private Long stock;
    //**
    private List<PlainProductAttributeDto> attributes = new ArrayList<>();
    public static ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());

        productDto.setStock(product.getStock());
        if(Objects.nonNull(product.getBrand())){
            productDto.setBrand(PlainBrandDto.from(product.getBrand()));
        }
        if(Objects.nonNull(product.getGender())){
            productDto.setGender(PlainGenderDto.from(product.getGender()));
        }
        if(Objects.nonNull(product.getCategory())){
            productDto.setCategory(PlainCategoryDto.from(product.getCategory()));
        }
        //**
        productDto.setAttributes(product.getProductAttributes().stream().map(PlainProductAttributeDto::from).collect(Collectors.toList()));


        return productDto;
    }

    public static ProductDto from(Product product, byte[] image) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());

        if(Objects.nonNull(product.getBrand())){
            productDto.setBrand(PlainBrandDto.from(product.getBrand()));
        }
        if(Objects.nonNull(product.getGender())){
            productDto.setGender(PlainGenderDto.from(product.getGender()));
        }
        if(Objects.nonNull(product.getCategory())){
            productDto.setCategory(PlainCategoryDto.from(product.getCategory()));
        }
        //**
        productDto.setAttributes(product.getProductAttributes().stream().map(PlainProductAttributeDto::from).collect(Collectors.toList()));

        productDto.image=image;
        return productDto;
    }
}
