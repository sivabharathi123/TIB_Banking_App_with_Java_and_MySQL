package tibcustomerdata;
import javax.swing.table.AbstractTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

@SuppressWarnings("serial")
class QueryTableModel extends AbstractTableModel
{
	Vector modelData; //will hold String[] objects
	int colCount;
	String[] headers=new String[0] ;
	Connection con;
	Statement stmt = null;
	String[] record;
	ResultSet rs = null;
	String custID="", accountID="", txnType="", datetf="";
	int amount=0;

	public QueryTableModel(){
		modelData = new Vector();
	}//end constructor QueryTableModel

	public String getColumnName(int i){
		return headers[i];
	}	
	public int getColumnCount(){
		return colCount;
	}
	
	public int getRowCount(){
		return modelData.size();
	}
	
	public Object getValueAt(int row, int col){
		return ((String[])modelData.elementAt(row))[col];
	}

	public void refreshFromDB( Statement stmt1)
	{
		//modelData is the data stored by the table
		//when set query is called the data from the 
		//DB is queried using �SELECT * FROM myInfo� 
		//and the data from the result set is copied 
		//into the modelData. Every time refreshFromDB is
		//called the DB is queried and a new 
		//modelData is created  

		modelData = new Vector();
		stmt = stmt1;
		try{
			//Execute the query and store the result set and its metadata
			rs = stmt.executeQuery("SELECT * FROM Customer");
			ResultSetMetaData meta = rs.getMetaData();
		
			//to get the number of columns
			colCount = meta.getColumnCount(); 
			// Now must rebuild the headers array with the new column names
			headers = new String[colCount];
	
			for(int h = 0; h<colCount; h++)
			{
				headers[h] = meta.getColumnName(h+1);
			}//end for loop
		
			// fill the cache with the records from the query, ie get all the rows
		
			while(rs.next())
			{
				record = new String[colCount];
				for(int i = 0; i < colCount; i++)
				{
					record[i] = rs.getString(i+1);
				}//end for loop
				modelData.addElement(record);
			}// end while loop
			fireTableChanged(null); //Tell the listeners a new table has arrived.
		}//end try clause
		catch(Exception e) {
					System.out.println("Error with refreshFromDB Method\n"+e.toString());
		} // end catch clause to query table
	}//end refreshFromDB method
	
	public void refreshFromDBJoin( Statement stmt1)
	{
		//modelData is the data stored by the table
		//when set query is called the data from the 
		//DB is queried using �SELECT * FROM myInfo� 
		//and the data from the result set is copied 
		//into the modelData. Every time refreshFromDB is
		//called the DB is queried and a new 
		//modelData is created  

		modelData = new Vector();
		stmt = stmt1;
		try{
			//Execute the query and store the result set and its metadata
			rs = stmt.executeQuery("SELECT c.CustomerID, c.FirstName, c.LastName, c.Address, c.City, c.Country, c.Email,"
					+ " c.PhoneNumber, a.AccountID, a.AccountType, a.Balance, d.CardID, d.CardType, d.ExpiryDate, l.LoanID,"
					+ " l.LoanType, l.LoanAmount, l.InterestRate, l.LoanTerm FROM Customer c "
					+ "JOIN Account a ON c.CustomerID = a.CustomerID "
					+ "LEFT JOIN Card d ON a.AccountID = d.AccountID "
					+ "LEFT JOIN Loan l ON a.AccountID = l.AccountID");
			ResultSetMetaData meta = rs.getMetaData();
		
			//to get the number of columns
			colCount = meta.getColumnCount(); 
			// Now must rebuild the headers array with the new column names
			headers = new String[colCount];
	
			for(int h = 0; h<colCount; h++)
			{
				headers[h] = meta.getColumnName(h+1);
			}//end for loop
		
			// fill the cache with the records from the query, ie get all the rows
		
			while(rs.next())
			{
				record = new String[colCount];
				for(int i = 0; i < colCount; i++)
				{
					record[i] = rs.getString(i+1);
				}//end for loop
				modelData.addElement(record);
			}// end while loop
			fireTableChanged(null); //Tell the listeners a new table has arrived.
		}//end try clause
		catch(Exception e) {
					System.out.println("Error with refreshFromDB Method\n"+e.toString());
		} // end catch clause to query table
	}//end refreshFromDB method
	
