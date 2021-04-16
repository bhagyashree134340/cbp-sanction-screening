package com.dao;

//import java.util.List;

//import com.pojo.Transaction;

public interface TransactionDAO {
	
	//public int fileUpload(Transaction transaction);
	public void readTransaction(String[] lines);
	public void moveTransactionsToDB();
	public void displayAllTransactions();
	public void displayValidTransactions();
	public void displayInvalidTransactions();
	
}
