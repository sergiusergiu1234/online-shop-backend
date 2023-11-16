package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.product.*;
import com.StefanSergiu.Licenta.dto.productSize.ProductSizeDto;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
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

        @Autowired
        FavoriteService favoriteService;

        @Autowired
        UserService userService;
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

    @Async
    public CompletableFuture<List<ProductCardDto>> downloadImagesAsync(List<Product> products) {
        return CompletableFuture.supplyAsync(() ->
                products.parallelStream().map(product -> {
                    ProductCardDto dto = ProductCardDto.from(product);
                    String placeholder = "N/A";
                    byte[] imageData = placeholder.getBytes();
                    try {
                        imageData = fileStore.download(product.getImagePath(), product.getImageFileName());
                        System.out.println("Image downloaded for product: " + product.getId());
                    } catch (Exception e) {
                        System.err.println("Error downloading image for product: " + product.getId());
                        e.printStackTrace();
                    }
                    dto.setImage(imageData);
                    return dto;
                }).collect(Collectors.toList())
        );
    }
        @Transactional
        public Page<ProductCardDto> filterProducts(ProductRequestModel request, Pageable pageable){
            Page<Product> productsPage = productRepository.findAll(productSpecification.getProducts(request), pageable);
            List<Product> products = productsPage.getContent();

            CompletableFuture<List<ProductCardDto>> productCardDtosFuture = downloadImagesAsync(products);
            List<ProductCardDto> productCardDtos =productCardDtosFuture.join();

            return new PageImpl<>(productCardDtos, pageable,products.size());}


    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(()->
                new EntityNotFoundException("Product with id " + productId + " does not exist"));
    }
    public ProductDto getProductByName(String productName) {
        Product product = productRepository.findByName(productName);
        ProductDto productDto = ProductDto.from(product);
        try{
            productDto.setImage(fileStore.download(product.getImagePath(), product.getImageFileName()));
        }catch(IllegalArgumentException exception){
            String placeholder = "N/A";
            byte[] imageData = placeholder.getBytes();
            productDto.setImage(imageData);
            System.out.println("couldn't do the do");
        }

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserInfo user = userService.getLoggedInUser(username);
            Integer userId = user.getId();


            List<Favorite> favorites = new ArrayList<>();
            favorites = favoriteService.getFavoriteByUser(userId);

            for(ProductSizeDto productSizeDto : productDto.getSizes()){
                boolean isFavorite = favorites.stream()
                        .anyMatch(favorite -> favorite.getProductSize().getProductSizeId().equals(productSizeDto.getProductId()));

                productSizeDto.setFavorite(isFavorite);
            }
        }catch (NullPointerException exception){
            System.out.println("User is not logged");
        }

        return productDto;
    }

    @Transactional
    public Long decreaseStock(Long quantity, Long productSizeId){
        ProductSize productSize = productSizeRepository.findById(productSizeId)
                .orElseThrow(()-> new EntityNotFoundException("Product size with id " + productSizeId + " does not exist"));
        if(productSize.getStock()>= quantity)
        productSize.setStock(productSize.getStock()-quantity);

        return quantity;

    }
}