	public void refreshFromDBsp( Statement stmt1, String custID)
	{
		//modelData is the data stored by the table
		//when set query is called the data from the 
		//DB is queried using �SELECT * FROM myInfo� 
		//and the data from the result set is copied 
		//into the modelData. Every time refreshFromDB is
		//called the DB is queried and a new 
		//modelData is created  

		modelData = new Vector();
		stmt = stmt1;
		custID = custID;
		try{
			//Execute the query and store the result set and its metadata
			rs = stmt.executeQuery("CALL GetCustomerDetails("+custID+");");
			ResultSetMetaData meta = rs.getMetaData();
		
			//to get the number of columns
			colCount = meta.getColumnCount(); 
			// Now must rebuild the headers array with the new column names
			headers = new String[colCount];
	
			for(int h = 0; h<colCount; h++)
			{
				headers[h] = meta.getColumnName(h+1);
			}//end for loop
		
			// fill the cache with the records from the query, ie get all the rows
		
			while(rs.next())
			{
				record = new String[colCount];
				for(int i = 0; i < colCount; i++)
				{
					record[i] = rs.getString(i+1);
				}//end for loop
				modelData.addElement(record);
			}// end while loop
			fireTableChanged(null); //Tell the listeners a new table has arrived.
		}//end try clause
		catch(Exception e) {
					System.out.println("Error with refreshFromDB Method\n"+e.toString());
		} // end catch clause to query table
	}//end refreshFromDB method

//	public void refreshFromDBsf( Statement stmt1, String custID1)
//	{
//		//modelData is the data stored by the table
//		//when set query is called the data from the 
//		//DB is queried using �SELECT * FROM myInfo� 
//		//and the data from the result set is copied 
//		//into the modelData. Every time refreshFromDB is
//		//called the DB is queried and a new 
//		//modelData is created  
//
//		modelData = new Vector();
//		stmt = stmt1;
//		custID = custID1;
//		try{
//			//Execute the query and store the result set and its metadata
//			rs = stmt.executeQuery("SELECT CustomerID, FirstName, LastName, GetTotalBalanceWithDetails("+custID+") AS TotalBalance FROM Customer WHERE CustomerID = "+custID+";");
//			ResultSetMetaData meta = rs.getMetaData();
//		
//			//to get the number of columns
//			colCount = meta.getColumnCount(); 
//			// Now must rebuild the headers array with the new column names
//			headers = new String[colCount];
//	
//			for(int h = 0; h<colCount; h++)
//			{
//				headers[h] = meta.getColumnName(h+1);
//			}//end for loop
//		
//			// fill the cache with the records from the query, ie get all the rows
//		
//			while(rs.next())
//			{
//				record = new String[colCount];
//				for(int i = 0; i < colCount; i++)
//				{
//					record[i] = rs.getString(i+1);
//				}//end for loop
//				modelData.addElement(record);
//			}// end while loop
//			fireTableChanged(null); //Tell the listeners a new table has arrived.
//		}//end try clause
//		catch(Exception e) {
//					System.out.println("Error with refreshFromDB Method\n"+e.toString());
//		} // end catch clause to query table
//	}//end refreshFromDB method

	public void refreshFromDBsf( Statement stmt1, String custID1)
	{
	    //modelData is the data stored by the table
	    //when set query is called the data from the 
	    //DB is queried using �SELECT * FROM myInfo� 
	    //and the data from the result set is copied 
	    //into the modelData. Every time refreshFromDB is
	    //called the DB is queried and a new 
	    //modelData is created  

	    modelData = new Vector();
	    stmt = stmt1;
	    custID = custID1;
	    try{
	        //Execute the query and store the result set and its metadata
	        rs = stmt.executeQuery("SELECT CustomerID, FirstName, LastName, GetTotalBalanceWithDetails('"+custID+"') AS TotalBalance FROM Customer WHERE CustomerID = '"+custID+"';");
	        ResultSetMetaData meta = rs.getMetaData();
	    
	        //to get the number of columns
	        colCount = meta.getColumnCount(); 
	        // Now must rebuild the headers array with the new column names
	        headers = new String[colCount];

	        for(int h = 0; h<colCount; h++)
	        {
	            headers[h] = meta.getColumnName(h+1);
	        }//end for loop
	    
	        // fill the cache with the records from the query, ie get all the rows
	    
	        while(rs.next())
	        {
	            record = new String[colCount];
	            for(int i = 0; i < colCount; i++)
	            {
	                record[i] = rs.getString(i+1);
	            }//end for loop
	            modelData.addElement(record);
	        }// end while loop
	        fireTableChanged(null); //Tell the listeners a new table has arrived.
	    }//end try clause
	    catch(Exception e) {
	            System.out.println("Error with refreshFromDBsf Method\n"+e.toString());
	    } // end catch clause to query table
	}//end refreshFromDB method
	
	
	
}// end class QueryTableModel
