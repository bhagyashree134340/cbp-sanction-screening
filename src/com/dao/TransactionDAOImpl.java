package com.dao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
//import java.lang.Double;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
//import java.util.List;
//import java.text.DecimalFormat;
import java.util.Iterator;

//import com.connection.MyConnection;
import com.pojo.Transaction;

public class TransactionDAOImpl implements TransactionDAO{
	
	ArrayList<Transaction> transactions = new ArrayList<>();

	static TransactionDAOImpl t = null;	
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
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
							
							java.util.Date valueDate = null;
							try {
								valueDate = new SimpleDateFormat("dd/MM/yyyy").parse(st.substring(12, 20));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
		for (int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).getTransactionRef().equals(transactionRef)) {
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
		
		try {
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/inputFiles","root","mysql");
			PreparedStatement ps=con.prepareStatement("INSERT INTO fileUploaded(transactionRef,valueDate,payerName,payerAccount,"
					+ "payeeName,payeeAccount,amount,status) values(?,?,?,?,?,?,?,?)");
											
			for(Iterator<Transaction> t=transactions.iterator();t.hasNext();)
			{
				Transaction t1=(Transaction) t.next();
				ps.setString(1,  t1.getTransactionRef());
				ps.setDate(2,    (Date)t1.getValueDate());
				ps.setString(3,  t1.getPayerName());
				ps.setString(4,  t1.getPayerAccount());
				ps.setString(5,  t1.getPayeeName());
				ps.setString(6,  t1.getPayeeAccount());
				ps.setDouble(7,  t1.getAmount());
				ps.setString(8,  t1.getStatus());
			
				ps.addBatch();
			}
			int[] updateCounts=ps.executeBatch();
			System.out.println(Arrays.toString(updateCounts));
			
			
			con.commit();
			con.setAutoCommit(true);
			
		} catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void displayAllTransactionsFromDB() {
		// TODO Auto-generated method stub
/*		int transactionsSize=transactions.size();
		
		for(int i = 0; i < transactionsSize; i++)
		{
			System.out.println(transactions.get(i).toString());
			System.out.println("\n");
		}*/
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/inputFiles","root","mysql");
	
			Statement stmt=con.createStatement();
			ResultSet rs= stmt.executeQuery("Select * from fileUploaded");
			while(rs.next()) {
				String id=rs.getString("transactionRef");
				java.util.Date dt=(java.util.Date) rs.getDate("valueDate");
				String payeeName=rs.getString("payeeName");
				String payeeAccount=rs.getString("payeeAccount");
				String payerName=rs.getString("payerName");
				String payerAccount=rs.getString("payerAccount");
				Double amount=rs.getDouble("amount");
				String status=rs.getString("status");
				
				System.out.println("id "+id);
				System.out.println("Date "+dt);
				System.out.println("payeeName "+payeeName);
				System.out.println("payeeAccount "+payeeAccount);
				System.out.println("payerName "+ payerName);
				System.out.println("payerAccount "+payerAccount);
				System.out.println("amount "+ amount);
				System.out.println("status "+ status);
			}
			con.commit();
			con.setAutoCommit(true);
			
		} catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void displayValidTransactionsFromDB() {
		// TODO Auto-generated method stub
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/inputFiles","root","mysql");
	
			Statement stmt=con.createStatement();
			ResultSet rs= stmt.executeQuery("Select * from fileUploaded");
			while(rs.next()) {
				String id=rs.getString("transactionRef");
				java.util.Date dt=(java.util.Date) rs.getDate("valueDate");
				String payeeName=rs.getString("payeeName");
				String payeeAccount=rs.getString("payeeAccount");
				String payerName=rs.getString("payerName");
				String payerAccount=rs.getString("payerAccount");
				Double amount=rs.getDouble("amount");
				String status=rs.getString("status");
				if(status.equals("Valid Pass"))
				{
					System.out.println("id "+id);
					System.out.println("Date "+dt);
					System.out.println("payeeName "+payeeName);
					System.out.println("payeeAccount "+payeeAccount);
					System.out.println("payerName "+ payerName);
					System.out.println("payerAccount "+payerAccount);
					System.out.println("amount "+ amount);
					System.out.println("status "+ status);
				}
			}
			con.commit();
			con.setAutoCommit(true);
			
		} catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void displayInvalidTransactionsFromDB() {
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/inputFiles","root","mysql");
	
			Statement stmt=con.createStatement();
			ResultSet rs= stmt.executeQuery("Select * from fileUploaded");
			while(rs.next()) {
				String id=rs.getString("transactionRef");
				java.util.Date dt=(java.util.Date) rs.getDate("valueDate");
				String payeeName=rs.getString("payeeName");
				String payeeAccount=rs.getString("payeeAccount");
				String payerName=rs.getString("payerName");
				String payerAccount=rs.getString("payerAccount");
				Double amount=rs.getDouble("amount");
				String status=rs.getString("status");
				if(status.equals("Valid Fail"))
				{
					System.out.println("id "+id);
					System.out.println("Date "+dt);
					System.out.println("payeeName "+payeeName);
					System.out.println("payeeAccount "+payeeAccount);
					System.out.println("payerName "+ payerName);
					System.out.println("payerAccount "+payerAccount);
					System.out.println("amount "+ amount);
					System.out.println("status "+ status);
				}
			}
			con.commit();
			con.setAutoCommit(true);
			
		} catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
