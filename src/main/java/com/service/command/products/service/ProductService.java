package com.service.command.products.service;

import com.service.command.config.ConfigAcces;
import com.service.command.products.models.Product;
import com.service.command.products.models.ProductsCategory;
import com.service.command.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ConfigAcces setting;

    public Product CreateProduct(Product product,String validation){
        if(setting.validateToken(validation)){
            if (repository.findByName(product.getName()).isPresent()){
                throw new RuntimeException("The Product already exist");
            }
            return repository.save(product);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public Product GetForId(Long id,String validation){
        if(setting.validateToken(validation)){
            return repository.findById(id)
                    .orElseThrow(() -> new ResolutionException("The product with "+id+" it's not found"));
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public Product UpdateProduct(Long id,Product product,String validation){
        if(setting.validateToken(validation)){
            return repository.findById(id).map(existingProduct->{
                existingProduct.setName(product.getName());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setStock(product.getStock());
                existingProduct.setStatus(product.isStatus());
                existingProduct.setCategory(product.getCategory());

                return repository.save(existingProduct);

            }).orElseThrow(()-> new RuntimeException("Product not found with id "+id));
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public Product UnvalidateProduct(Long id,Boolean active,String validation){
        if(setting.validateToken(validation)){
            Product p = GetForId(id,validation);
            p.setStatus(active);
            return repository.save(p);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public Product ChangeStock(Long id,int stock,String validation){
        if(setting.validateToken(validation)){
            Product p = GetForId(id,validation);
            p.setStock(stock);
            return repository.save(p);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public List<Product> ListActiveProduct(boolean status,String validation){
        if(setting.validateToken(validation)){
            return repository.findByStatus(status);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public List<Product> ListActiveProductAndCategory(ProductsCategory category,boolean status,String validation){
        if(setting.validateToken(validation)){
            return repository.findByCategoryAndStatus(category,status);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public Product GetForName(String name,String validation){
        if(setting.validateToken(validation)){
            return repository.findByName(name)
                    .orElseThrow(()-> new RuntimeException("The product with the name "+name+" is not found"));
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }


    }
}
