package tibcustomerdata;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;

@SuppressWarnings("serial")

public class JDBCMainWindowContent extends JInternalFrame implements ActionListener
{	
	String cmd = null;
	boolean success=false;

	// DB Connectivity Attributes
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	private Container content;

	private JPanel detailsPanel;
	private JPanel exportButtonPanel;
	private JPanel updateBalancePanel;
	private JPanel displayResultPanel;
	private JScrollPane dbContentsPanel;

	private Border lineBorder;

	private JLabel customerIDLabel=new JLabel("CustomerID:                 ");
	private JLabel firstNameLabel=new JLabel("FirstName:               ");
	private JLabel lastNameLabel=new JLabel("LastName:      ");
	private JLabel addressLabel=new JLabel("Address:        ");
	private JLabel cityLabel=new JLabel("City:                 ");
	private JLabel countryLabel=new JLabel("Country:               ");
	private JLabel emailLabel=new JLabel("Email:      ");
	private JLabel phoneLabel=new JLabel("Phone:      ");
	private JLabel resultLabel=new JLabel("        ");

	private JTextField customerIDTF= new JTextField(10);
	private JTextField firstNameTF=new JTextField(10);
	private JTextField lastNameTF=new JTextField(10);
	private JTextField addressTF=new JTextField(10);
	private JTextField cityTF=new JTextField(10);
	private JTextField countryTF=new JTextField(10);
	private JTextField emailTF=new JTextField(10);
	private JTextField phoneTF=new JTextField(10);
	//private JTextField HoursTF=new JTextField(10);


	private static QueryTableModel TableModel = new QueryTableModel();
	//Add the models to JTabels
	private JTable TableofDBContents=new JTable(TableModel);
	//Buttons for inserting, and updating members
	//also a clear button to clear details panel
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton displayAllButton  = new JButton("View Cust");
	private JButton deleteButton  = new JButton("Delete");
	private JButton clearButton  = new JButton("Clear");
	private JButton viewBalButton  = new JButton("View Bal");

	private JLabel accountID=new JLabel("Account ID        ");
	private JTextField accountIDTF  = new JTextField(12);
	private JLabel txnType=new JLabel("Deposit/Withdrawal        ");
	private JTextField txnTypeTF  = new JTextField(12);
	private JLabel amount=new JLabel("Amount        ");
	private JTextField amountTF  = new JTextField(12);
	private JLabel datelabel=new JLabel("Date(yyyy-mm-dd)        ");
	private JTextField dateTF  = new JTextField(12);
	private JButton  updateBalanceButton = new JButton("Update Balance");
	private JButton  clearButton1 = new JButton("Clear");
	private JTextArea resultArea = new JTextArea(290, 190);
    

	
	
	private JButton custData = new JButton("Customer Data");
	//private JTextField NumLecturesTF  = new JTextField(12);
	private JButton custTxnData  = new JButton("Customer Account Data");
	//private JTextField avgAgeDepartmentTF  = new JTextField(12);
	//private JButton ListAllDepartments  = new JButton("ListAllDepartments");
	//private JButton ListAllPositions  = new JButton("ListAllPositions");



