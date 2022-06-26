package com.ajcp.item.api;

import com.ajcp.item.model.entity.Item;
import com.ajcp.item.model.service.ItemService;
import io.swagger.annotations.Example;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${path.items}")
@AllArgsConstructor
public class ItemController {

    //@Qualifier("feignService")
    @Qualifier("restTemplateService")
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> getItems() {
        return new ResponseEntity<List<Item>>(itemService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}/quantity/{quantity}")
    public ResponseEntity<?> getItem(@PathVariable Long id, @PathVariable Integer quantity) {
        return new ResponseEntity<Item>(itemService.findById(id, quantity), HttpStatus.OK);
    }
}
