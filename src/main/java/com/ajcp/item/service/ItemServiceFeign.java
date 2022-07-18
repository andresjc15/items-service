package com.ajcp.item.service;

import com.ajcp.item.client.ProductRestClient;
import com.ajcp.item.model.entity.Item;
import com.ajcp.item.model.service.ItemService;
import com.ajcp.library.common.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("feignService")
@Primary
@AllArgsConstructor
public class ItemServiceFeign implements ItemService {

    private final ProductRestClient productRestClient;

    @Override
    public List<Item> findAll() {
        return productRestClient.getProducts().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        return new Item(productRestClient.detail(id), quantity);
    }

    @Override
    public Product save(Product product) {
        return productRestClient.register(product);
    }

    @Override
    public Product update(Long id, Product product) {
        return productRestClient.update(id, product);
    }

    @Override
    public void delete(Long id) { productRestClient.delete(id); }

}
