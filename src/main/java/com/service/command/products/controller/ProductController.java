package com.service.command.products.controller;

import com.service.command.products.models.Product;
import com.service.command.products.models.ProductsCategory;
import com.service.command.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> CreateProduct(@RequestBody Product product){
        productService.CreateProduct(product);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/nro/{id}")
    public ResponseEntity<?> GetForId(@PathVariable Long id){
        return ResponseEntity.ok(productService.GetForId(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> UpdateProduct(@PathVariable Long id,@RequestBody Product product){
        return ResponseEntity.ok(productService.UpdateProduct(id,product));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> InvalidateProduct(@PathVariable Long id, @RequestBody boolean status){
        return ResponseEntity.ok(productService.UnvalidateProduct(id,status));
    }

    @PutMapping("/stock/{id}")
    public ResponseEntity<?> ChangeStock(@PathVariable Long id,@RequestBody int number){
        return ResponseEntity.ok(productService.ChangeStock(id,number));
    }

    @GetMapping("/list")
    public List<Product> ListActiveProduct(@RequestBody boolean status){
        return productService.ListActiveProduct(status);
    }

    @GetMapping("/list/category")
    public List<Product> ListCategoryProduct(@RequestParam ProductsCategory category, @RequestParam boolean status){
        return productService.ListActiveProductAndCategory(category, status);
    }

    @GetMapping("/name")
    public ResponseEntity<?> GetForName(@RequestBody String string){
        return ResponseEntity.ok(productService.GetForName(string));
    }
}
