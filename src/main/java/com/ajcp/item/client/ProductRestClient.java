package com.ajcp.item.client;

import com.ajcp.library.common.model.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "products-service")
public interface ProductRestClient {

    @GetMapping("/api/products")
    public List<Product> getProducts();

    @GetMapping("/api/products/{id}")
    public Product detail(@PathVariable Long id);

    @PostMapping("/api/products")
    public Product register(@RequestBody Product product);

    @PutMapping("/api/products/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product);

    @DeleteMapping("/api/products/{id}")
    public Product delete(@PathVariable Long id);

}
