package com.poly.entity;

import java.util.List;

import lombok.Data;

@Data
public class ItemDetailResponse {
    private Item item;
    private List<Item> relatedProducts;
}
