package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dao.CategoryDao;
import com.poly.entity.Category;

@Service

public interface CategoryService {

	List<Category> findAll();

}
