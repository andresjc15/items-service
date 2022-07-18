package com.ajcp.item.service;

import com.ajcp.item.model.entity.Item;
import com.ajcp.item.model.service.ItemService;
import com.ajcp.library.common.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("restTemplateService")
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final RestTemplate restClient;

    @Override
    public List<Item> findAll() {
        List<Product> products = Arrays.asList(
                //restClient.getForObject("http://localhost:8000/api/products", Product[].class)
                restClient.getForObject("http://products-service/api/products", Product[].class)
        );
        return products.stream().map(p -> new Item(p, 1)).map(item -> {
            log.info("[ITEM]: " + item);
            return item;
        }) .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        Map<String, Object> pathVariables = new HashMap<String, Object>();
        pathVariables.put("id", id.toString());
        //Product product = restClient.getForObject("http://localhost:8000/api/products", Product.class, pathVariables);
        Product product = restClient.getForObject("http://products-service/api/products", Product.class, pathVariables);
        return new Item(product, quantity);
    }

    @Override
    public Product save(Product product) {
        HttpEntity<Product> body = new HttpEntity<Product>(product);
        log.info("[EXECUTING TRANSACTION REST CLIENT]");
        ResponseEntity<Product> response = restClient.exchange("http://products-service/api/products", HttpMethod.POST, body, Product.class);
        Product productResponse = response.getBody();
        log.info("[RESPONSE]: " + response);
        return productResponse;
    }

    @Override
    public Product update(Long id, Product product) {
        Map<String, Object> pathVariables = new HashMap<String, Object>();
        pathVariables.put("id", id.toString());

        log.info("[EXECUTING TRANSACTION REST CLIENT]");
        HttpEntity<Product> body = new HttpEntity<Product>(product);
        ResponseEntity<Product> response = restClient.exchange("http://products-service/api/products/{id}",
                HttpMethod.PUT, body, Product.class, pathVariables);
        Product productResponse = response.getBody();
        log.info("[RESPONSE]: " + response);
        return productResponse;
    }

    @Override
    public void delete(Long id) {
        Map<String, Object> pathVariables = new HashMap<String, Object>();
        pathVariables.put("id", id.toString());

        restClient.delete("http://products-service/api/products/{id}", pathVariables);
    }
}
