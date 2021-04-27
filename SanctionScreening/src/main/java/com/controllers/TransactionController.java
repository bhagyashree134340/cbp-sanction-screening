package com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dao.TransactionDAO;
import com.pojo.Transaction;

@RestController
public class TransactionController {
		@Autowired
		TransactionDAO dao;
		
		@RequestMapping(value = "/data", method = RequestMethod.GET)
		public String message() {
			return "welcome to REST";
		}

		//dao.moveTransactionsToDB();
		
		@CrossOrigin(origins = "https://localhost:4200")
		@RequestMapping(value = "/uploadfile", method = RequestMethod.GET)
		public void uploadFile() {
			System.out.println("Calling the readtrans() in IMPL");
			String res = dao.readTransaction();
			System.out.println(res);
//			return res;

		}
		

		@CrossOrigin(origins = "http://localhost:4200")
		@RequestMapping(value = "/transactions", method = RequestMethod.GET)
		public List<Transaction> showTransactions() {
			System.out.println("calling displayTransactions() in IMPL")
			return dao.displayAllTransactionsFromDB();
		}
		
		@RequestMapping(value = "/validTransactions", method = RequestMethod.GET)
		public List<Transaction> showValidTransactions() {
			return dao.displayValidTransactions();
		}
		
		@RequestMapping(value = "/inValidTransactions", method = RequestMethod.GET)
		public List<Transaction> showInvalidTransactions() {
			return dao.displayInvalidTransactions();
		}
		
		@RequestMapping(value = "/screenFail", method = RequestMethod.GET)
		public List<Transaction> screenFail() {
			return dao.screenfailTransactions();
		}
		
		@RequestMapping(value = "/screenpass", method = RequestMethod.GET)
		public List<Transaction> screenPass() {
			return dao.screenPassTransactions();
		}
		
		@RequestMapping(value = "/screenStatus", method = RequestMethod.GET)
		public List<Transaction> screenStatus() {
			return dao.screeningStatus();
		}
	

}
