package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.entity.Category;
import com.poly.entity.Item;

public interface CategoryDao extends JpaRepository<Category, Integer> {

    List<Category> findAll();

    Category findById(int categoryId);
}
