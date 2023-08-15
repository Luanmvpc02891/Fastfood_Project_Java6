package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.InvoiceItem;

public interface InvoiceItemDAO extends JpaRepository<InvoiceItem, Integer> {

}