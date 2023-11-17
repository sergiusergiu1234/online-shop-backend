package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.config.BucketName;
import com.StefanSergiu.Licenta.dto.product.*;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.service.FavoriteService;
import com.StefanSergiu.Licenta.service.FileStore;
import com.StefanSergiu.Licenta.service.ProductService;
import com.StefanSergiu.Licenta.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        Product product = productService.addProduct(createNewProductModel);
        return new ResponseEntity<>(ProductDto.from(product), HttpStatus.OK);
    }


    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Page<ProductCardDto>> getproducts(@RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "brands",required = false)String brands,
                                                        @RequestParam(name = "genders", required = false)String genders,
                                                        @RequestParam(name = "category_name",required = false)String category_name,
                                                        @RequestParam(name = "price",required = false)Float price,
                                                        @RequestParam(name = "minPrice", required = false) Float minPrice,
                                                        @RequestParam(name = "maxPrice", required = false) Float maxPrice,
                                                        @RequestParam(name = "type_name", required = false) String type_name,
                                                        @RequestParam(name = "attributes", required = false) String attributesParam,
                                                        @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                        @RequestParam(name = "sizes", required = false) String sizes,
                                                        @RequestParam(name = "size", defaultValue = "8") int size){


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

        Page<ProductCardDto> productsPage = productService.filterProducts(request, pageable);

        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.MINUTES);
        return ResponseEntity.ok().cacheControl(cacheControl).body(productsPage);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value ="/Page/{productName}")
    public ResponseEntity<ProductDto> getProductDetails(@PathVariable final String productName){

        ProductDto productDto = productService.getProductByName(productName);

        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @DeleteMapping(value ="/admin/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable final Long id){
        Product product = productService.getProduct(id);

        Product deletedProduct = productService.deleteProduct(id);
        if(product.getImagePath() != null && product.getImageFileName() != null){
            fileStore.deleteImage(product.getImagePath(), product.getImageFileName());
        }

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

        return ResponseEntity.status(HttpStatus.OK).body("Image edited succesfully");
    }


    @GetMapping("/admin/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") Long productId){
        Product product = productService.getProduct(productId);
        return new ResponseEntity<>(ProductDto.from(product),HttpStatus.OK);
    }
}
