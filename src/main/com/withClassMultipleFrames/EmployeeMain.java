package main.com.withClassMultipleFrames;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class EmployeeMain {

	private JFrame frame;
	private JTextField idTextField;
	private JTextField fnameTextField;
	private JTextField lnameTextField;
	private JTextField jobTextField;
	protected static JTable displayTable;
	
	Connection con;
    PreparedStatement st;
    ArrayList<Employee> employees = new ArrayList<Employee>();

	/* Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeMain window = new EmployeeMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/* Create the application. */
	public EmployeeMain() {
		initialize();
		load();
	}
	
	// METHODS
	
	 private void clear() {
		 idTextField.setText("");
		 fnameTextField.setText("");
		 lnameTextField.setText("");
		 jobTextField.setText("");
	 }  
	       
	
    private void load() {
    	/* LOAD DATA */ 
        try {
        	int CC;
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacrud", "root", "toor");        
                st = con.prepareStatement("SELECT * FROM employee");
                ResultSet Rs = st.executeQuery();
                
                ResultSetMetaData RSMD = Rs.getMetaData();
                CC = RSMD.getColumnCount();
                DefaultTableModel DFT = (DefaultTableModel) displayTable.getModel();
                DFT.setRowCount(0);
                while (Rs.next()) {
                	
                    Vector<String> vec = new Vector<String>();
               
                    for (int ii = 1; ii <= CC; ii++) {
                        vec.add(Integer.toString(Rs.getInt("empl_id")));
                        vec.add(Rs.getString("empl_fname"));
                        vec.add(Rs.getString("empl_lname"));
                        vec.add(Rs.getString("empl_job"));
                    }
                    DFT.addRow(vec);
                }

        } catch (SQLException ex) {
        	JOptionPane.showMessageDialog(null, ex.toString(), "Database Error: load", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {   
    	String id = idTextField.getText().trim();
        String fname = fnameTextField.getText().trim();
        String lname = lnameTextField.getText().trim();
        String job = jobTextField.getText().trim();
        
        if (!id.isEmpty() && !fname.isEmpty() && !lname.isEmpty() && !job.isEmpty()) {
            try {            	
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacrud", "root", "toor");        
                st = con.prepareStatement("SELECT * FROM employee WHERE empl_id = '" + id + "'");                
                     
                ResultSet rs = st.executeQuery();
                if (!rs.first()) {
                	
                    saveEmployee(id, fname, lname, job);
                    
                    DefaultTableModel model = (DefaultTableModel) displayTable.getModel();
                    Object[] row = new Object[4];
                    row[0] = id;
                    row[1] = fname;
                    row[2] = lname;
                    row[3] = job;
                                      
                    model.addRow(row);
                } else {
                	JOptionPane.showMessageDialog(null, "Please provide a different ID", "Similar id found", JOptionPane.ERROR_MESSAGE);
                }

                clear();
            } catch (SQLException ex) {
            	JOptionPane.showMessageDialog(null, ex.toString(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    con.close();
                    st.close();
                } catch (SQLException ex) {
                	JOptionPane.showMessageDialog(null, ex.toString(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } //        else if (!id.matches("^[0-9]{8}$")) {
        //            alert("please provide a valid id number", "Wrong id");
        //        } 
        else {
        	JOptionPane.showMessageDialog(null, "Please fill in all the details", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }         
    
    // Method to save Employee to the DB
    public void saveEmployee(String id, String fname, String lname, String job) {
    	
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacrud", "root", "toor");
            
            String sql = "INSERT INTO Employee (empl_id, empl_fname, empl_lname, empl_job) VALUES (?, ?, ?, ?)"; 
                     
            PreparedStatement preparedStmt = con.prepareStatement(sql);
            preparedStmt.setInt(1, Integer.parseInt(id));
            preparedStmt.setString(2, fname);
            preparedStmt.setString(3, lname);
            preparedStmt.setString(4, job);    
            
			int rowsInserted = preparedStmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println();
				JOptionPane.showMessageDialog(null, "Employee added successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
                                 
        } catch (ClassNotFoundException | SQLException ex) {
        	JOptionPane.showMessageDialog(null, ex.toString(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        //load();
    }

	/* Initialize the contents of the frame. */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 913, 378);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Employee ID");
		lblNewLabel.setBounds(21, 51, 103, 14);
		frame.getContentPane().add(lblNewLabel);
		
		idTextField = new JTextField();
		idTextField.setBounds(134, 48, 158, 20);
		frame.getContentPane().add(idTextField);
		idTextField.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(21, 85, 103, 14);
		frame.getContentPane().add(lblFirstName);
		
		fnameTextField = new JTextField();
		fnameTextField.setColumns(10);
		fnameTextField.setBounds(134, 82, 158, 20);
		frame.getContentPane().add(fnameTextField);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(21, 116, 103, 14);
		frame.getContentPane().add(lblLastName);
		
		lnameTextField = new JTextField();
		lnameTextField.setColumns(10);
		lnameTextField.setBounds(134, 113, 158, 20);
		frame.getContentPane().add(lnameTextField);
		
		JLabel lblJob = new JLabel("Job");
		lblJob.setBounds(21, 159, 103, 14);
		frame.getContentPane().add(lblJob);
		
		jobTextField = new JTextField();
		jobTextField.setColumns(10);
		jobTextField.setBounds(134, 156, 158, 20);
		frame.getContentPane().add(jobTextField);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSaveActionPerformed(e);
			}
		});
		btnSave.setBounds(10, 222, 131, 44);
		frame.getContentPane().add(btnSave);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
                	EmployeeUpdate f2 = new EmployeeUpdate();
    				f2.setVisible(true);             
			}
		});
		btnUpdate.setBounds(161, 222, 131, 44);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnArchive = new JButton("Archive");
		btnArchive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnArchive.setBounds(10, 287, 131, 44);
		frame.getContentPane().add(btnArchive);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(302, 48, 585, 275);
		frame.getContentPane().add(scrollPane);
		
		displayTable = new JTable();
		
		scrollPane.setViewportView(displayTable);
		displayTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Employee ID", "First Name", "Last Surname", "Job"
			}
		));
		
		displayTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		displayTable.setBounds(302, 48, 457, 283);
		//frame.getContentPane().add(displayTable);
	}
}
