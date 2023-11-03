package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.config.BucketName;
import com.StefanSergiu.Licenta.dto.brand.BrandDto;
import com.StefanSergiu.Licenta.dto.favorite.FavoriteDto;
import com.StefanSergiu.Licenta.dto.product.*;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.service.FavoriteService;
import com.StefanSergiu.Licenta.service.FileStore;
import com.StefanSergiu.Licenta.service.ProductService;
import com.StefanSergiu.Licenta.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.http.entity.ContentType.*;
//@Autowired
//    AmazonS3 s3Client;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = {"http://localhost:3000","https://slope-emporium.vercel.app"})
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    FileStore fileStore;
    @Autowired
    UserService userService;

    @Autowired
    FavoriteService favoriteService;
    @Autowired
    private ProductRepository productRepository;


    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        System.out.println("This works");
        return new ResponseEntity<>("This works",HttpStatus.OK);
    }
    @PostMapping("/admin/add")
    public ResponseEntity<?> addProduct(@RequestBody final CreateNewProductModel createNewProductModel){
        try{
            Product product = productService.addProduct(createNewProductModel);
            return new ResponseEntity<>(ProductDto.from(product), HttpStatus.OK);
        }catch(DataIntegrityViolationException e) {
            // Handle the exception and send an error response

            String errorMessage = e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Page<ProductCardDto>> getproducts(@RequestParam(name = "name", required = false) String name,
                                                            @RequestParam(name = "brands",required = false)String brands,
                                                            @RequestParam(name = "genders", required = false)String genders,
                                                            @RequestParam(name = "category_name",required = false)String category_name,
                                                            @RequestParam(name = "price",required = false)Float price,
                                                            @RequestParam(name ="minPrice", required = false) Float minPrice,
                                                            @RequestParam(name = "maxPrice", required = false) Float maxPrice,
                                                            @RequestParam(name = "type_name", required = false) String type_name,
                                                            @RequestParam(name = "attributes", required = false) String attributesParam,
                                                            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                            @RequestParam(name = "sizes", required = false) String sizes,
                                                            @RequestParam(name = "size", defaultValue = "12") int size){

        // Parse attributes parameter into a Map<String, String>
        Map<String, String> attributes = new HashMap<>();
        if (attributesParam != null) {
            String[] pairs = attributesParam.split("_");
            for (String pair : pairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    attributes.put(parts[0], parts[1]);
                }
            }
        }
        ProductRequestModel request = new ProductRequestModel(name,brands,category_name,genders,price, minPrice, maxPrice, type_name,attributes,sizes);

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.Direction.ASC,"name");

        Page<ProductCardDto> productsPage = productService.getAllProducts(request, pageable);

        CacheControl cacheControl = CacheControl.maxAge(3, TimeUnit.DAYS);
        return ResponseEntity.ok().cacheControl(cacheControl).body(productsPage);
    }
    @PreAuthorize("permitAll()")
    @GetMapping("/{productName}")
    public ResponseEntity<List<ProductDto>> getProduct(@PathVariable final String productName){

        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        System.out.println(user);
        ////
        Integer userId = null;
        if (user != null) {
            userId = user.getId();
        }
        //favorite list by logged user. If user is null, then all productDto's are false on favorite
        List<Favorite> favorites;
        if(userId != null){
            favorites = favoriteService.getFavoriteByUser(userId);
        } else {
            favorites = null;
        }

        List<Product> products = productService.getProductByName(productName);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = ProductDto.from(product);
            if(favorites != null){
                for(Favorite fav : favorites){
                    if(fav.getProduct().getId() == product.getId()){
                        productDto.setIsFavorite(true);
                        break;
                    }
                }
            }
            productDtoList.add(productDto);
        }
        return new ResponseEntity<>(productDtoList,HttpStatus.OK);
    }
    @DeleteMapping(value ="/admin/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable final Long id){
        Product product = productService.getProduct(id);

        if(productRepository.countByName(product.getName())>1) {
            Product deletedProduct = productService.deleteProduct(id);
            System.out.println("Am ajuns aici!");
            return new ResponseEntity<>(ProductDto.from(deletedProduct),HttpStatus.OK);
        }

        Product deletedProduct = productService.deleteProduct(id);
        fileStore.deleteImage(product.getImagePath(), product.getImageFileName());
        return new ResponseEntity<>(ProductDto.from(deletedProduct),HttpStatus.OK);
    }

    @GetMapping(value = "{id}/image/download")
    public byte[] downloadProductImage(@PathVariable("id") Long id){
        Product product = productService.getProduct(id);
        return fileStore.download(product.getImagePath(), product.getImageFileName());
    }
    @PostMapping(value = "/admin/add-image/{id}")
        public ResponseEntity<String> addProductImage(@PathVariable Long id, @RequestParam("file")MultipartFile file) {
        //check if product exists
        Product product = productService.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>("Product not found!", HttpStatus.NOT_FOUND);
        }
        // check if name already exists in database
        //if present, don't upload the image and only set the path to the existing one
        if(productRepository.countByName(product.getName())!=1){
            List<Product> products = productRepository.findAllByName(product.getName());
            //set the existing image to the product
            productService.updateProductImage(id, products.get(0).getImagePath(),products.get(0).getImageFileName());

            return ResponseEntity.status(HttpStatus.OK).body("Image added succesfully");
        }

        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save Image in S3
        String path = String.format("%s/images/%s", BucketName.PRODUCT_IMAGE.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());
        try {
            fileStore.upload(path, fileName, Optional.of(metadata), file.getInputStream());

        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        //set the image to the product
        productService.updateProductImage(id, path,fileName);

        return ResponseEntity.status(HttpStatus.OK).body("Image added succesfully");
    }


    @PutMapping(value = "/admin/edit-image/{id}")
    public ResponseEntity<String> editProductImage(@PathVariable Long id, @RequestParam("file")MultipartFile file){


        Product product = productService.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>("Product not found!", HttpStatus.NOT_FOUND);
        }

        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }

        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save Image in S3
        String path = String.format("%s/images/%s", BucketName.PRODUCT_IMAGE.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());

        try {
            fileStore.upload(path, fileName, Optional.of(metadata), file.getInputStream());

        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }

        List<Product> products = productRepository.findAllByName(product.getName());
        for (Product p : products){
            //set the image to the product
            productService.updateProductImage(p.getId(), path,fileName);
        }


        return ResponseEntity.status(HttpStatus.OK).body("Image edited succesfully");
    }

    @GetMapping("/sizes/{typeId}")
    public List<String> getSizesByTypeId(@PathVariable("typeId") Long typeId) {
        return productService.getSizesByTypeId(typeId);
    }

    @GetMapping("/admin/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") Long productId){
        Product product = productService.getProduct(productId);
        return new ResponseEntity<>(ProductDto.from(product),HttpStatus.OK);
    }
}
