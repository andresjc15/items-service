package com.ajcp.item.service;

import com.ajcp.item.client.ProductRestClient;
import com.ajcp.item.model.entity.Item;
import com.ajcp.item.model.entity.Product;
import com.ajcp.item.model.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
}
