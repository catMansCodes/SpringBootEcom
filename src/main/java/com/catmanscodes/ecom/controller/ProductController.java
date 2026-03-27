package com.catmanscodes.ecom.controller;

import com.catmanscodes.ecom.model.Product;
import com.catmanscodes.ecom.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        log.info("Fetching all products");
        return new ResponseEntity<List<Product>>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id) {
        log.info("Fetching product with id: {}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestPart Product product, @RequestPart("imageFile") MultipartFile image) {
        log.info("Creating new product request with image: {}", product.getName());

        Product savedProduct = productService.addOrupdateProduct(product, image);

        return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable("id") Integer id) {
        log.info("Fetching image for product with id: {}", id);

        Product product = productService.getProductById(id);

        if (product.getImageData() == null) {
            log.warn("No image found for product with id: {}", id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", product.getImageType())
                .body(product.getImageData());
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable("id") Integer id, @RequestPart Product product, @RequestPart("imageFile") MultipartFile image) {
        log.info("Updating product with id: {}", id);

        productService.addOrupdateProduct(product, image);

        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id) {
        log.info("Deleting product with id: {}", id);

        if(productService.getProductById(id) == null) {
            log.warn("Product with id: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }

        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        log.info("Searching for products");

        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

}