package com.ajcp.item.model.service;

import com.ajcp.item.model.entity.Item;

import java.util.List;

public interface ItemService {

    public List<Item> findAll();
    public Item findById(Long id, Integer quantity);

}
