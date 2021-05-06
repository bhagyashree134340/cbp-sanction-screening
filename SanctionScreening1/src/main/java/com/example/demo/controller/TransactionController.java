package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Utility.FileUploadHelper;
import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	TransactionService ts;
	
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public String message() {
		return "welcome to REST";
	}
	
	@PostMapping("/readTransactions")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
		
		return ts.FileUpload(file);
	}
	
//	@RequestMapping(value = "/readTransactions", method = RequestMethod.GET)
//	public void readTransactions() {
//		ts.readTransaction();
//		System.out.println("Transactions added.");
//	}
	
	@RequestMapping(value = "/addTransactions", method = RequestMethod.GET)
	public void addTransactions() {
		ts.addTransactions();
		System.out.println("Transactions added.");
	}
	
	@RequestMapping(value = "/displayAll", method = RequestMethod.GET)
	public List<Transaction> displayAll() {
		return ts.displayAllTransactionsFromDB();
	}
	
	@RequestMapping(value = "/displayStatus/{status}", method = RequestMethod.GET)
	public List<Transaction> displayStatus(@PathVariable(name = "status") String status) {
		return ts.displayStatus(status);
	}
	
	@RequestMapping(value = "/checkSanctionList", method = RequestMethod.GET)
	public List<Transaction> checkSanctionList() {
		return ts.screening();
	}
	
	
}
