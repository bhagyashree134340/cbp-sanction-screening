package com.dao;

import java.util.List;

import com.pojo.Transaction;

public interface TransactionDAO {
	
	//public int fileUpload(Transaction transaction);

	public String readTransaction();
	public int moveTransactionsToDB();
	public List<Transaction> displayAllTransactionsFromDB();
	public List<Transaction> displayValidTransactions();
	public List<Transaction> displayInvalidTransactions();
	public List<Transaction> screenfailTransactions();
	public List<Transaction> screenPassTransactions();
	public List<Transaction> screeningStatus();
	
}
