
//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.connection.MyConnection;

public class TestConnection {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		try {			
			MyConnection m=new MyConnection();
			
			System.out.println("connection");
			Statement st=m.getConnection().createStatement();
			
			int added=st.executeUpdate("INSERT INTO fileUploaded(transactionRef,valueDate,payerName,payerAccount,payeeName,payeeAccount,amount,status) values(?,?,?,?,?,?,?,?)");
			if(added>0)
			{
				System.out.println("Transaction inserted");
			}
			else
			{
				System.out.println("Sorry");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

