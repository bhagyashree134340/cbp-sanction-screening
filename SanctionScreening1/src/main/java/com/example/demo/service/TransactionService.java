package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.SanctionList;
import com.example.demo.model.Transaction;
import com.example.demo.repository.SanctionListRepository;
import com.example.demo.repository.TransactionRepositoy;

@Service
public class TransactionService {

	@Autowired
	TransactionRepositoy transactionRepo;
	
	@Autowired 
	SanctionListRepository sanctionRepo;
    
	public void addTransactions()
	{
		long millis = System.currentTimeMillis();
		java.util.Date d = new java.sql.Date(millis);
		Transaction t1 = new Transaction("1", d, "elena", "123", "damon", "345", 23.5, "valid");
		Transaction t2 = new Transaction("2", d, "Manasi", "1234", "Bhandari", "6789", 2346, "invalid");
		Transaction t3 = new Transaction("3", d, "Manasi", "1234", "Noel", "6789", 2346, "valid");
		transactionRepo.save(t1);
		transactionRepo.save(t2);
		transactionRepo.save(t3);
	}

	
	/*
	public void readTransaction(String[] lines) {
		// TODO Auto-generated method stub
		int flag=0;
		int payerNameSize=0;
		int payeeNameSize=0;
		
		File file = new File("SampleFileTTS.txt");
		  
		BufferedReader br = null;
	    try
	    {
			br = new BufferedReader(new FileReader(file));
			  String st;
			  try {
				try {
					while ((st = br.readLine()) != null) 
					{						  
						   
							String transactionRef = st.substring(0, 12);
							
							
							java.util.Date valueDate = null;
							try {
								valueDate = new SimpleDateFormat("dd/MM/yyyy").parse(st.substring(12, 20));
							} catch (ParseException e) {
								
								e.printStackTrace();
							}
					
							int firstIndex = st.indexOf(' '), midIndex;
							midIndex = st.substring(firstIndex + 1).indexOf(' ') + firstIndex + 1;
							
							String payerName = st.substring(20, firstIndex);
							payerNameSize=firstIndex-20;
							
							 
							String payerAccount = st.substring(firstIndex + 1, firstIndex + 13);
							
							 
							String payeeName = st.substring(firstIndex + 13, midIndex);
							payeeNameSize=midIndex-(firstIndex+13);
							 
							 
							String payeeAccount = st.substring(midIndex + 1, midIndex + 13);
							
							 
							double amount = Double.parseDouble(st.substring(midIndex+13));
						

							if (uniqueReferenceId(transactionRef)) {	
								String s1=dateFormat.format(valueDate);
								if (presentDate(s1)) {
									if(alphaNumeric(payerName) && payerNameSize<=35 && alphaNumeric(payeeName) &&
											payeeNameSize<=35 && alphaNumeric(payeeAccount) && alphaNumeric(payeeAccount)) {
										if(validAmount(amount)) {
											transactions.add(new Transaction(transactionRef, valueDate, payerName, payerAccount, payeeName,
													payeeAccount, amount, "Valid Pass"));			
											flag = 1;
										}
									}
								}
							}
							if (flag == 0) {
								transactions.add(new Transaction(transactionRef, valueDate, payerName, payerAccount, payeeName,
										payeeAccount, amount, "Valid Fail"));
							}
							flag = 0;
						}
			} catch (IOException e) {
				
				e.printStackTrace();
			}		
			}catch (NumberFormatException e) {
			
				e.printStackTrace();
			}
	   } catch (FileNotFoundException e) {
		  
		 e.printStackTrace();
	   }			

	}
	public boolean uniqueReferenceId(String transactionRef) 
	{
		for (int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).getTransactionRef().equals(transactionRef)) {
				return false;
			}
		}
		return true;
	}
	
	*/
	public boolean presentDate(String date)
	{
	
		int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calobj = Calendar.getInstance();
		String s = df.format(calobj.getTime());
		int day = Integer.parseInt(date.substring(0, 2));
		int month = Integer.parseInt(date.substring(3, 5));
		int year = Integer.parseInt(date.substring(6));
		
		if (month <= 12 && year == Integer.parseInt(s.substring(4))) {
			if (year % 4 == 0) {
				if (year % 100 == 0) {
					if (year % 400 == 0)
						daysInMonth[1] = 29;
				} else
					daysInMonth[1] = 29;
			}

			if (day <= daysInMonth[month - 1])
				return true;
		}
		return false;

	}
	public boolean alphaNumeric(String str)
	{
		boolean isAlphaNumeric = (str != null) && str.chars().allMatch(Character::isLetterOrDigit);
		
		return isAlphaNumeric;                
	}
	
	public boolean validAmount(double amount)
	{
		String[] splitter = Double.toString(amount).split("\\.");
		splitter[0].length();  
		
		if (splitter[0].length()<=10 && amount>0)
		{
		           
	        amount= Math.round(amount * 100.0) / 100.0;
			return true;
		}
		else
		{
		   
			return false;
		}
	}
	
	

	
	public List<Transaction> displayAllTransactionsFromDB() {
		
		return transactionRepo.findAll();
	}

	
	public List<Transaction> displayStatus(String status)
	{
		return transactionRepo.displayStatus(status);
	}
	
	
	public List<Transaction> screening()
	{
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		for(Transaction t :transactionRepo.findAll())
		{
			String payer = t.getPayerName();
			String payee = t.getPayeeName();
			for(SanctionList s: sanctionRepo.findAll())
			{
				if(payee.equalsIgnoreCase(s.getName()) || payer.equalsIgnoreCase(s.getName()))
				{
					
					
					transactions.add(saveWithStatus(t,"screen-failed"));
				}
				else
				{
					if(t.getStatus().equalsIgnoreCase("valid"))
					{
						saveWithStatus(t,"screen-pass");
					}
				}
			}
		}
			
		return transactions;
	}
	
	
	public Transaction saveWithStatus(Transaction t,String status)
	{
		Transaction temp = transactionRepo.getOne(t.getTransactionRef());
		temp.setTransactionRef(t.getTransactionRef());
		temp.setValueDate(t.getValueDate());
		temp.setPayerName(t.getPayerName());
		temp.setPayerAccount(t.getPayerAccount());
		temp.setPayeeName(t.getPayeeName());
		temp.setPayeeAccount(t.getPayeeAccount());
		temp.setAmount(t.getAmount());
		temp.setStatus(status);
		
		transactionRepo.save(temp);
		return temp;
	}
	

}
