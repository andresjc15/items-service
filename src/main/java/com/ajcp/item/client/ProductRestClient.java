package com.ajcp.item.client;

import com.ajcp.item.model.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "products-service")
public interface ProductRestClient {

    @GetMapping("/api/products")
    public List<Product> getProducts();

    @GetMapping("/api/products/{id}")
    public Product detail(@PathVariable Long id);

}
