package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Utility.FileUploadHelper;
import com.example.demo.model.SanctionList;
import com.example.demo.model.Transaction;
import com.example.demo.repository.SanctionListRepository;
import com.example.demo.repository.TransactionRepositoy;
import com.sun.el.parser.ParseException;
import com.example.demo.Utility.Util;

@Service
public class TransactionService {

	@Autowired
	TransactionRepositoy transactionRepo;

	@Autowired
	SanctionListRepository sanctionRepo;

	@Autowired
	public FileUploadHelper fileUploadHelper;

	List<Transaction> transactions = new ArrayList<>();
	List<Transaction> validtransactions = new ArrayList<>();
	

	public void addTransactions() {
//		long millis = System.currentTimeMillis();
//		java.util.Date d = new java.sql.Date(millis);
//		Transaction t1 = new Transaction("1", d, "elena", "123", "damon", "345", 23.5, "valid");
//		Transaction t2 = new Transaction("2", d, "Manasi", "1234", "Bhandari", "6789", 2346, "invalid");
//		Transaction t3 = new Transaction("3", d, "Manasi", "1234", "Noel", "6789", 2346, "valid");
//		transactionRepo.save(t1);
//		transactionRepo.save(t2);
//		transactionRepo.save(t3);

		for (Iterator<Transaction> t = transactions.iterator(); t.hasNext();) {

			Transaction t1 = (Transaction) t.next();
			transactionRepo.save(t1);

		}

	}

	public ResponseEntity<String> FileUpload(MultipartFile file) {

		try {

			if (file.isEmpty()) {

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("UPLOAD A FILE");
			}

			System.out.println(file.getContentType());

			if (!file.getContentType().equals("text/plain")) {

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FILE TYPE NOT SUPPORTED");

			}

			boolean uploaded = fileUploadHelper.uploadFile(file);

			if (uploaded) {

				int flag = 0;
				int payerNameSize = 0, payeeNameSize = 0;
				
				BufferedReader br = null;
				
				try {
					
					br = new BufferedReader(new FileReader("C:\\\\Users\\\\raneb\\\\OneDrive\\\\Desktop\\\\cbp\\\\cbp-sanction-screening-main\\\\UPLOADS\\\\"+file.getOriginalFilename()));
					String st = "";
					
					try {
						try {
							while ((st = br.readLine()) != null) {
								
								flag = 0;
								String ipdate = null;
								
								DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
								DateFormat inputFormat = new SimpleDateFormat("ddMMyyyy");

								System.out.println(st);
								String transactionRef = st.substring(0, 12);
								System.out.println("Id :" + transactionRef);

								java.sql.Date valueDate = null;
								
								
									
								System.out.println(st.substring(12, 20));
								ipdate = st.substring(12, 20);
									
								
								SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyyyy");
								java.util.Date date = sdf1.parse(ipdate);
								valueDate = new java.sql.Date(date.getTime());
								
								
								System.out.println("Date :" + valueDate);
								
								int firstIndex = st.indexOf(' '), midIndex;
								midIndex = st.substring(firstIndex + 1).indexOf(' ') + firstIndex + 1;

								String payerName = st.substring(20, firstIndex);
								payerNameSize = firstIndex - 20;
								System.out.println("Payer name :" + payerName);

								String payerAccount = st.substring(firstIndex + 1, firstIndex + 13);
								System.out.println("Payer acc :" + payerAccount);

								String payeeName = st.substring(firstIndex + 13, midIndex);
								payeeNameSize = midIndex - (firstIndex + 13);
								System.out.println("Payee name :" + payeeName);

								String payeeAccount = st.substring(midIndex + 1, midIndex + 13);
								System.out.println("Payee acc :" + payeeAccount);

								double amount = Double.parseDouble(st.substring(midIndex + 13));
								System.out.println("amount :" + amount);

								if (Util.uniqueReferenceId(transactions, transactionRef)) {

									String s1 = dateFormat.format(valueDate);
									System.out.println(s1);
									
									if (Util.presentDate(s1)) {
										
										System.out.println("Date :" + valueDate);
										
										if (Util.alphaNumeric(payerName) && payerNameSize <= 35 && Util.alphaNumeric(payeeName)
												&& payeeNameSize <= 35 && Util.alphaNumeric(payeeAccount)
												&& Util.alphaNumeric(payeeAccount)) {
											
											if (Util.validAmount(amount)) {
												
												transactions.add(new Transaction(transactionRef, valueDate, payerName,
														payerAccount, payeeName, payeeAccount, amount, "Valid Pass"));
												
												flag = 1;
												
											} 
											else {
												
												System.out.println("No amount");
											}
										} 
										else {
											
											System.out.println("No name or account");
										}
									} 
									else {
										
										System.out.println("Date :" + valueDate);
										System.out.println("No date");
									}
									
								} 
								else {
									
									System.out.println("No id");
								}

								if (flag == 0) {
									
									transactions.add(new Transaction(transactionRef, valueDate, payerName, payerAccount,
											payeeName, payeeAccount, amount, "Valid Fail"));
								}
								
								flag = 0;
								
								addTransactions();
								
							}
							
						} catch (IOException e) {

							e.printStackTrace();
							
						} catch (StringIndexOutOfBoundsException e) {
							
							System.out.println("Exception occurred . . . . . . . . ");
						}
						
					} catch (NumberFormatException e) {

						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}

				return ResponseEntity.ok("FILE SUCCESSFULLY UPLOADED!");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR. TRY AGAIN.");

		
	}

	public List<Transaction> displayAllTransactionsFromDB() {

		return transactionRepo.findAll();
	}

	public List<Transaction> displayStatus(String status) {
		return transactionRepo.displayStatus(status);
	}

	public List<Transaction> screening() {
		List<Transaction> transactions = new ArrayList<Transaction>();

		for (Transaction t : transactionRepo.findAll()) {
			String payer = t.getPayerName();
			String payee = t.getPayeeName();
			for (SanctionList s : sanctionRepo.findAll()) {
				if (payee.equalsIgnoreCase(s.getName()) || payer.equalsIgnoreCase(s.getName())) {

					transactions.add(saveWithStatus(t, "screen-failed"));
				} else {
					if (t.getStatus().equalsIgnoreCase("valid")) {
						saveWithStatus(t, "screen-pass");
					}
				}
			}
		}

		return transactions;
	}

	public Transaction saveWithStatus(Transaction t, String status) {
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