	public JDBCMainWindowContent( String aTitle)
	{	
		//setting up the GUI
		super(aTitle, false,false,false,false);
		setEnabled(true);

		initiate_db_conn();
		//add the 'main' panel to the Internal Frame
		content=getContentPane();
		content.setLayout(null);
		content.setBackground(Color.lightGray);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

		//setup details panel and add the components to it
		detailsPanel=new JPanel();
		detailsPanel.setLayout(new GridLayout(11,2));
		detailsPanel.setBackground(Color.lightGray);
		detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));

		detailsPanel.add(customerIDLabel);			
		detailsPanel.add(customerIDTF);
		detailsPanel.add(firstNameLabel);		
		detailsPanel.add(firstNameTF);
		detailsPanel.add(lastNameLabel);		
		detailsPanel.add(lastNameTF);
		detailsPanel.add(addressLabel);	
		detailsPanel.add(addressTF);
		detailsPanel.add(cityLabel);		
		detailsPanel.add(cityTF);
		detailsPanel.add(countryLabel);
		detailsPanel.add(countryTF);
		detailsPanel.add(emailLabel);
		detailsPanel.add(emailTF);
		detailsPanel.add(phoneLabel);
		detailsPanel.add(phoneTF);
		//detailsPanel.add(HoursLabel);
		//detailsPanel.add(HoursTF);

		//setup Export Data panel and add the components to it
		exportButtonPanel=new JPanel();
		exportButtonPanel.setLayout(new GridLayout(2,1));
		exportButtonPanel.setBackground(Color.lightGray);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
		exportButtonPanel.add(custData);
		exportButtonPanel.add(custTxnData);
		//exportButtonPanel.add(avgAgeDepartment);
		//exportButtonPanel.add(avgAgeDepartmentTF);
		//exportButtonPanel.add(ListAllDepartments);
		//exportButtonPanel.add(ListAllPositions);
		exportButtonPanel.setSize(350, 200);
		exportButtonPanel.setLocation(800, 300);
		content.add(exportButtonPanel);

		//setup Export Data panel and add the components to it
		updateBalancePanel = new JPanel();
		updateBalancePanel.setLayout(new GridLayout(5,3));
		updateBalancePanel.setBackground(Color.lightGray);
		updateBalancePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Update Balance"));
		updateBalancePanel.add(accountID);
		updateBalancePanel.add(accountIDTF);
		updateBalancePanel.add(txnType);
		updateBalancePanel.add(txnTypeTF);
		updateBalancePanel.add(amount);
		updateBalancePanel.add(amountTF);
		updateBalancePanel.add(datelabel);
		updateBalancePanel.add(dateTF);
		updateBalancePanel.add(updateBalanceButton);
		updateBalancePanel.add(clearButton1);
		updateBalancePanel.setSize(500, 200);
		updateBalancePanel.setLocation(3, 300);
		content.add(updateBalancePanel);		
		
		//setup Display Result panel and add the components to it
		displayResultPanel = new JPanel();
		displayResultPanel.setLayout(new GridLayout(1,1));
		displayResultPanel.setBackground(Color.LIGHT_GRAY);
		displayResultPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Update Balance Status"));
		resultArea.setText("");
	    resultArea.setWrapStyleWord(true);
	    resultArea.setLineWrap(true);
	    resultArea.setOpaque(false);
	    resultArea.setEditable(false);
	    resultArea.setFocusable(false);
	    resultArea.setBackground(UIManager.getColor("Label.background"));
	    resultArea.setFont(UIManager.getFont("Label.font"));
	    resultArea.setBorder(UIManager.getBorder("Label.border"));
		//resultLabel.setPreferredSize(new Dimension(290, 190));
		displayResultPanel.add(resultArea);
		displayResultPanel.setSize(300, 200);
		displayResultPanel.setLocation(500, 300);
		content.add(displayResultPanel);	
		
		
		insertButton.setSize(100, 30);
		updateButton.setSize(100, 30);
		displayAllButton.setSize (100, 30);
		deleteButton.setSize (100, 30);
		clearButton.setSize (100, 30);
		viewBalButton.setSize(100,30);

		insertButton.setLocation(370, 10);
		updateButton.setLocation(370, 110);
		displayAllButton.setLocation (370, 160);
		deleteButton.setLocation (370, 60);
		viewBalButton.setLocation (370, 210);
		clearButton.setLocation (370, 260);

		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		displayAllButton.addActionListener(this);
		deleteButton.addActionListener(this);
		viewBalButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		this.updateBalanceButton.addActionListener(this);
		this.clearButton1.addActionListener(this);
		this.custData.addActionListener(this);
		this.custTxnData.addActionListener(this);


		content.add(insertButton);
		content.add(updateButton);
		content.add(displayAllButton);
		content.add(deleteButton);
		content.add(clearButton);
		content.add(viewBalButton);


		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));
		TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_ALL_COLUMNS);


		dbContentsPanel=new JScrollPane(TableofDBContents,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dbContentsPanel.setBackground(Color.lightGray);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,"Database Content"));

		detailsPanel.setSize(360, 300);
		detailsPanel.setLocation(3,0);
		dbContentsPanel.setSize(700, 300);
		dbContentsPanel.setLocation(477, 0);

		content.add(detailsPanel);
		content.add(dbContentsPanel);

		setSize(982,645);
		setVisible(true);

		TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_ALL_COLUMNS);
		TableModel.refreshFromDB(stmt);
		TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_ALL_COLUMNS);
	}

	public void initiate_db_conn()
	{
		try
		{
			// Load the JConnector Driver
			Class.forName("com.mysql.jdbc.Driver");
			// Specify the DB Name
			String url="jdbc:mysql://localhost:3306/tibdatabase";
			// Connect to DB using DB URL, Username and password
			con = DriverManager.getConnection(url, "root", "root");
			//Create a generic statement which is passed to the TestInternalFrame1
			stmt = con.createStatement();
		}
		catch(Exception e)
		{
			System.out.println("Error: Failed to connect to database\n"+e.getMessage());
		}
	}

	//event handling 
	public void actionPerformed(ActionEvent e)
	{
		Object target=e.getSource();
		if (target == clearButton)
		{
			customerIDTF.setText("");
			firstNameTF.setText("");
			lastNameTF.setText("");
			addressTF.setText("");
			cityTF.setText("");
			countryTF.setText("");
			emailTF.setText("");
			phoneTF.setText("");
			
			//TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_OFF);
			//TableModel.refreshFromDBJoin(stmt);

		}

		if (target == insertButton)
		{		 
			try
			{
				String updateTemp ="INSERT INTO Customer(FirstName, LastName, Address, City, Country, Email, PhoneNumber) VALUES('"+
						firstNameTF.getText()+"','"+lastNameTF.getText()+"','"+
						addressTF.getText()+"','"+cityTF.getText()+"','"+
						countryTF.getText()+"','"+emailTF.getText()+"','"+phoneTF.getText()+"');";

				stmt.executeUpdate(updateTemp);

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  insert:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
			}
		}
		if (target == deleteButton)
		{

			try
			{
				String updateTemp ="DELETE FROM Customer WHERE CustomerID = "+customerIDTF.getText()+";"; 
				stmt.executeUpdate(updateTemp);

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with delete:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
			}
		}
		if (target == updateButton)
		{	 	
			try
			{ 			
				String updateTemp ="UPDATE Customer SET " +
						"firstName = '"+firstNameTF.getText()+
						"', lastName = '"+lastNameTF.getText()+
						"', Address = '"+addressTF.getText()+
						"', City ='"+cityTF.getText()+
						"', Country = '"+countryTF.getText()+
						"', Email = '"+emailTF.getText()+
						"', PhoneNumber = '"+phoneTF.getText()+
						//", hours = "+HoursTF.getText()+
						"' where CustomerID = "+customerIDTF.getText();


				stmt.executeUpdate(updateTemp);
				//these lines do nothing but the table updates when we access the db.
				rs = stmt.executeQuery("SELECT * from Customer ");
				rs.next();
				rs.close();	
			}
			catch (SQLException sqle){
				System.err.println("Error with  update:\n"+sqle.toString());
			}
			finally{
				TableModel.refreshFromDB(stmt);
			}
		}
		if (target == displayAllButton)
		{


			try
			{
				String displayTemp = "CALL GetCustomerDetails("+customerIDTF.getText()+");";
				stmt.executeUpdate(displayTemp);

				//String updateTemp ="DELETE FROM Customer WHERE CustomerID = "+customerIDTF.getText()+";"; 
				//stmt.executeUpdate(updateTemp);


			}
			catch (SQLException sqle)
			{
				System.err.println("Error with Stored procedure:\n"+sqle.toString());
			}
			finally
			{
				TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_OFF);
				TableModel.refreshFromDBsp(stmt,customerIDTF.getText());
				//TableModel.refreshFromDBJoin(stmt);
			}
		}
		if (target == viewBalButton)
		{


//			try
//			{
//				//String viewBalTemp = "GetTotalBalanceWithDetails("+customerIDTF.getText()+")";
//				//stmt.executeUpdate(viewBalTemp);
//				HoursLabel.setText("hi");
//				String viewBalTemp = "SELECT CustomerID, FirstName, LastName, GetTotalBalanceWithDetails(CustomerID) AS TotalBalance FROM Customer WHERE CustomerID = "+customerIDTF.getText()+");";
//				stmt.executeUpdate(viewBalTemp);
//				//String updateTemp ="DELETE FROM Customer WHERE CustomerID = "+customerIDTF.getText()+";"; 
//				//stmt.executeUpdate(updateTemp);
//				System.out.println("Balance ");
//
//
//			}
//			catch (SQLException sqle)
//			{
//				System.err.println("Error with Stored procedure:\n"+sqle.toString());
//			}
//			finally
//			{
				TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_OFF);
				TableModel.refreshFromDBsf(stmt,customerIDTF.getText());
			//	TableModel.refreshFromDBJoin(stmt);
			//}

		}
		if (target == this.updateBalanceButton)
		{	
			success=true;
			try
			{ 	
				resultArea.setText("");
				
				String updateBalTemp ="call updatebalance('"+accountIDTF.getText()+"','"+txnTypeTF.getText()+"',"+amountTF.getText()+",'"+dateTF.getText()+"');";

				stmt.executeUpdate(updateBalTemp);
				//these lines do nothing but the table updates when we access the db.
				rs = stmt.executeQuery("SELECT * from Customer ");
				rs.next();
				rs.close();	
			}
			catch (SQLException sqle){
				if (sqle.getSQLState().equals("45000")) {
					
					resultArea.setText("");
	    	        resultArea.setText("Rejected: " + sqle.getMessage());
	    	        success=false;
	    	    }
				//System.err.println("Error with  update:\n"+sqle.toString());
			}
			finally{
				TableModel.refreshFromDB(stmt);
			}
			if(success==true) {
				resultArea.setText("");
				resultArea.setText("Transaction Success!!!");
			}
		}

		
		
		
		
		
		
		
		
		
		
