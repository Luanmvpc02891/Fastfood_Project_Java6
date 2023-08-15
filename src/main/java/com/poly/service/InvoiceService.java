package com.poly.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dao.InvoiceRepository;
import com.poly.entity.Invoice;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

}
