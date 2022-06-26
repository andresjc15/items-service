package com.ajcp.item.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Product product;
    private Integer quantity;

    public BigDecimal getTotal() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }

}
