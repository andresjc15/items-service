package com.ajcp.item.model.service;

import com.ajcp.item.model.entity.Item;
import com.ajcp.library.common.model.entity.Product;

import java.util.List;

public interface ItemService {

    public List<Item> findAll();
    public Item findById(Long id, Integer quantity);
    public Product save(Product product);
    public Product update(Long id, Product product);
    public void delete(Long id);

}
