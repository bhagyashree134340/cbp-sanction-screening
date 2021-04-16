package com.pojo;

//import java.util.Date;

public class Transaction {
	
	private String transactionRef;
	private String valueDate;
	private String payerName;
	private String payerAccount;
	private String payeeName;
	private String payeeAccount;
	private double amount;
	
	private String status ="upload";
		
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transaction(String transactionRef, String valueDate, String payerName, String payerAccount, String payeeName,
			String payeeAccount, double amount, String status) {
		super();
		this.transactionRef = transactionRef;
		this.valueDate = valueDate;
		this.payerName = payerName;
		this.payerAccount = payerAccount;
		this.payeeName = payeeName;
		this.payeeAccount = payeeAccount;
		this.amount = amount;
		this.status = status;
	}


	public String getTransactionRef() {
		return transactionRef;
	}


	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}


	public String getValueDate() {
		return valueDate;
	}


	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}


	public String getPayerName() {
		return payerName;
	}


	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}


	public String getPayerAccount() {
		return payerAccount;
	}


	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}


	public String getPayeeName() {
		return payeeName;
	}


	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}


	public String getPayeeAccount() {
		return payeeAccount;
	}


	public void setPayeeAccount(String payeeAccount) {
		this.payeeAccount = payeeAccount;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Transaction [transactionRef=" + transactionRef + ", valueDate=" + valueDate + ", payerName=" + payerName
				+ ", payerAccount=" + payerAccount + ", payeeName=" + payeeName + ", payeeAccount=" + payeeAccount
				+ ", amount=" + amount + "]";
	}


	

}
