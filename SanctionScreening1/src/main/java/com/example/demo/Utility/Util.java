package com.example.demo.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.example.demo.model.Transaction;

public class Util {

	public static boolean presentDate(String date) {

		int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		Calendar calobj = Calendar.getInstance();
		String s = df.format(calobj.getTime());
		System.out.println(s);
		try {
			int day = Integer.parseInt(date.substring(0, 2));
			int month = Integer.parseInt(date.substring(2, 4));
			int year = Integer.parseInt(date.substring(4));
			
			System.out.println(day + "\t" + month + "\t" + year);
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

			} else {
				return false;
			}

		} catch (NumberFormatException ex) { // handle your exception
		}
		return false;

	}

	public static boolean alphaNumeric(String str) {
		boolean isAlphaNumeric = (str != null) && str.chars().allMatch(Character::isLetterOrDigit);

		return isAlphaNumeric;
	}

	public static boolean validAmount(double amount) {
		String[] splitter = Double.toString(amount).split("\\.");
		splitter[0].length();

		if (splitter[0].length() <= 10 && amount > 0) {

			amount = Math.round(amount * 100.0) / 100.0;
			return true;
			
		} else {

			return false;
		}
	}

	public static boolean uniqueReferenceId(List<Transaction> transactions, String transactionRef) {
		for (int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).getTransactionRef().equals(transactionRef)) {
				return false;
			}
		}
		return true;

	}
}
