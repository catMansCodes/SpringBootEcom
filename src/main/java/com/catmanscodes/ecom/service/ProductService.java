package com.catmanscodes.ecom.service;

import com.catmanscodes.ecom.model.Product;
import com.catmanscodes.ecom.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        log.debug("Calling repository for product id: {}", id);
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found with id: " + id)
        );
    }

    public Product addOrupdateProduct(Product product, MultipartFile image) {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());

        try {
            product.setImageData(image.getBytes());
        } catch (Exception e) {
            log.error("Error processing image file: {}", e.getMessage());
            throw new RuntimeException("Failed to process image file: " + e.getMessage());
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent product with id: {}", id);
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {

        return productRepository.searchProducts(keyword);
    }
}
