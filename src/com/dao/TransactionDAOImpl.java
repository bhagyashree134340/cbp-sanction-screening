package com.dao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.lang.Double;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.List;
//import java.text.DecimalFormat;

import com.connection.MyConnection;
import com.pojo.Transaction;

public class TransactionDAOImpl implements TransactionDAO{
	
	ArrayList<Transaction> transactions = new ArrayList<>();
	ArrayList<Transaction> validTransactions = new ArrayList<>();
	ArrayList<Transaction> invalidTransactions = new ArrayList<>();
	static TransactionDAOImpl t = null;	
	
		//get connection
		//get instance of PS
		//set values to PS
		//execute Query
		//return result
	
	public static TransactionDAOImpl getInstance() {
		if (t == null) {
			t = new TransactionDAOImpl();
		}
		return t;
	}
	
	public ArrayList<Transaction> gettransaction() {
		return this.transactions;
	}
	
	public ArrayList<Transaction> getValidTransaction() {
		return this.validTransactions;
	}

	public ArrayList<Transaction> getinvalidTransaction() {
		return this.invalidTransactions;
	}
	
	@Override
	public void readTransaction(String lines[]) 
	{
		int flag=0;
		int payerNameSize=0;
		int payeeNameSize=0;
		File file = new File("C:\\Users\\admin\\Desktop\\Sanction Screening\\SampleFileTTS.txt");
		  
		BufferedReader br = null;
	    try
	    {
			br = new BufferedReader(new FileReader(file));
			  String st;
			  try {
				try {
					while ((st = br.readLine()) != null) 
					{						  
						    //System.out.println(st);
							String transactionRef = st.substring(0, 12);
							//System.out.println("Id :"+transactionRef);
							String valueDate = st.substring(12, 20);
							//System.out.println("Date :"+valueDate);
							int firstIndex = st.indexOf(' '), midIndex;
							midIndex = st.substring(firstIndex + 1).indexOf(' ') + firstIndex + 1;
							
							String payerName = st.substring(20, firstIndex);
							payerNameSize=firstIndex-20;
							 //System.out.println("Payer name :"+payerName);
							 
							String payerAccount = st.substring(firstIndex + 1, firstIndex + 13);
							 //System.out.println("Payer acc :"+payerAccount);
							 
							String payeeName = st.substring(firstIndex + 13, midIndex);
							payeeNameSize=midIndex-(firstIndex+13);
							 //System.out.println("Payee name :"+payeeName);
							 
							String payeeAccount = st.substring(midIndex + 1, midIndex + 13);
							 //System.out.println("Payee acc :"+payeeAccount);
							 
							double amount = Double.parseDouble(st.substring(midIndex+13));
							//System.out.println("amount :"+amount);
							transactions.add(new Transaction(transactionRef, valueDate, payerName, payerAccount, payeeName,
							payeeAccount, amount, "Uploading"));
							if (uniqueReferenceId(transactionRef)) {				
								if (presentDate(valueDate)) {
									if(alphaNumeric(payerName) && payerNameSize<=35 && alphaNumeric(payeeName) &&
											payeeNameSize<=35 && alphaNumeric(payeeAccount) && alphaNumeric(payeeAccount)) {
										if(validAmount(amount)) {
											validTransactions.add(new Transaction(transactionRef, valueDate, payerName, payerAccount, payeeName,
											payeeAccount, amount, "Valid Pass"));
											flag = 1;
										}
									}
								}
							}
							if (flag == 0) {
								invalidTransactions.add(
										new Transaction(transactionRef, valueDate, payerName, payerAccount, payeeName, payeeAccount, amount, "Valid Fail"));
							}
							flag = 0;
						}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			}catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   } catch (FileNotFoundException e) {
		  // TODO Auto-generated catch block
		 e.printStackTrace();
	   }					
	}
	
	public boolean uniqueReferenceId(String transactionRef) 
	{
		for (int i = 0; i < validTransactions.size(); i++) {
			if (validTransactions.get(i).getTransactionRef().equals(transactionRef)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean presentDate(String date) 
	{
		//System.out.println(date);
		int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calobj = Calendar.getInstance();
		String s = df.format(calobj.getTime());
		int day = Integer.parseInt(date.substring(0, 2));
		int month = Integer.parseInt(date.substring(3, 5));
		int year = Integer.parseInt(date.substring(6));
		// System.out.println(day+"\t"+month+"\t"+year);
		// System.out.println(Integer.parseInt(s.substring(4)));
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
		splitter[0].length();   // Before Decimal Count
		//int decimalLength = splitter[1].length();  // After Decimal Count

		if (splitter[0].length()<=10 && amount>0)
		{
		   // valid
	        
	        amount= Math.round(amount * 100.0) / 100.0;
			return true;
		}
		else
		{
		   // invalid
			return false;
		}
			
	}
	@Override
	public void moveTransactionsToDB()
	{
		int moved=0;
		
		Connection con=new MyConnection().getConnection();
		try {
			for(int i=0;i<transactions.size();i++)
			{
				PreparedStatement ps=con.prepareStatement("INSERT INTO fileUploaded(transactionRef,valueDate,payerName,payerAccount,payeeName,payeeAccount,amount,status) values(?,?)");
				ps.setString(1,  transactions.get(i).getTransactionRef());
				ps.setString(1,  transactions.get(i).getValueDate());
				ps.setString(1,  transactions.get(i).getPayerName());
				ps.setString(1,  transactions.get(i).getPayerAccount());
				ps.setString(1,  transactions.get(i).getPayeeName());
				ps.setString(1,  transactions.get(i).getPayeeAccount());
				ps.setDouble(1,  transactions.get(i).getAmount());
				ps.setString(1,  transactions.get(i).getStatus());
			
				moved=ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void displayAllTransactions() {
		// TODO Auto-generated method stub
		int transactionsSize=transactions.size();
		
		for(int i = 0; i < transactionsSize; i++)
		{
			System.out.println(transactions.get(i).toString());
			System.out.println("\n");
		}
		
	}

	@Override
	public void displayValidTransactions() {
		// TODO Auto-generated method stub
		int validTransactionsSize=validTransactions.size();
		
		for(int i = 0; i < validTransactionsSize; i++)
		{
			System.out.println(validTransactions.get(i).toString());
			System.out.println("\n");
		}
	}

	@Override
	public void displayInvalidTransactions() {
		int invalidTransactionsSize=invalidTransactions.size();
		
		for(int i = 0; i < invalidTransactionsSize; i++)
		{
			System.out.println(invalidTransactions.get(i).toString());
			System.out.println("\n");
		}
	}

	
}
