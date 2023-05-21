package main.com.withClassMultipleFrames;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class EmployeeUpdate extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField idTextField;
	private JTextField fnameTextField;
	private JTextField lnameTextField;
	private JTextField jobTextField;
		
	Connection con;
    PreparedStatement st;
    
    private void clear() {
		 idTextField.setText("");
		 fnameTextField.setText("");
		 lnameTextField.setText("");
		 jobTextField.setText("");
	 }  
	
	public void update(String id, String fname, String lname, String job) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacrud", "root", "toor");
           
            String sql = "UPDATE Employee SET empl_id=?, empl_fname=?, empl_lname=? , empl_job=? WHERE empl_id=?";

			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, Integer.parseInt(idTextField.getText()));
			statement.setString(2, fnameTextField.getText());
			statement.setString(3, lnameTextField.getText());
			statement.setString(4, jobTextField.getText());
			statement.setInt(5, Integer.parseInt(idTextField.getText()));

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0) {
				 JOptionPane.showMessageDialog(null, "Update Successful", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
            
        } catch (ClassNotFoundException | SQLException ex) {
            
        }
    }
	
	private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {                                          
		
        String id = idTextField.getText().trim();
        String fname = fnameTextField.getText().trim();
        String lname = lnameTextField.getText().trim();
        String job = jobTextField.getText().trim();
        
        if (!id.isEmpty() && !fname.isEmpty() && !lname.isEmpty() && !job.isEmpty()) {
            try {
                try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					
				}
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacrud", "root", "toor");
                st = con.prepareStatement("SELECT * FROM employee WHERE empl_id = '" + id + "'");       
                ResultSet rs = st.executeQuery();
                if (rs.first()) {
                    update(id, fname, lname, job);                 
                    
                } else {
                	JOptionPane.showMessageDialog(null, "There is no such Employee", "Update error", JOptionPane.INFORMATION_MESSAGE);
                    clear();
                }

            } catch (SQLException ex) {
               
            }
        } else {
            JOptionPane.showMessageDialog(null, "There is nothing to update","Nothing", JOptionPane.INFORMATION_MESSAGE);
        }
    }                         

	
	
	/*Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeUpdate frame = new EmployeeUpdate();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/* Create the frame.*/
	public EmployeeUpdate() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Employee ID");
		lblNewLabel.setBounds(24, 35, 103, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(24, 69, 103, 14);
		contentPane.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(24, 100, 103, 14);
		contentPane.add(lblLastName);
		
		JLabel lblJob = new JLabel("Job");
		lblJob.setBounds(24, 143, 103, 14);
		contentPane.add(lblJob);
		
		idTextField = new JTextField();
		idTextField.setColumns(10);
		idTextField.setBounds(137, 32, 158, 20);
		contentPane.add(idTextField);
		
		fnameTextField = new JTextField();
		fnameTextField.setColumns(10);
		fnameTextField.setBounds(137, 66, 158, 20);
		contentPane.add(fnameTextField);
		
		lnameTextField = new JTextField();
		lnameTextField.setColumns(10);
		lnameTextField.setBounds(137, 97, 158, 20);
		contentPane.add(lnameTextField);
		
		jobTextField = new JTextField();
		jobTextField.setColumns(10);
		jobTextField.setBounds(137, 140, 158, 20);
		contentPane.add(jobTextField);
		
		JButton btnUpdateDetails = new JButton("Update Details");
		btnUpdateDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpdateActionPerformed(e);
			}
		});
		btnUpdateDetails.setBounds(164, 206, 131, 44);
		contentPane.add(btnUpdateDetails);
	}

}
