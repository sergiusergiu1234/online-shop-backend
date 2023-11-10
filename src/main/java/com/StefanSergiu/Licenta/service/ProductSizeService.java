package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.productSize.NewProductSizeModel;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.repository.ProductSizeRepository;
import com.StefanSergiu.Licenta.repository.SizeRepository;
import com.StefanSergiu.Licenta.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSizeService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ProductSizeRepository productSizeRepository;
    @Autowired
    TypeRepository typeRepository;
    @Transactional
    public ProductSize getProductSize(Long productId, Long SizeId){
        Product product = productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException());
        Size size = sizeRepository.findById(SizeId).orElseThrow(()->new EntityNotFoundException());

        ProductSizeKey productSizeKey = new ProductSizeKey();
        productSizeKey.setSizeId(SizeId);
        productSizeKey.setProductId(productId);

        return productSizeRepository.findById(productSizeKey)
                .orElseThrow(()->new EntityNotFoundException("ProductSize with id "+ productSizeKey+ " not found!"));
    }



    public List<ProductSize> getProductSizesByProduct(Long productId){
        return productSizeRepository.findAllByProductId(productId);
    }
    public List<ProductSize> getAll(){
        return productSizeRepository.findAll();
    }

    @Transactional
    public ProductSize createNewProductSize(NewProductSizeModel newProductSizeModel) {
        Product product = productRepository.findById(newProductSizeModel.getProductId())
                .orElseThrow(()->new EntityNotFoundException("Product with id "+ newProductSizeModel.getProductId() +" not found"));
        Size size = sizeRepository.findById(newProductSizeModel.getSizeId())
                .orElseThrow(()->new EntityNotFoundException(("Size with id " + newProductSizeModel.getSizeId()+ " not found")));
        ProductSizeKey key = new ProductSizeKey();
        key.setProductId(product.getId());
        key.setSizeId(size.getId());

        ProductSize productSize = new ProductSize();
        productSize.setId(key);
        productSize.setProduct(product);
        productSize.setSize(size);


        productSize.setStock(newProductSizeModel.getStock());


        productSizeRepository.save(productSize);

        return productSize;
    }
}
