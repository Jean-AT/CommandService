package com.service.command.products.service;

import com.service.command.products.models.Product;
import com.service.command.products.models.ProductsCategory;
import com.service.command.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product CreateProduct(Product product){
        if (repository.findByName(product.getName()).isPresent()){
            throw new RuntimeException("The Product already exist");
        }
        return repository.save(product);
    }

    public Product GetForId(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResolutionException("The product with "+id+" it's not found"));
    }

    public Product UpdateProduct(Long id,Product product){
        return repository.findById(id).map(existingProduct->{
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock(product.getStock());
            existingProduct.setStatus(product.isStatus());
            existingProduct.setCategory(product.getCategory());

            return repository.save(existingProduct);

        }).orElseThrow(()-> new RuntimeException("Product not found with id "+id));
    }

    public Product UnvalidateProduct(Long id,Boolean active){
        Product p = GetForId(id);
        p.setStatus(active);
        return repository.save(p);
    }

    public Product ChangeStock(Long id,int stock){
        Product p = GetForId(id);
        p.setStock(stock);
        return repository.save(p);
    }

    public List<Product> ListActiveProduct(boolean status){
        return repository.findByStatus(status);
    }

    public List<Product> ListActiveProductAndCategory(ProductsCategory category,boolean status){
        return repository.findByCategoryAndStatus(category,status);
    }

    public Product GetForName(String name){
        return repository.findByName(name)
                .orElseThrow(()-> new RuntimeException("The product with the name "+name+" is not found"));
    }
}
