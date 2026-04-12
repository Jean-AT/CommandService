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
    public ResponseEntity<?> CreateProduct(@RequestBody Product product,@CookieValue(name = "HttpsOnly", required = false) String validation){
        productService.CreateProduct(product,validation);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/nro/{id}")
    public ResponseEntity<?> GetForId(@PathVariable Long id,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return ResponseEntity.ok(productService.GetForId(id,validation));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> UpdateProduct(@PathVariable Long id,@RequestBody Product product,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return ResponseEntity.ok(productService.UpdateProduct(id,product,validation));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> InvalidateProduct(@PathVariable Long id, @RequestBody boolean status,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return ResponseEntity.ok(productService.UnvalidateProduct(id,status,validation));
    }

    @PutMapping("/stock/{id}")
    public ResponseEntity<?> ChangeStock(@PathVariable Long id,@RequestBody int number,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return ResponseEntity.ok(productService.ChangeStock(id,number,validation));
    }

    @GetMapping("/list")
    public List<Product> ListActiveProduct(@RequestBody boolean status,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return productService.ListActiveProduct(status,validation);
    }

    @GetMapping("/list/category")
    public List<Product> ListCategoryProduct(@RequestParam ProductsCategory category, @RequestParam boolean status,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return productService.ListActiveProductAndCategory(category, status,validation);
    }

    @GetMapping("/name")
    public ResponseEntity<?> GetForName(@RequestBody String string,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return ResponseEntity.ok(productService.GetForName(string,validation));
    }
}
