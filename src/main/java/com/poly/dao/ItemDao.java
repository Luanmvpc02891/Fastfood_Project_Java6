package com.poly.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Item;

public interface ItemDao extends JpaRepository<Item, Integer> {
    List<Item> findByCategoryCategoryId(int categoryId);

    List<Item> findByNameContainingIgnoreCase(String keyword);

    List<Item> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Item> findByNameContainingIgnoreCaseAndPriceBetween(String keyword, BigDecimal minPrice, BigDecimal maxPrice);

    List<Item> findByCategory_CategoryIdAndItemIdNot(Integer categoryId, Integer currentItemId);

    Page<Item> findByNameContaining(String keyword, PageRequest pageRequest);

    Page<Item> findByCategoryCategoryId(Integer categoryId, PageRequest pageRequest);

}
