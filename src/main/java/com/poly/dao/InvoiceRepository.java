package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

}
