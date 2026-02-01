package com.quiz_app.quiz_app.service;

import com.quiz_app.quiz_app.model.Cart;
import com.quiz_app.quiz_app.model.Products;
import com.quiz_app.quiz_app.repo.CartRepo;
import com.quiz_app.quiz_app.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Autowired
    private CartRepo cartRepo;


    public ResponseEntity<List<Products>> loadAllProducts() {
        List<Products> products = productRepo.findAll();
        return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> addProduct(Products product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        Products savedProduct = productRepo.save(product);

        if (savedProduct.getId() != null) {
            return ResponseEntity.ok("Product added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add product");
        }
    }

    public ResponseEntity<byte[]> getProductImageById(long id) {
        Products product = productRepo.getProductsById(id).get(0);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(product.getImageData());
    }

    public ResponseEntity<Integer> addProductToCart(long id, int qty) {
        Products product = productRepo.getProductsById(id).get(0);
        int productBalanceStock = product.getStock();
        Cart cart = null;
        if (productBalanceStock >= qty) {
            cart = new Cart();
            cart.setId(product.getId());
            cart.setProductQty(qty);
            cartRepo.save(cart);

            product.setStock(productBalanceStock-qty);
            productRepo.save(product);
            return new ResponseEntity<>(product.getStock(), HttpStatus.OK);
        }

        return new ResponseEntity<>(cart.getProductQty(), HttpStatus.OK);
    }
}
