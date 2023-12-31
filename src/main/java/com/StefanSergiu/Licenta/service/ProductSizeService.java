package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.productSize.NewProductSizeModel;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.exception.ProductSizeTypeConflictException;
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
    public ProductSize getProductSize(Long productSizeId){

        return productSizeRepository.findById(productSizeId)
                .orElseThrow(()->new EntityNotFoundException("ProductSize with id "+ productSizeId+ " not found!"));
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
        if(product.getCategory().getType().equals(size.getType())){
            ProductSize productSize = new ProductSize();
            productSize.setProduct(product);
            productSize.setSize(size);
            productSize.setStock(newProductSizeModel.getStock());
            productSizeRepository.save(productSize);
            return productSize;
        }else{
            throw new ProductSizeTypeConflictException("Product was linked to type id "+ product.getCategory().getType().getId()
                    +", and the linked size is linked to type with id" + size.getType().getId()
                    +". They must have the same type.");
        }
    }
    @Transactional
    public ProductSize deleteProductSize(Long productSizeId){
        ProductSize productSize = productSizeRepository.findById(productSizeId)
                .orElseThrow(()-> new EntityNotFoundException("Product size with id "+productSizeId + " not found"));

        productSizeRepository.delete(productSize);
        return productSize;
    }
}
