package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.product.*;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.filter.ProductSpecification;
import com.StefanSergiu.Licenta.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@AllArgsConstructor
@Service
public class ProductService {
        @Autowired
        FileStore fileStore;
       @Autowired
        ProductRepository productRepository;
       @Autowired
       BrandRepository brandRepository;
       @Autowired
       GenderRepository genderRepository;
        @Autowired
         CategoryRepository categoryRepository;
        @Autowired
        ProductSpecification productSpecification;
        @Autowired
        ProductAttributeRepository productAttributeRepository;

        @Autowired
        ProductSizeRepository productSizeRepository;
    private final TypeRepository typeRepository;


    @Transactional
        public Product addProduct(CreateNewProductModel createNewProductModel){
            Product product = new Product();

            Brand brand = brandRepository.findByName(createNewProductModel.getBrand_name());
            if(brand == null){
                throw new EntityNotFoundException("Brand " + createNewProductModel.getBrand_name() + " not found");
            }

            Gender gender = genderRepository.findByName(createNewProductModel.getGender_name());
            if(gender == null){
                throw new EntityNotFoundException("Gender " + createNewProductModel.getGender_name() + " not found");
            }

            Category category = categoryRepository.findByName(createNewProductModel.getCategory_name())
                    .orElseThrow(()->new EntityNotFoundException("Category with name "+ createNewProductModel.getCategory_name() + " not found"));

            //set every field of the product (from createNewProductModel)
            product.setGender(gender);
            product.setBrand(brand);
            product.setCategory(category);
            product.setName(createNewProductModel.getName());

            product.setPrice(createNewProductModel.getPrice());
            product.setDescription(createNewProductModel.getDescription());
            product.setStock(createNewProductModel.getStock());
            //add the product to the other side of every relationship
            brand.addProduct(product);
            gender.addProduct(product);
            category.addProduct(product);

            return productRepository.save(product);
        }


        public byte[] downloadProductImage(Long id) {
            Product product = productRepository.findById(id).get();
            return fileStore.download(product.getImagePath(), product.getImageFileName());
        }

        public List<Product> getProducts(){
            return StreamSupport
                    .stream(productRepository.findAll().spliterator(),false)
                    .collect(Collectors.toList());
        }

        public List<Product>  getProductsByBrandName(String brandName){
            return  StreamSupport
                    .stream(productRepository.findByBrandName(brandName).spliterator(),false)
                    .collect(Collectors.toList());
        }

        @Transactional
        public Product deleteProduct(Long id){
            Product product = getProduct(id);
            product.getBrand().getProducts().remove(product);   //detach product from brand
            product.getGender().getProducts().remove(product);  //detach product from gender
            product.getCategory().getProducts().remove(product); //detach product from category
            productRepository.delete(product);
            return product;
        }



        @Transactional
        public void updateProductImage(Long productId, String imagePath, String imageFileName) {
            Product product = getProduct(productId);

            product.setImageFileName(imageFileName);
            product.setImagePath(imagePath);
            productRepository.save(product);
        }

        public Page<ProductCardDto> filterProducts(ProductRequestModel request, Pageable pageable){
            Page<Product> productsPage = productRepository.findAll(productSpecification.getProducts(request), pageable);
            List<Product> products = productsPage.getContent();
            List <ProductCardDto>productCardDtos = new ArrayList<>();
            for(Product product : products){
                ProductCardDto dto = ProductCardDto.from(product);
                String placeholder = "N/A";
                byte[] imageData = placeholder.getBytes();
                try{
                    imageData  = fileStore.download(product.getImagePath(), product.getImageFileName());
                }catch(Exception e){
                   System.out.println("Image field is null");
                }
                System.out.println("image downloaded");
                dto.setImage(imageData);

                //placeholder for image downloading
                // byte[] placeholderByteArray = new byte[16992];
                //productDto.setImage(placeholderByteArray);
                productCardDtos.add(dto);
            }
            return new PageImpl<>(productCardDtos, pageable,products.size());}

//        public  Page<ProductCardDto> getAllProducts(ProductRequestModel request, Pageable pageable){
//            //get the product entries based on the request (grouped by name)

//            //generate a dto List
//            List<ProductCardDto> productCardDtos = new ArrayList<>();
//            //save the names of the result in a list
//            List<String> productNames = new ArrayList<>();
//            for (Product product : products) {
//                productNames.add(product.getName());
//            }
//            //for each saved name, ...
//            for (String productName : productNames) {
//                //generate a new dto
//                ProductCardDto productCardDto = new ProductCardDto();
//                // get product entries with that name
//                List<Product> filteredProducts = productRepository.findByName(productName);
//                List<ProductVariationDto> sizes = new ArrayList<>();
//                //for each entry
//                for (Product product : filteredProducts){
//
//                    if(productCardDto.getName() ==null){
//
//                        productCardDto.setName(product.getName());
//                        productCardDto.setDescription(product.getDescription());
//                        productCardDto.setPrice(product.getPrice());
//                        productCardDto.setCategory(product.getCategory().getName());
//                        productCardDto.setBrand(product.getCategory().getName());
//                        productCardDto.setGender(product.getGender().getName());
//                        String placeholder = "N/A";
//                        byte[] imageData = placeholder.getBytes();
//                        try{
//                            System.out.println("image downloaded");
//                           imageData  = fileStore.download(product.getImagePath(), product.getImageFileName());
//                        }catch(Exception e){
//                            System.out.println("Image field is null");
//                        }
//                        productCardDto.setImage(imageData);
//
//                        //placeholder for image downloading
//                        // byte[] placeholderByteArray = new byte[16992];
//                        //productDto.setImage(placeholderByteArray);
//                    }
//                    ProductVariationDto var = new ProductVariationDto();
//                    var.setId(product.getId());
//
//                    sizes.add(var);
//                }
//                productCardDto.setSizes(sizes);
//                productCardDtos.add(productCardDto);
//            }
//
//           return new PageImpl<>(productCardDtos, pageable, totalDistinctNames);
//        }
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(()->
                new EntityNotFoundException("Product with id " + productId + " does not exist"));
    }
    public List<Product> getProductByName(String productName) {

        List<Product> products = productRepository.findAllByName(productName);
            return products;
    }



    @Transactional
    public Long decreaseStock(Long quantity, Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product with id " + productId + " does not exist"));
        if(product.getStock()>= quantity)
        product.setStock(product.getStock()-quantity);

        return quantity;

    }
}
