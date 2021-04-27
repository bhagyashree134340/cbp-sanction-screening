package com.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pojo.Transaction;

@Repository(value = "dao1")
public class TransactionDAOJdbcTemplate implements TransactionDAO {

	@Autowired
	JdbcTemplate template;

	ArrayList<Transaction> transactions = new ArrayList<>();
	ArrayList<Transaction> validtransactions = new ArrayList<>();
	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
	DateFormat inputFormat = new SimpleDateFormat("ddmmyyyy");

	public TransactionDAOJdbcTemplate() {
		
//		long millis = System.currentTimeMillis();
//		java.util.Date d = new java.sql.Date(millis);
//		
//		transactions.add(new Transaction("1", d, "Elena", "1234", "Damon", "6789", 2346, "valid"));
//		transactions.add(new Transaction("2", d, "Manasi", "1234", "Bhandari", "6789", 2346, "invalid"));
//		transactions.add(new Transaction("3", d, "Manasi", "1234", "Noel", "6789", 2346, "valid"));
	}

	

	public ArrayList<Transaction> gettransaction() {
		return this.transactions;
	}

	@Override
	public String readTransaction() {
		// TODO Auto-generated method stub
		int flag = 0;
		int payerNameSize = 0;
		int payeeNameSize = 0;

		File file = new File("C:\\Users\\Vibavari\\Desktop\\SampleFile_TTS.txt");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
				try {
					while ((st = br.readLine()) != null) {
						
						System.out.println(st);

						String transactionRef = st.substring(0, 12);

						java.util.Date valueDate = null;
						try {
							valueDate = inputFormat.parse(st.substring(12, 20));
						} catch (ParseException e) {
							e.printStackTrace();
						}

						int firstIndex = st.indexOf(' '), midIndex;	//first space
						midIndex = st.substring(firstIndex + 1).indexOf(' ') + firstIndex + 1;  //second space

						String payerName = st.substring(20, firstIndex);
						payerNameSize = payerName.length();
						String payerAccount = st.substring(firstIndex + 1, firstIndex + 13);

						String payeeName = st.substring(firstIndex + 13, midIndex-12);
						payeeNameSize = payeeName.length();
						String payeeAccount = st.substring(firstIndex+13+payeeNameSize, midIndex);

						double amount = Double.parseDouble(st.substring(midIndex+1));
						
						String date = dateFormat.format(valueDate);
						
						System.out.println(transactionRef);
						System.out.println(date);
						System.out.println(payerName);
						System.out.println(payerAccount);
						System.out.println(payeeName);
						System.out.println(payeeAccount);
						System.out.println(amount);
						
						
						boolean validRef = uniqueReferenceId(transactionRef);
						boolean validDate = presentDate(date);
						boolean validPayerName = alphaNumeric(payerName) && payerNameSize <= 35 && payerNameSize>0;
						boolean validPayeeName = alphaNumeric(payeeName) && payeeNameSize <= 35 && payeeNameSize>0;
						boolean validPayerAc = alphaNumeric(payeeAccount);
						boolean validPayeeAc = alphaNumeric(payeeAccount);
						boolean validAmt = validAmount(amount);
						
						System.out.println("Valid Ref: "+ validRef);
						System.out.println("Valid Date: "+ validDate);
						System.out.println("Valid payer name: "+ validPayerName);
						System.out.println("Valid payer acc: "+ validPayerAc);
						System.out.println("Valid payee name: "+ validPayeeName);
						System.out.println("Valid payee acc: "+ validPayeeAc);
						System.out.println("Valid amount: "+ validAmt);

						if (validRef && validDate && validPayerName && validPayeeName && validPayerAc && validPayeeAc && validAmt){
							transactions.add(new Transaction(transactionRef, date, payerName,
							payerAccount, payeeName, payeeAccount, amount, "Valid Pass"));
							System.out.println("Valid Pass");
							flag = 1;
						}
						if (flag == 0) {
							transactions.add(new Transaction(transactionRef, date, payerName, payerAccount,
									payeeName, payeeAccount, amount, "Valid Fail"));
							System.out.println("Valid Fail");
						}
						flag = 0;
					}
					br.close();
					
					//if correct file format
					System.out.println(transactions);
					System.out.println("moving these transactions to DB");
					int added = moveTransactionsToDB();
					System.out.println("Back to read()");
					if(added>0) {
						System.out.println("upload successful");
						String str =  "File uploaded to DB";
						return str;
					}
					else {
						System.out.println("upload NOT successful");
						return "Sorry, file upload to DB failed";
					}

					
				} catch (IOException e) {
					
					e.printStackTrace();
					return "Problem occured while uploading file";
				}
		} catch (FileNotFoundException | NumberFormatException e) {
			e.printStackTrace();
			return "Check file format again";
		}
		
	}

	public boolean uniqueReferenceId(String transactionRef) {
		for (int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).getTransactionRef().equals(transactionRef)) {
				return false;
			}
		}
		return true;
	}

	public boolean presentDate(String date) {

		int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calobj = Calendar.getInstance();
		String s = df.format(calobj.getTime());
		
		// if (date.equals(s)) {
        // return true;
        // }
        // return false;
		try {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8));
            System.out.println("Broken-down date: " + day + "-" + month + "-" + year);
            if (month > 0 && month <= 12 && year == Integer.parseInt(s.substring(0,4))) {
                if (year % 4 == 0) {
                    if (year % 100 == 0) {
                        if (year % 400 == 0)
                            daysInMonth[1] = 29;
                    } else
                        daysInMonth[1] = 28;
                }
                if (day <= daysInMonth[month - 1])
                    return true;

            } else {
                return false;
            }

        } catch (NumberFormatException ex) { // handle your exception
        }
        return false;
	}

	public boolean alphaNumeric(String str) {
		boolean isAlphaNumeric = (str != null) && str.chars().allMatch(Character::isLetterOrDigit);

		return isAlphaNumeric;
	}

	public boolean validAmount(double amount) {
		String[] splitter = Double.toString(amount).split("\\.");
		splitter[0].length();

		if (splitter[0].length() <= 10 && amount > 0) {

			amount = Math.round(amount * 100.0) / 100.0;
			return true;
		} else {

			return false;
		}
	}
	
	@Override
	public int moveTransactionsToDB() {
		int added = 0;
		System.out.println("Printing transactions in moveTransactionsto DB(): ");
		System.out.println(transactions);

		for (Iterator<Transaction> t = transactions.iterator(); t.hasNext();) {
			System.out.println("t= "+ t);
			Transaction t1 = (Transaction) t.next();
			System.out.println("t1= "+ t1);
			//(transactionRef,valueDate,payerName,payerAccount,payeeName,payeeAccount,amount,status)
			added = template.update(
					"insert into fileuploaded values(?,?,?,?,?,?,?,?)",
					t1.getTransactionRef(), t1.getValueDate(), t1.getPayerName(), t1.getPayerAccount(),
					t1.getPayeeName(), t1.getPayeeAccount(), t1.getAmount(), t1.getStatus());
		}
		System.out.println("added = "+ added);

		return added;

	}

	@Override
	public List<Transaction> displayAllTransactionsFromDB() {
		List<Transaction> transactions = template.query("select * from fileuploaded", new RowMapper<Transaction>() {

			@Override
			public Transaction mapRow(ResultSet set, int arg1) throws SQLException {
				return new Transaction(set.getString(1), set.getString(2), set.getString(3), set.getString(4),
						set.getString(5), set.getString(6), set.getDouble(7), set.getString(8));
			}
		});
		System.out.println(transactions);
		return transactions;
	}

	@Override
	public List<Transaction> displayValidTransactions() {
		String VALID = "select * from fileuploaded where status = 'valid'";
		List<Transaction> transactions = template.query(VALID, new RowMapper<Transaction>() {

			@Override
			public Transaction mapRow(ResultSet set, int arg1) throws SQLException {
				return new Transaction(set.getString(1), set.getString(2), set.getString(3), set.getString(4),
						set.getString(5), set.getString(6), set.getDouble(7), set.getString(8));
			}
		});
		validtransactions = (ArrayList<Transaction>) ((ArrayList<Transaction>) transactions).clone();
		return transactions;
	}

	@Override
	public List<Transaction> displayInvalidTransactions() {
		String INVALID = "select * from fileuploaded where status = 'invalid'";
		List<Transaction> transactions = template.query(INVALID, new RowMapper<Transaction>() {

			@Override
			public Transaction mapRow(ResultSet set, int arg1) throws SQLException {
				return new Transaction(set.getString(1), set.getString(2), set.getString(3), set.getString(4),
						set.getString(5), set.getString(6), set.getDouble(7), set.getString(8));
			}
		});
		return transactions;
	}

	@Override
	public List<Transaction> screenfailTransactions() {

		String Screening = "select * from fileuploaded INNER JOIN sanctionlist on fileuploaded.payeeName = sanctionlist.firstName;";
		List<Transaction> transactions = template.query(Screening, new RowMapper<Transaction>() {

			@Override
			public Transaction mapRow(ResultSet set, int arg1) throws SQLException {
				return new Transaction(set.getString(1), set.getString(2), set.getString(3), set.getString(4),
						set.getString(5), set.getString(6), set.getDouble(7), set.getString(8));
			}
		});

		for (Iterator<Transaction> t = transactions.iterator(); t.hasNext();) {
			Transaction t1 = (Transaction) t.next();
			template.update(
					"insert into fileuploaded(transactionRef,valueDate,payerName,payerAccount,payeeName,payeeAccount,Amount,status) values(?,?,?,?,?,?,?,?)",
					t1.getTransactionRef(), t1.getValueDate(), t1.getPayerName(), t1.getPayerAccount(),
					t1.getPayeeName(), t1.getPayeeAccount(), t1.getAmount(), "screen-failed");
		}

		Screening = "select * from fileuploaded where status = 'screen-failed'";
		transactions = template.query(Screening, new RowMapper<Transaction>() {

			@Override
			public Transaction mapRow(ResultSet set, int arg1) throws SQLException {
				return new Transaction(set.getString(1), set.getString(2), set.getString(3), set.getString(4),
						set.getString(5), set.getString(6), set.getDouble(7), set.getString(8));
			}
		});

		return transactions;
	}

	@Override
	public List<Transaction> screenPassTransactions() {
		String Screening = "update fileuploaded set status = 'screen-pass' where status = 'valid' ";
		int update = template.update(Screening);

		String screenPass = "select * from fileuploaded where status = 'screen-pass'";
		List<Transaction> transactions = template.query(screenPass, new RowMapper<Transaction>() {

			@Override
			public Transaction mapRow(ResultSet set, int arg1) throws SQLException {
				return new Transaction(set.getString(1), set.getString(2), set.getString(3), set.getString(4),
						set.getString(5), set.getString(6), set.getDouble(7), set.getString(8));
			}
		});

		return transactions;
	}

	@Override
	public List<Transaction> screeningStatus() {

		String status = "select * from fileuploaded where status NOT IN('invalid')";
		List<Transaction> transactions = template.query(status, new RowMapper<Transaction>() {

			@Override
			public Transaction mapRow(ResultSet set, int arg1) throws SQLException {
				return new Transaction(set.getString(1), set.getString(2), set.getString(3), set.getString(4),
						set.getString(5), set.getString(6), set.getDouble(7), set.getString(8));
			}
		});

		return transactions;
	}

}
