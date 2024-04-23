package tibcustomerdata;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDBCMainWindow extends JFrame implements ActionListener
	{
	
	private static final long serialVersionUID = 1L;
		private JMenuItem exitItem;

		public JDBCMainWindow()
		{
			// Sets the Window Title
			super( "JDBC Assignment" ); 
			
			//Setup fileMenu and its elements
			JMenuBar menuBar=new JMenuBar();
			JMenu fileMenu=new JMenu("File");
			exitItem =new JMenuItem("Exit");
	
			fileMenu.add(exitItem);
			menuBar.add(fileMenu );
			setJMenuBar(menuBar);
			
			// Add a listener to the Exit Menu Item
			exitItem.addActionListener(this);

			// Create an instance of our class JDBCMainWindowContent 
			JDBCMainWindowContent aWindowContent = new JDBCMainWindowContent( "Welcome to TUS International Bank (TIB) Bank Database via JDBC");
			// Add the instance to the main section of the window
			getContentPane().add( aWindowContent );
			
			setSize( 1200, 600 );
			setVisible( true );
		}
		
		// The event handling for the main frame
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource().equals(exitItem)){
				this.dispose();
			}
		}
		
		
		
	}