//		if (target == updateBalanceButton)
//		{
//
//				TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_OFF);
//				TableModel.refreshFromDBtr(stmt,accountIDTF.getText(),txnTypeTF.getText(),amountTF.getText(),dateTF.getText());
//
//		}

		
		if (target == this.clearButton1)
		{
			accountIDTF.setText("");
			txnTypeTF.setText("");
			amountTF.setText("");
			dateTF.setText("");
			resultArea.setText("");
			TableofDBContents.setAutoResizeMode(TableofDBContents.AUTO_RESIZE_OFF);
			TableModel.refreshFromDBJoin(stmt);

		}

		
		

			/////////////////////////////////////////////////////////////////////////////////////
			//I have only added functionality of 2 of the button on the lower right of the template
			///////////////////////////////////////////////////////////////////////////////////

			if(target == this.custData){

				cmd = "select * from Customer;";

				try{					
					rs= stmt.executeQuery(cmd); 	
					writeToFile(rs);
				}
				catch(Exception e1){e1.printStackTrace();}

			}

			if(target == this.custTxnData){
				//String deptName = this.NumLecturesTF.getText();

				cmd = "SELECT c.CustomerID, c.FirstName, c.LastName, c.Address, c.City, c.Country, c.Email,"
						+ " c.PhoneNumber, a.AccountID, a.AccountType, a.Balance, d.CardID, d.CardType, d.ExpiryDate, l.LoanID,"
						+ " l.LoanType, l.LoanAmount, l.InterestRate, l.LoanTerm FROM Customer c "
						+ "JOIN Account a ON c.CustomerID = a.CustomerID "
						+ "LEFT JOIN Card d ON a.AccountID = d.AccountID "
						+ "LEFT JOIN Loan l ON a.AccountID = l.AccountID";
				System.out.println(cmd);
				try{					
					rs= stmt.executeQuery(cmd); 	
					writeToFile(rs);
				}
				catch(Exception e1){e1.printStackTrace();}

			}
		

	}
	///////////////////////////////////////////////////////////////////////////

	private void writeToFile(ResultSet rs){
		try{
			System.out.println("In writeToFile");
			FileWriter outputFile = new FileWriter("output.csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for(int i=0;i<numColumns;i++){
				printWriter.print(rsmd.getColumnLabel(i+1)+",");
			}
			printWriter.print("\n");
			while(rs.next()){
				for(int i=0;i<numColumns;i++){
					printWriter.print(rs.getString(i+1)+",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}
