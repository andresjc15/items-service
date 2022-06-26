package com.ajcp.item.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer port;
    private Date createAt;

}
