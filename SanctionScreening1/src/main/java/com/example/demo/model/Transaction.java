package com.example.demo.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {
	
	@Id
	@Column(name = "TRANSACTION_REF")
	private String transactionRef;
	
	@Column(name = "VALUE_DATE")
	private Date valueDate;
	
	@Column(name = "PAYER_NAME")
	private String payerName;
	
	@Column(name = "PAYER_ACCOUNT")
	private String payerAccount;
	
	@Column(name = "PAYEE_NAME")
	private String payeeName;
	
	@Column(name = "PAYEE_ACCOUNT")
	private String payeeAccount;
	
	@Column(name = "AMOUNT")
	private double amount;
	
	@Column(name = "STATUS")
	private String status;
		
	
	
	public Transaction()
	{
		
	}

	public Transaction(String transactionRef, Date d, String payerName, String payerAccount, String payeeName,
			String payeeAccount, double amount, String status) {
		super();
		this.transactionRef = transactionRef;
		this.valueDate = d;
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


	public Date getValueDate() {
		return valueDate;
	}


	public void setValueDate(Date valueDate) {
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
				+ ", amount=" + amount + ", status=" + status + "]";
	}


	

}
