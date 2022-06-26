package com.ajcp.item.service;

import com.ajcp.item.model.entity.Item;
import com.ajcp.item.model.entity.Product;
import com.ajcp.item.model.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("restTemplateService")
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final RestTemplate restClient;

    @Override
    public List<Item> findAll() {
        List<Product> products = Arrays.asList(
                //restClient.getForObject("http://localhost:8000/api/products", Product[].class)
                restClient.getForObject("products-service", Product[].class)
        );
        return products.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        Map<String, Object> pathVariables = new HashMap<String, Object>();
        pathVariables.put("id", id.toString());
        //Product product = restClient.getForObject("http://localhost:8000/api/products", Product.class, pathVariables);
        Product product = restClient.getForObject("http://products-service/api/products", Product.class, pathVariables);
        return new Item(product, quantity);
    }
}
