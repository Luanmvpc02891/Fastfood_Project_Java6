package com.poly.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.Dto.TotalAmountRequest;
import com.poly.dao.AccountDao;
import com.poly.dao.InvoiceItemDAO;
import com.poly.dao.InvoiceRepository;
import com.poly.dao.OrderItemDao;
import com.poly.dao.OrdersDao;
import com.poly.entity.Account;
import com.poly.entity.AddressCity;
import com.poly.entity.AddressDistrict;
import com.poly.entity.Invoice;
import com.poly.entity.InvoiceItem;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;
import com.poly.service.AddressCityService;
import com.poly.service.AddressCityServiceImp;
import com.poly.service.AddressDistrictService;
import com.poly.service.AddressDistrictServiceImp;
import com.poly.service.OrderItemService;
import com.poly.service.OrderService;

import lombok.Data;

@CrossOrigin("*")
@RestController
@RequestMapping("/client/checkout/")
public class CheckoutRestController {

	@Autowired
	AddressCityServiceImp addressCityServiceImp;

	@Autowired
	AddressDistrictServiceImp addressDistrictServiceImp;

	@Autowired
	AccountDao udao;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	OrdersDao orderDao;

	@Autowired
	AccountDao accountDao;

	@Autowired
	InvoiceItemDAO invoiceItemDAO;

	@Autowired
	private OrderItemDao orderItemDao;

	@GetMapping("city")
	public List<AddressCity> getCity(Model model) {
		List<AddressCity> addressCity = addressCityServiceImp.findAll();
		System.out.println(addressCity);
		return addressCity;
	}

	@GetMapping("district")
	public List<AddressDistrict> getDistrict(Model model) {
		List<AddressDistrict> addressDistrict = addressDistrictServiceImp.findAll();
		return addressDistrict;
	}

	@GetMapping("findUser/{accountId}")
	public Account getUser(Model model, @PathVariable("accountId") Integer accountId) {
		Account account = udao.findByAccountId(1);
		return account;
	}

	@PutMapping("updateUser/{accountId}")
	public Account update(@PathVariable Integer accountId, @RequestBody Account updatedUser) {
		Account account = udao.findByAccountId(accountId);

		account.setAddressCity(updatedUser.getAddressCity());
		account.setAddressDistrict(updatedUser.getAddressDistrict());

		return udao.save(account);
	}

	@PostMapping("check")
	public Invoice getInvoice(@PathVariable Integer accountId, @RequestBody Invoice invoices) {
		Invoice invoi = new Invoice();
		Account account = udao.findByAccountId(accountId);

		invoi.setAccount(account);
		invoi.setInvoiceDate(new Date());
		invoi.setOrder(orderDao.findByAccount(account));
		invoi.setPaymentMethod(invoices.getPaymentMethod());
		invoi.setTotalAmount(invoices.getTotalAmount());
		invoi.setShipfee(invoices.getShipfee());
		invoi.setStatus(true);
		invoiceRepository.save(invoi);
		System.out.println("Thành công");
		return invoi;
	}

	@PostMapping("add-invoice/methodCOD")
	public ResponseEntity<String> addInvoice(@RequestBody Map<String, Object> checkoutData) {
		// Tìm kiếm thông tin người dùng
		// double shipFee = shipfee;
		int totalAmount = (Integer) (checkoutData.get("totalAmount"));

		Account account = udao.findByAccountId(1);
		Order order = orderDao.findByAccount(account);
		// Lấy thông tin đơn hàng và tạo hóa đơn mới
		Invoice invoice = new Invoice();
		invoice.setOrder(order);
		// Lấy thông tin tài khoản và cập nhật vào hóa đơn
		invoice.setAccount(account);
		// Cập nhật thông tin khác cho hóa đơn
		invoice.setInvoiceDate(new Date());
		// invoice.setShipfee(shipFee);
		invoice.setTotalAmount(totalAmount);
		invoice.setPaymentMethod(1);
		// invoice.setStatus(true);

		// Lưu hóa đơn vào cơ sở dữ liệu
		invoiceRepository.save(invoice);

		for (OrderItem od : orderItemDao.findByOrder(order)) {
			InvoiceItem ivoiceItem = new InvoiceItem();
			ivoiceItem.setInvoice(invoice);
			ivoiceItem.setItem(od.getItem());
			ivoiceItem.setQuantity(od.getQuantity());
			ivoiceItem.setUnitPrice(od.getItem().getPrice());
			invoiceItemDAO.save(ivoiceItem);
			orderItemDao.delete(od);
		}

		return ResponseEntity.ok().body("{\"message\": \"Hóa đơn đã được tạo thành công!\"}");
	}

	@PostMapping("add-invoice/methodBanking")
	public ResponseEntity<String> addInvoiceBanking(@RequestBody Map<String, Object> checkoutData) {
		// Tìm kiếm thông tin người dùng
		// Double shipFee = (Double) checkoutData.get("shipFee2");
		int totalAmount = (Integer) (checkoutData.get("totalAmount"));

		Account account = udao.findByAccountId(6);
		Order order = orderDao.findByAccount(account);
		// Lấy thông tin đơn hàng và tạo hóa đơn mới
		Invoice invoice = new Invoice();
		invoice.setOrder(order);
		// Lấy thông tin tài khoản và cập nhật vào hóa đơn
		invoice.setAccount(account);
		// Cập nhật thông tin khác cho hóa đơn
		invoice.setInvoiceDate(new Date());
		// invoice.setShipfee(shipFee);
		invoice.setTotalAmount(totalAmount);
		invoice.setPaymentMethod(2);
		// invoice.setStatus(true);

		// Lưu hóa đơn vào cơ sở dữ liệu
		invoiceRepository.save(invoice);

		InvoiceItem ivoiceItem = new InvoiceItem();
		for (OrderItem od : orderItemDao.findByOrder(order)) {
			ivoiceItem.setInvoice(invoice);
			ivoiceItem.setItem(od.getItem());
			ivoiceItem.setQuantity(od.getQuantity());
			ivoiceItem.setUnitPrice(od.getItem().getPrice());
			invoiceItemDAO.save(ivoiceItem);
			orderItemDao.delete(od);
		}

		return ResponseEntity.ok().body("{\"message\": \"Hóa đơn đã được tạo thành công!\"}");
	}

}